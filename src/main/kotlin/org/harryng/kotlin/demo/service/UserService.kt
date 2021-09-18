package org.harryng.kotlin.demo.service

import org.harryng.kotlin.demo.entity.UserImpl
import org.harryng.kotlin.demo.util.SessionHolder

interface UserService: BaseService<UserImpl, Long>{
    fun addUserBatch(sessionHolder: SessionHolder, users: List<UserImpl>, extras: Map<String, Any>)

    fun getByUsername(sessionHolder: SessionHolder, username: String, extras: Map<String, Any>) : UserImpl?
}