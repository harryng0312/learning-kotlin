package org.harryng.kotlin.demo.entity

import org.harryng.kotlin.demo.persistence.CounterPersistence

open class CounterModel(
    override var id: String,
    open var value: Long,
    open var maxValue: Long = value + CounterPersistence.DEFAULT_STEP + CounterPersistence.DEFAULT_CACHE_STEP
) : AbstractEntity<String>(), Entity<String>