package org.harryng.kotlin.demo.persistence

import java.io.Serializable
import java.lang.RuntimeException
import javax.persistence.EntityManager
import kotlin.jvm.Throws

interface BasePersistence<T : Any, Id : Serializable> {
    val entityManager: EntityManager
    val entityClass: Class<T>

    @Throws(RuntimeException::class)
    fun selectById(id: Id): T?

    @Throws(RuntimeException::class)
    fun insert(obj: T): Int

    @Throws(RuntimeException::class)
    fun update(obj: T): Int

    @Throws(RuntimeException::class)
    fun delete(id: Id): Int
}