package org.harryng.kotlin.demo.persistence

import org.harryng.kotlin.demo.entity.UserImpl

interface UserPersistence : BasePersistence<UserImpl, Long>