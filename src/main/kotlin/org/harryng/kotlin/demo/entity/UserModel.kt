package org.harryng.kotlin.demo.entity

import java.util.Date

open class UserModel(override var id: Long,
                     open var username: String,
                     open var passwd: String,
                     open var passwdEncryptedMethod: String,
                     open var screenName: String,
                     open var dob: Date)
    : AbstractEntity<Long>(), Entity<Long>