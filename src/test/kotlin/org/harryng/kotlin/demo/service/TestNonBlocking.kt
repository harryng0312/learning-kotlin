package org.harryng.kotlin.demo.service

import kotlinx.coroutines.*
import org.harryng.kotlin.demo.initApplicationContext
import org.harryng.kotlin.demo.util.SessionHolder
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TestNonBlocking {
    val logger: Logger = LoggerFactory.getLogger(TestNonBlocking::class.java)
    val sessionHolder: SessionHolder = SessionHolder()
    val extras: Map<String, Any> = mutableMapOf()

    @Before
    fun init() {
        initApplicationContext()
    }

    suspend fun function1(): String {
        logger.info("Thread: ${Thread.currentThread().name}:${Thread.currentThread().hashCode()}")
        delay(1_000L)
//        Thread.sleep(1_000L)
        val message = "function1"
        logger.info("Launch ${message}")
        return message
    }

    suspend fun function2(): String {
        logger.info("Thread: ${Thread.currentThread().name}:${Thread.currentThread().hashCode()}")
        delay(100L)
//        Thread.sleep(100L)
        val message = "function2"
        logger.info("Launch ${message}")
        return message
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun testCoroutine() {
        var resultOne = "Android"
        var resultTwo = "Kotlin"
        logger.info("Launch: Before")
//        GlobalScope.launch(Dispatchers.Default) { resultOne = function1() }
//        GlobalScope.launch(Dispatchers.Default) { resultTwo = function2() }
        logger.info("main Thread: ${Thread.currentThread().name}:${Thread.currentThread().hashCode()}")
        runBlocking {
//            launch(Dispatchers.Unconfined) { resultOne = function1() }
//            launch(Dispatchers.Unconfined) { resultTwo = function2() }
//            launch(Dispatchers.Default) { resultOne = function1() }
//            launch(Dispatchers.Default) { resultTwo = function2() }
//            launch(Dispatchers.IO) { resultOne = function1() }
//            launch(Dispatchers.IO) { resultTwo = function2() }
//            launch{ resultOne = function1() }
//            launch{ resultTwo = function2() }
            val deffered1 = async{function1()}
            val deffered2 = async{function2()}
//            val deffered1 = async(Dispatchers.Default){function1()}
//            val deffered2 = async(Dispatchers.Default){function2()}
            resultOne = deffered1.await()
            resultTwo = deffered2.await()
        }
        logger.info("Launch: After")
        val resultText = resultOne + resultTwo
        logger.info("Launch $resultText")
    }
}