package org.harryng.kotlin.demo

import org.harryng.kotlin.demo.entity.UserImpl
import org.harryng.kotlin.demo.service.CounterService
import org.harryng.kotlin.demo.util.SessionHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.atomic.AtomicLong

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
}

fun testGetParalleExecutorCounterIncrement() {
    logger.info("=====")
    // 2_000 (500 * 4 in 1.682s) ()
    // 20_000 (5_000 * 4 in 2.373s)
    // 20_000 * 10 in 2.557s
    // 200_000 * 4 in 9.666s
    // 2_000_000 in 17.312s
    // 20_000_000 in 120.016s
    val noOfWorker = 4
    val loop = 1
    val counterService = SpringUtil.applicationContext.getBean("counterService") as CounterService
    var currVal = UserImpl::class.qualifiedName?.let { counterService.currentValue(it) }
    logger.info("Get currval:${currVal}")
    val atomRs = AtomicLong(currVal ?: 0L)

    val pool = Executors.newFixedThreadPool(noOfWorker)
    val getNextVal = fun(): Long {
        var lastRs = 0L
        for (i in 0 until loop step 1) {
            val nextVal = UserImpl::class.qualifiedName?.let { counterService.currentValue(it) }
//                val nextVal = UserImpl::class.qualifiedName?.let { counterService.increment(it) }
            if (nextVal != null) {
                atomRs.set(nextVal)
                lastRs = nextVal
            }
        }
        return lastRs
    }
    val lsTask = mutableListOf<Future<Long>>()
    for (i in 0 until noOfWorker step 1) {
        val task = pool.submit(getNextVal)
        lsTask.add(task)
        logger.info("${i}\tTask:${task.hashCode()}")
    }
    logger.info("+++++")
    for (task in lsTask) {
        val lastVal = task.get()
        logger.info("Last:${lastVal}")
    }
    pool.shutdown()
    currVal = UserImpl::class.qualifiedName?.let { counterService.currentValue(it) }
    logger.info("Counter value: ${currVal}")
    logger.info("Client result: ${atomRs.get()}")
}
val logger: Logger = LoggerFactory.getLogger("main")
val sessionHolder: SessionHolder = SessionHolder()
val extras: Map<String, Any> = mutableMapOf()
fun main(args: Array<String>) {
    testGetParalleExecutorCounterIncrement()
}