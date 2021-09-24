package org.harryng.kotlin.demo

import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import kotlin.coroutines.CoroutineContext

class SpringUtil {
    companion object {
        val applicationContext: ApplicationContext by lazy { createApplicationContext() }
        private fun createApplicationContext(): ApplicationContext {
            return ClassPathXmlApplicationContext("classpath:spring-conf.xml")
        }
    }
}

fun initApplicationContext() {
}

val logger: Logger = LoggerFactory.getLogger("main")


fun main(args: Array<String>) {

}