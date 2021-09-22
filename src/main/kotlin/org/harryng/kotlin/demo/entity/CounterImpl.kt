package org.harryng.kotlin.demo.entity

import java.util.concurrent.atomic.AtomicLong

data class CounterImpl(
    override var id: String,
    override var value: AtomicLong
) : CounterModel(id, value) {
    constructor() : this("", AtomicLong(0L))
}