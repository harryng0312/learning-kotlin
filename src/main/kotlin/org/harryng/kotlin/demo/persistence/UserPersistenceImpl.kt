package org.harryng.kotlin.demo.persistence

import org.harryng.kotlin.demo.entity.UserImpl

class UserPersistenceImpl : AbstractPersistence<UserImpl, Long>(UserImpl::class.java), UserPersistence{}