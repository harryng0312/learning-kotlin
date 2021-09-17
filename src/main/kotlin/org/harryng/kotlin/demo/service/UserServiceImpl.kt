package org.harryng.kotlin.demo.service

import org.harryng.kotlin.demo.entity.UserImpl
import org.harryng.kotlin.demo.persistence.UserPersistence
import org.springframework.beans.factory.annotation.Autowired

class UserServiceImpl :
    AbstractService<UserImpl, Long>(),
    UserService {
    @Autowired
    override lateinit var persistence: UserPersistence
}