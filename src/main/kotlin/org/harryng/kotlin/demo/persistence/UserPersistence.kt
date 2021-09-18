package org.harryng.kotlin.demo.persistence

import org.harryng.kotlin.demo.entity.UserImpl
import org.springframework.data.repository.query.Param

interface UserPersistence : BasePersistence<UserImpl, Long> {
//    @Query("select u from org.harryng.kotlin.demo.entity.UserImpl u where u.username = :username")
    fun selectByUsername(@Param("username") username: String) : UserImpl
}