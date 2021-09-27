package org.harryng.kotlin.demo.rpc.interceptor

import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import org.slf4j.LoggerFactory

class AuthInterceptor : ServerInterceptor {
    companion object {
        private val logger = LoggerFactory.getLogger(AuthInterceptor::class.java)
    }
    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        logger.info("Auth interceptor")
        return next.startCall(call, headers)
    }
}