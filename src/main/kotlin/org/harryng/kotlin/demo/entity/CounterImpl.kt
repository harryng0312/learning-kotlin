package org.harryng.kotlin.demo.entity

data class CounterImpl(
    override var id: String,
    override var value: Long
) : CounterModel(id, value)