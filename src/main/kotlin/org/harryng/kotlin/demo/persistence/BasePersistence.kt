package org.harryng.kotlin.demo.persistence

import java.io.Serializable
import javax.persistence.EntityManager

interface BasePersistence<T: Any, Id: Serializable> {
    val entityManager: EntityManager
    val entityClass: Class<T>
    fun selectById(id: Id): T
    fun insert(obj: T): Int
    fun update(obj: T): Int
    fun delete(id: Id): Int
}