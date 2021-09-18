package org.harryng.kotlin.demo.service

import org.harryng.kotlin.demo.SpringUtil
import org.harryng.kotlin.demo.entity.UserImpl
import org.harryng.kotlin.demo.initApplicationContext
import org.harryng.kotlin.demo.util.SessionHolder
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TestCounterService {
    val logger: Logger = LoggerFactory.getLogger(TestCounterService::class.java)
    val sessionHolder: SessionHolder = SessionHolder()
    val extras: Map<String, Any> = mutableMapOf()

    @Before
    fun init() {
        initApplicationContext()
    }

    @Test
    fun testGetCounterIncrement(){
        logger.info("=====")
        val loop = 2_000
        val counterService = SpringUtil.applicationContext.getBean("counterService") as CounterService
        val currVal = UserImpl::class.qualifiedName?.let {counterService.currentValue(it) }
        logger.info("Get currval:${currVal}")
        for(i in 0..loop step 1){
            val nextVal = UserImpl::class.qualifiedName?.let { counterService.increment(it) }
            logger.info("Get nextval:${nextVal}")
        }
    }
}