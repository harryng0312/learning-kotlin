package org.harryng.kotlin.demo.service

import com.google.protobuf.ByteString
import io.grpc.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.harryng.kotlin.demo.SpringUtil
import org.harryng.kotlin.demo.initApplicationContext
import org.harryng.kotlin.demo.rpc.service.ChatServiceKtImpl
import org.harryng.kotlin.demo.rpc.gen.ChatMessage
import org.harryng.kotlin.demo.rpc.gen.ChatServiceGrpcKt
import org.harryng.kotlin.demo.rpc.gen.MessageState
import org.harryng.kotlin.demo.rpc.server.GrpcServer
import org.harryng.kotlin.demo.util.SessionHolder
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.Charset

class TestChatService {

    val logger: Logger = LoggerFactory.getLogger(TestChatService::class.java)
    val sessionHolder: SessionHolder = SessionHolder()
    val extras: Map<String, Any> = mutableMapOf()
    val port = 50051

    @Before
    fun init() {
        initApplicationContext()
    }

    @Test
    fun testStartServer() {
        val grpcServer = SpringUtil.applicationContext.getBean(GrpcServer::class.java)
        grpcServer.block()
    }

    @Test
    fun testSendChatStream() {
        val n = 10
        val msg = ChatMessage.newBuilder()
        msg.id = "1"
        msg.message = ByteString.copyFrom("test message", Charset.forName("utf8"))
        msg.senderId = "me"
        msg.receiverId = "you"
        msg.state = MessageState.NOT_SEND
        runBlocking {
            val requests = flow<ChatMessage> {
                for (i in 0 until n step 1) {
                    var mess = ChatMessage.newBuilder()
                    mess.id = msg.id;
                    mess.senderId = msg.senderId;
                    mess.receiverId = msg.receiverId;
                    mess.state = msg.state;
                    mess.message = ByteString.copyFrom(msg.message.toString("utf-8"), Charset.forName("utf8"))
//                    Thread.sleep(2_000)
                    delay(1_000)
                    logger.info("create msg:$i");
                    emit(mess.build())
                }
            }
//        val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()
            val channel = ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .compressorRegistry(CompressorRegistry.getDefaultInstance())
                .build()
            try {
                val client = ChatServiceGrpcKt.ChatServiceCoroutineStub(channel)
                val response = client.sendChatStream(requests)
//                response.collect {
//                    logger.info("From Server: ${it.state}")
//                }
                response.collect {
                    logger.info("From Server: ${it.state}")
                }
            } catch (e: Exception) {
                logger.error("", e)
            } finally {
                channel.shutdown()
            }
        }
    }
}