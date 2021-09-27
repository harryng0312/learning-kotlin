package org.harryng.kotlin.demo.rpc.service

import kotlinx.coroutines.flow.Flow
import org.harryng.kotlin.demo.rpc.gen.*
import kotlin.jvm.Throws

interface ChatServiceKt {
    @Throws(Exception::class)
    suspend fun getCurrentDate(request: GetCurrentDateRequest): GetCurrentDateResponse
    @Throws(Exception::class)
    suspend fun loginChat(request: AuthRequest): AuthResponse
    @Throws(Exception::class)
    suspend fun logoutChat(request: AuthRequest): AuthResponse
    @Throws(Exception::class)
    fun sendChatStream(requests: Flow<ChatMessage>): Flow<ChatSignal>
}