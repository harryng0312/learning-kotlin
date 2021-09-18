package org.harryng.kotlin.demo.service

import org.harryng.kotlin.demo.entity.CounterImpl
import org.harryng.kotlin.demo.persistence.CounterPersistence
import kotlin.jvm.Throws

interface CounterService{
    @Throws(RuntimeException::class)
    fun insert(id: String, initValue: Long = CounterPersistence.DEFAULT_INIT_VALUE) : CounterImpl

    @Throws(RuntimeException::class)
    fun increment(id:String, step: Int = CounterPersistence.DEFAULT_STEP) : Long

    @Throws(RuntimeException::class)
    fun currentValue(id:String): Long
}