package org.harryng.kotlin.demo.service

import org.harryng.kotlin.demo.entity.UserImpl
import org.harryng.kotlin.demo.persistence.UserPersistence
import org.harryng.kotlin.demo.util.SessionHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable

open class UserServiceImpl :
    AbstractService<UserImpl, Long>(),
    UserService {

    @Autowired
    override lateinit var persistence: UserPersistence

    override fun addUserBatch(sessionHolder: SessionHolder, users: List<UserImpl>, extras: Map<String, Any>) {

    }

//    @Cacheable(value= ["user"], key = "#username")
    override fun getByUsername(sessionHolder: SessionHolder, username: String, extras: Map<String, Any>): UserImpl? {
        return persistence.selectByUsername(username)
    }
}