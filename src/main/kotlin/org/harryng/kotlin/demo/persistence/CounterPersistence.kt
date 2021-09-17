package org.harryng.kotlin.demo.persistence

import org.harryng.kotlin.demo.entity.CounterImpl
import javax.persistence.EntityManager
import kotlin.jvm.Throws

interface CounterPersistence {
    val entityManager: EntityManager
    companion object{
        const val DEFAULT_STEP = 1
        const val DEFAULT_INIT_VALUE = 1L
        const val DEFAULT_CACHE_STEP = 20
    }
    @Throws(RuntimeException::class)
    fun insert(id: String, initValue: Long = DEFAULT_INIT_VALUE) : CounterImpl
    @Throws(RuntimeException::class)
    fun increment(id:String, step: Int = DEFAULT_STEP) : Long
    @Throws(RuntimeException::class)
    fun currentValue(id:String): Long
}