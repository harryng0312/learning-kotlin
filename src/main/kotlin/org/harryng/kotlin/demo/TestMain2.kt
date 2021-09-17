package org.harryng.kotlin.demo

import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

class SpringUtil {
    companion object {
        val applicationContext: ApplicationContext by lazy { createApplicationContext() }
        private fun createApplicationContext(): ApplicationContext {
            var appCtx: ApplicationContext = ClassPathXmlApplicationContext("classpath:spring-conf.xml")
            return appCtx
        }
    }
}

fun initApplicationContext() {
    println(SpringUtil.applicationContext.hashCode())
}


fun main(args: Array<String>) {
    initApplicationContext()
}