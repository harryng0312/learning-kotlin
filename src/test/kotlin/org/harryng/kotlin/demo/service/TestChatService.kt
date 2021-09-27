package org.harryng.kotlin.demo.service

import com.google.protobuf.ByteString
import io.grpc.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.harryng.kotlin.demo.initApplicationContext
import org.harryng.kotlin.demo.rpc.ChatServiceKtImpl
import org.harryng.kotlin.demo.rpc.gen.ChatMessage
import org.harryng.kotlin.demo.rpc.gen.ChatServiceGrpcKt
import org.harryng.kotlin.demo.rpc.gen.MessageState
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
    private lateinit var server: Server
    private val port = 50051

    private class LogInterceptor : ServerInterceptor {
        val logger: Logger = LoggerFactory.getLogger(LogInterceptor::class.java)
        override fun <ReqT : Any?, RespT : Any?> interceptCall(
            call: ServerCall<ReqT, RespT>,
            headers: Metadata,
            next: ServerCallHandler<ReqT, RespT>
        ): ServerCall.Listener<ReqT> {
            logger.info("${call.methodDescriptor.fullMethodName}:${call.attributes}")
            return next.startCall(
                call,
                headers
            )
        }
    }

    private fun startServer() {
        val interceptor = LogInterceptor()
        server = ServerBuilder.forPort(port)
            .addService(ChatServiceKtImpl())
            .intercept(interceptor)
            .compressorRegistry(CompressorRegistry.getDefaultInstance())
            .build()
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                server.shutdown()
                println("*** server shut down")
            }
        )
        server.awaitTermination()
    }

    @Before
    fun init() {
        initApplicationContext()
    }

    @Test
    fun testStartServer() {
        startServer()
    }

    @Test
    fun testSendChatStream() {
        val n = 10
        var msg = ChatMessage.newBuilder()
        msg.id = "1"
        msg.message = ByteString.copyFrom("test message", Charset.forName("utf8"))
        msg.senderId = "me"
        msg.receiverId = "you"
        msg.state = MessageState.NOT_SEND
        runBlocking {
            async(Dispatchers.Default) {
                startServer()
            }
            delay(2_000)
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
                val response = async { client.sendChatStream(requests) }
//                response.collect {
//                    logger.info("From Server: ${it.state}")
//                }
                response.await().collect {
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