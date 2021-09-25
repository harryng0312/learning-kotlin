package org.harryng.kotlin.demo.rpc

import kotlinx.coroutines.flow.Flow
import org.harryng.kotlin.demo.rpc.gen.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ChatService : ChatServiceGrpcKt.ChatServiceCoroutineImplBase() {
    companion object{
        val dataFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    }
    override suspend fun getCurrentDate(request: GetCurrentDateRequest): GetCurrentDateResponse {
        val res = GetCurrentDateResponse.newBuilder().apply {
            val now = Date()
            this.result = dataFormat.format(now)
        }.build()
        return res
    }

    override suspend fun loginChat(request: AuthRequest): AuthResponse {
        return super.loginChat(request)
    }

    override suspend fun logoutChat(request: AuthRequest): AuthResponse {
        return super.logoutChat(request)
    }

    override fun sendChatStream(requests: Flow<ChatMessage>): Flow<ChatSignal> {
        return super.sendChatStream(requests)
    }
}