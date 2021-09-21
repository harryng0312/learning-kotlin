package org.harryng.kotlin.demo.service

import org.harryng.kotlin.demo.SpringUtil
import org.harryng.kotlin.demo.entity.UserImpl
import org.harryng.kotlin.demo.initApplicationContext
import org.harryng.kotlin.demo.util.SessionHolder
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicLong

class TestCounterService {
    val logger: Logger = LoggerFactory.getLogger(TestCounterService::class.java)
    val sessionHolder: SessionHolder = SessionHolder()
    val extras: Map<String, Any> = mutableMapOf()

    @Before
    fun init() {
        initApplicationContext()
    }

    @Test
    fun testGetCounterIncrement() {
        logger.info("=====")
        // 2000 in 1.753s
        // 20_000 in 2.557s
        // 200_000 in 4.380s
        // 2_000_000 in 17.312s
        // 20_000_000 in 120.016s
        val loop = 200_000
        val counterService = SpringUtil.applicationContext.getBean("counterService") as CounterService
        var currVal = UserImpl::class.qualifiedName?.let { counterService.currentValue(it) }
        logger.info("Get currval:${currVal}")
        val atomRs = AtomicLong(currVal ?: 0L)
        for (i in 0 until loop step 1) {
            val nextVal = UserImpl::class.qualifiedName?.let { counterService.increment(it) }
            if (nextVal != null) {
                atomRs.set(nextVal)
            }
//            logger.info("Get nextval:${nextVal}")
        }
        currVal = UserImpl::class.qualifiedName?.let { counterService.currentValue(it) }
        logger.info("Counter value: ${currVal}")
        logger.info("Result: ${atomRs.get()}")
    }

    @Test
    fun testGetParalleExecutorCounterIncrement() {
        logger.info("=====")
        // 2_000 (500 * 4 in 1.682s) (100 * 20 in 2.698s) (50 * 40 in 2.693)
        // 20_000 (5_000 * 4 in 2.373s)
        // 20_000 * 10 in 2.557s
        // 200_000 * 4 in 9.666s
        // 2_000_000 in 17.312s
        // 20_000_000 in 120.016s
        val noOfWorker = 40
        val loop = 50
        val counterService = SpringUtil.applicationContext.getBean("counterService") as CounterService
        var currVal = UserImpl::class.qualifiedName?.let { counterService.currentValue(it) }
        logger.info("Get currval:${currVal}")
        val atomRs = AtomicLong(currVal ?: 0L)

        val pool = Executors.newFixedThreadPool(noOfWorker)
        val getNextVal = fun(): Long {
            var lastRs = 0L
            for (i in 0 until loop step 1) {
//                val nextVal = UserImpl::class.qualifiedName?.let { counterService.currentValue(it) }
                val nextVal = UserImpl::class.qualifiedName?.let { counterService.increment(it) }
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

    @Test
    fun testGetParalleForkJoinPoolCounterIncrement() {
        logger.info("=====")
        // 2000 in 1.753s (200 * 10 in 2.630s) (100 * 20 in 2.647s) (50 * 40 in 2.681)
        // 20_000 * 10 in 2.557s (200 * 100 in 3.929s) (100 * 200 in 4.033s) (50 * 400 in 4.057)
        // 200_000 * 4 in 9.666s (200 * 1000 in 8.767s) (100 * 2000 in 4.033s) (50 * 4000 in 4.057)
        // 2_000_000 in 17.312s
        // 20_000_000 in 120.016s
        val noOfWorker = 2000
        val loop = 100
        val counterService = SpringUtil.applicationContext.getBean("counterService") as CounterService
        var currVal = UserImpl::class.qualifiedName?.let { counterService.currentValue(it) }
        logger.info("Get currval:${currVal}")
        val atomRs = AtomicLong(currVal ?: 0L)

        val pool = ForkJoinPool(noOfWorker)
        val getNextVal = fun(): Long {
            var lastRs = 0L
            for (i in 0 until loop step 1) {
                val nextVal = UserImpl::class.qualifiedName?.let { counterService.increment(it) }
                if (nextVal != null) {
                    atomRs.set(nextVal)
                    lastRs = nextVal
                }
            }
            return lastRs
        }
//        val task : () -> Long = getNextVal
//        val task:ForkJoinTask<Long> = ForkJoinTask.adapt(getNextVal)
        val lsTask = mutableListOf<ForkJoinTask<Long>>()
//        logger.info("seed task:${task.hashCode()}")
        for (i in 0 until noOfWorker step 1) {
//            val callable = task.fork()
//            val callable: () -> Long = getNextVal
            val callable = ForkJoinTask.adapt(getNextVal)
            val task = pool.submit(callable)
            lsTask.add(task)
        }
        for(task in lsTask){
            logger.info("Task:${task.hashCode()}")
        }
        logger.info("+++++")
        for (task in lsTask) {
//            val lastVal = lsTask.first().join()
            val lastVal = task.join()
            logger.info("Last:${lastVal}")
        }
        pool.shutdown()
        currVal = UserImpl::class.qualifiedName?.let { counterService.currentValue(it) }
        logger.info("Counter value: ${currVal}")
        logger.info("Client result: ${atomRs.get()}")
    }
}