package org.harryng.kotlin.demo.service

import org.harryng.kotlin.demo.persistence.BasePersistence
import org.harryng.kotlin.demo.util.SessionHolder
import java.io.Serializable

abstract class AbstractService<T : Any, Id : Serializable> : BaseService<T, Id> {
    abstract override val persistence: BasePersistence<T, Id>

    override fun getById(sessionHolder: SessionHolder, id: Id, extras: Map<String, Any>): T {
        val obj = persistence.selectById(id)
        return obj
    }

    override fun add(sessionHolder: SessionHolder, obj: T, extras: Map<String, Any>) {
        persistence.insert(obj)
    }

    override fun edit(sessionHolder: SessionHolder, obj: T, extras: Map<String, Any>) {
        persistence.update(obj)
    }

    override fun remove(sessionHolder: SessionHolder, id: Id, extras: Map<String, Any>) {
        persistence.delete(id)
    }
}