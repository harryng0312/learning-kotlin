package org.harryng.kotlin.demo.entity

import org.harryng.kotlin.demo.util.ValueUtil
import java.util.*

data class UserImpl(override var id: Long,
                    override var username: String,
                    override var passwd: String,
                    override var passwdEncryptedMethod: String,
                    override var screenName: String,
                    override var dob: Date):
    UserModel(id, username, passwd, passwdEncryptedMethod, screenName, dob) {
    constructor() : this(0, "", "", "", "", ValueUtil.MIN_DATE)
}