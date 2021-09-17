package org.harryng.kotlin.demo.service

import org.harryng.kotlin.demo.SpringUtil
import org.harryng.kotlin.demo.entity.UserImpl
import org.harryng.kotlin.demo.initApplicationContext
import org.harryng.kotlin.demo.util.SessionHolder
import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

//@RunWith(SpringRunner::class)
//@SpringBootTest(classes = Application::class)
//@Import(Application::class)
class TestUserService {
    val logger: Logger = LoggerFactory.getLogger(TestUserService::class.java)
    val sessionHolder: SessionHolder = SessionHolder()
    val extras: Map<String, Any> = mutableMapOf()

    @Before
    fun init() {
        initApplicationContext()
    }

    @Test
    fun testUserService() {
        logger.info("=====:${extras.javaClass}")
        var user: UserImpl = UserImpl(
            id = 1, username = "username1", passwd = "passwd1", passwdEncryptedMethod = "plain",
            screenName = "screenname1", dob = Date(),
        )
        var userService = SpringUtil.applicationContext.getBean("userService") as UserService
        logger.info("userService.persistence:${userService.persistence.hashCode()}")
        userService.add(sessionHolder, user, extras)
        logger.info("sussess")
    }
}