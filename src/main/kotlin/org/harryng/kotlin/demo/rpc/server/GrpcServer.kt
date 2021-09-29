package org.harryng.kotlin.demo.rpc.server

import io.grpc.*
import org.slf4j.LoggerFactory
import java.io.InputStream


class GrpcServer(
    val port: Int = 50051,
    val services: List<BindableService> = listOf(),
    val interceptors: List<ServerInterceptor> = listOf(),
    var certChain: InputStream? = null,
    var privateKey: InputStream? = null
) {
    companion object {
        private val logger = LoggerFactory.getLogger(GrpcServer::class.java)
    }

    private lateinit var server: Server

    fun init() {
//        val compressorRegistry: CompressorRegistry = CompressorRegistry.newEmptyInstance()
//        compressorRegistry.register(Codec.Gzip())
//        compressorRegistry.register(Codec.Identity.NONE)
        val compressorRegistry: CompressorRegistry = CompressorRegistry.getDefaultInstance()
        lateinit var serverCredential: ServerCredentials
//        if (args.length === 4) {
//            tlsBuilder.trustManager(File(args[3]))
//            tlsBuilder.clientAuth(TlsServerCredentials.ClientAuth.REQUIRE)
//        }
//        val serverBuilder = ServerBuilder
//            .forPort(port)
//            .compressorRegistry(compressorRegistry)
//        certChain = this.javaClass.classLoader.getResourceAsStream("ks/ca.pem")
//        privateKey = this.javaClass.classLoader.getResourceAsStream("ks/ca.key")
        if (certChain != null && privateKey != null) {
            serverCredential = TlsServerCredentials.newBuilder()
                .keyManager(certChain, privateKey)
                .clientAuth(TlsServerCredentials.ClientAuth.OPTIONAL)
                .build()
            logger.info("TLS Server credential")
        }else{
            serverCredential = InsecureServerCredentials.create()
            logger.info("Insecure Server credential")
        }
        val serverBuilder = Grpc.newServerBuilderForPort(port, serverCredential)
            .compressorRegistry(compressorRegistry)
//        val serverBuilder = ServerBuilder.forPort (port)
//            .compressorRegistry(compressorRegistry)
//        if (certChainFile != null && privateKey != null) {
//            serverBuilder.useTransportSecurity(certChainFile, privateKey)
//        }
        services.forEach { elem -> serverBuilder.addService(elem) }
        interceptors.forEach { elem -> serverBuilder.intercept(elem) }
        server = serverBuilder.build()
    }

    fun start() {
        server.start()
        logger.info("Server is started!")
        Runtime.getRuntime().addShutdownHook(Thread {
            logger.info("Shutting down gRPC server.")
            this.stop()
            logger.info("gRPC server shut down successfully.")
        })
    }

    fun stop() {
        server.shutdown()
    }

    fun block() {
        server.awaitTermination()
    }
}
