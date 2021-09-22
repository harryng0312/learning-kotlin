package org.harryng.kotlin.demo.persistence

import java.util.concurrent.locks.ReentrantLock

open class CounterLockerPersistenceImpl : CounterPersistenceImpl() {
    private val locker = ReentrantLock() //CounterLocker()

    override fun increment(id: String, step: Int): Long {
        var rs: Long
        try {
            locker.lock()
            rs = doIncrement(id, step)
        } finally {
            locker.unlock()
        }
        return rs
    }

    override fun currentValue(id: String): Long {
        var rs: Long
        try {
            locker.lock()
            rs = currentCounter(id).value.get()
        } finally {
            locker.unlock()
        }
        return rs
    }
}