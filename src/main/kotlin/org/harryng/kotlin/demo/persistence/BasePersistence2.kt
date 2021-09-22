package org.harryng.kotlin.demo.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.io.Serializable

//@NoRepositoryBean
interface BasePersistence2<T : Any, Id : Serializable> : JpaRepository<T, Id> {

    @Throws(RuntimeException::class)
    fun selectById(id: Id): T?

    @Throws(RuntimeException::class)
    fun insert(obj: T): Int

    @Throws(RuntimeException::class)
    fun update(obj: T): Int

    @Throws(RuntimeException::class)
    fun delete(id: Id): Int
}