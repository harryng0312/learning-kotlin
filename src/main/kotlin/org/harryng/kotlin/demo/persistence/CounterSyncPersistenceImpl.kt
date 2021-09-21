package org.harryng.kotlin.demo.persistence

import org.harryng.kotlin.demo.entity.CounterImpl
import org.harryng.kotlin.demo.kernel.CounterLocker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import java.util.concurrent.locks.ReentrantLock
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Expression

open class CounterSyncPersistenceImpl : CounterPersistenceImpl() {
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