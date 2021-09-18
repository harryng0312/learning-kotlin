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
    fun testGetUserById(){
        logger.info("=====")
        var id: Long = 2L
        var userService = SpringUtil.applicationContext.getBean("userService") as UserService
        try {
            var user = userService.getById(sessionHolder, id, extras)
            logger.info("user: ${user?.username}")
        }catch (e: NullPointerException){
            logger.error("", e)
        }
    }

    @Test
    fun testGetUserByUsername(){
        logger.info("=====")
        var username = "username1"
        var userService = SpringUtil.applicationContext.getBean("userService") as UserService
        try {
            var user = userService.getByUsername(sessionHolder, username, extras)
            logger.info("user: ${user?.id}")
        }catch (e: NullPointerException){
            logger.error("", e)
        }
    }

    @Test
    fun testAddUser() {
        logger.info("=====")
        var user: UserImpl = UserImpl(
            id = 1, username = "username1", passwd = "passwd1", passwdEncryptedMethod = "plain",
            screenName = "screenname1", dob = Date(),
        )
        var userService = SpringUtil.applicationContext.getBean("userService") as UserService
        logger.info("userService.persistence:${userService.persistence.hashCode()}")
        userService.add(sessionHolder, user, extras)
        logger.info("sussess")
    }

    @Test
    fun testUpdateUser(){
        logger.info("=====")
        val user: UserImpl = UserImpl(
            id = 1, username = "username1", passwd = "passwd1", passwdEncryptedMethod = "plain",
            screenName = "screenname1-updated", dob = Date(),
        )
        val userService = SpringUtil.applicationContext.getBean("userService") as UserService
        userService.edit(sessionHolder, user, extras)
//        var userAfter = userService.getById(sessionHolder, 1, extras)
        logger.info("After:${user?.screenName}")
    }

    @Test
    fun testAddUserBatch(){

    }
}