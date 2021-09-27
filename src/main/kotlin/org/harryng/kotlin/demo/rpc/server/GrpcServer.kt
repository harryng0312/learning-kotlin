package org.harryng.kotlin.demo.rpc.server

import io.grpc.*
import org.slf4j.LoggerFactory

class GrpcServer(val port: Int = 50051,
                 val services: List<BindableService> = listOf(),
                 val interceptors: List<ServerInterceptor> = listOf()
) {
    companion object {
        private val logger = LoggerFactory.getLogger(GrpcServer::class.java)
    }

    private lateinit var server: Server

    private fun init() {
        val serverBuilder = ServerBuilder
            .forPort(port)
            .compressorRegistry(CompressorRegistry.getDefaultInstance())
        services.forEach { elem -> serverBuilder.addService(elem) }
        interceptors.forEach { elem -> serverBuilder.intercept(elem) }
        server = serverBuilder.build()
    }

    fun startServer() {
        init()
        server.start()
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
