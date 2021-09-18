package org.harryng.kotlin.demo.service

import org.harryng.kotlin.demo.persistence.BasePersistence
import org.harryng.kotlin.demo.util.SessionHolder
import java.io.Serializable

interface BaseService<T: Any, Id: Serializable>{
    val persistence: BasePersistence<T, Id>
    fun getById(sessionHolder: SessionHolder, id: Id, extras: Map<String, Any>): T?
    fun add(sessionHolder: SessionHolder, obj: T, extras: Map<String, Any>)
    fun edit(sessionHolder: SessionHolder, obj: T, extras: Map<String, Any>)
    fun remove(sessionHolder: SessionHolder, id: Id, extras: Map<String, Any>)
}