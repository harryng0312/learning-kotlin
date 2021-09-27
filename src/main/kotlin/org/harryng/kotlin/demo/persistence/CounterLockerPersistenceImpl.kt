package org.harryng.kotlin.demo.persistence

import org.harryng.kotlin.demo.SpringUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.getBean
import java.util.concurrent.locks.ReentrantLock

open class CounterLockerPersistenceImpl : CounterPersistenceImpl() {
//    @Autowired
//    @Qualifier("counterPersistence")
//    private lateinit var counterPersistence: CounterPersistence

    private val locker = ReentrantLock()

    override fun increment(id: String, step: Int): Long {
        var rs: Long
        try {
            locker.lock()
            val counterPersistence: CounterPersistence = SpringUtil.applicationContext.getBean("counterPersistence") as CounterPersistence
            rs = counterPersistence.doIncrement(id, step)
//            rs = doIncrement(id, step)
        } finally {
            locker.unlock()
        }
        return rs
    }

    override fun currentValue(id: String): Long {
        var rs: Long
        try {
            locker.lock()
            val counterPersistence: CounterPersistence = SpringUtil.applicationContext.getBean("counterPersistence") as CounterPersistence
            rs = counterPersistence.currentCounter(id).value.get()
//            rs = currentCounter(id).value.get()
        } finally {
            locker.unlock()
        }
        return rs
    }
}