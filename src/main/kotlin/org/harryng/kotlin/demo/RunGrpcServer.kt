package org.harryng.kotlin.demo

import org.harryng.kotlin.demo.rpc.server.GrpcServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

val logger: Logger = LoggerFactory.getLogger("main")

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

fun runServer(){
    val grpcServer = SpringUtil.applicationContext.getBean(GrpcServer::class.java)
    grpcServer.start()
    grpcServer.block()
}

fun main(args: Array<String>) {
    initApplicationContext()
    runServer()
}