package org.harryng.kotlin.demo.persistence

import org.harryng.kotlin.demo.entity.CounterImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock
import javax.persistence.EntityManager
import javax.persistence.LockModeType

open class CounterLockerPersistenceImpl : CounterPersistenceImpl() {

    companion object {
        @JvmStatic
        protected val lock = Object()
    }

    private val locker = ReentrantLock()

    @Autowired
    @Qualifier("cacheManager")
    private lateinit var cacheManager: CacheManager

    @Autowired
    @Qualifier("namedJdbcTemplate")
    private lateinit var namedJdbcTemplate: NamedParameterJdbcTemplate

    override fun updateCounter(id: String): Long {
        val selectSql = "select value_ from counter where id_ = :id for update"
        val updateSql = "update counter set value_ = :value where id_ = :id"
        val selectParams = mutableMapOf<String, Any>("id" to id)
        val value: Long =
            namedJdbcTemplate.queryForObject(selectSql, selectParams) ret@{ rs, _ -> return@ret rs.getLong("value_") }
        val newValue = (value + CounterPersistence.DEFAULT_CACHE_STEP)
        val updateParams = mutableMapOf<String, Any>("id" to id, "value" to newValue)
        namedJdbcTemplate.update(updateSql, updateParams)
        return value
    }

    override fun currentCounter(id: String): CounterImpl {
        var counter: CounterImpl?
        var cacheValue: Cache.ValueWrapper? = cache[id]
        // check cache
        if (cacheValue == null) {
            // if not exits in cache - select from db
            counter = CounterImpl(id, AtomicLong(updateCounter(id)))
            // if not exits in db - create into db and cache
            if (counter == null) {
                counter = insert(id)
            }
            cache.put(id, counter)
            cacheValue = cache[id]
            counter = cacheValue?.get() as CounterImpl
        } else {
            counter = cacheValue.get() as CounterImpl
        }
        return counter
    }

    override fun insert(id: String, initValue: Long): CounterImpl {
        val counter = CounterImpl(id, AtomicLong(initValue))
        val insertSql = "insert into counter(id_, value_) values (:id, :value)"
        val updateParams = mutableMapOf<String, Any>("id" to id, "value" to counter.maxValue)
        namedJdbcTemplate.update(insertSql, updateParams)
        counter.maxValue = counter.value.get() + CounterPersistence.DEFAULT_CACHE_STEP
        return counter
    }

    override fun increment(id: String, step: Int): Long {
        var rs: Long
        try {
            locker.lock()
            rs = doIncrement(id, step)
        } finally {
            locker.unlock()
        }
//        synchronized(lock){
//            rs = doIncrement(id, step)
//        }
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