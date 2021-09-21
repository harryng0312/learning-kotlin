package org.harryng.kotlin.demo.persistence

import java.util.concurrent.locks.ReentrantLock

open class CounterLockerPersistenceImpl : CounterPersistenceImpl() {
    private val locker = ReentrantLock() //CounterLocker()

    override fun increment(id: String, step: Int): Long {
        locker.lock()
        val rs = doIncrement(id, step)
        locker.unlock()
        return rs
    }

    override fun currentValue(id: String): Long {
        locker.lock()
        val rs = currentCounter(id).value
        locker.unlock()
        return rs
    }
}