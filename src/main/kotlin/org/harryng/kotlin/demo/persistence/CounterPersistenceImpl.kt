package org.harryng.kotlin.demo.persistence

import org.harryng.kotlin.demo.entity.CounterImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import java.util.concurrent.atomic.AtomicLong
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Expression

open class CounterPersistenceImpl : CounterPersistence {

    companion object {
        @JvmStatic
        protected val lock = Object()
    }

    @PersistenceContext(name = "primary")
    private lateinit var defaultEntityManager: EntityManager

    @Autowired
    @Qualifier("cacheManager")
    private lateinit var cacheManager: CacheManager
    protected val cache: Cache get() = cacheManager.getCache("counter")


    override val entityManager: EntityManager
        get() = defaultEntityManager

    protected fun updateCounter(id: String, step: Int): Long {
//        var value = value + step + CounterPersistence.DEFAULT_CACHE_STEP
        val cb = entityManager.criteriaBuilder
        val selectCri = cb.createQuery(CounterImpl::class.java)
        val selectRoot = selectCri.from(CounterImpl::class.java)
        val updateCri = cb.createCriteriaUpdate(CounterImpl::class.java)
        val updateRoot = updateCri.from(CounterImpl::class.java)

        selectCri.where(cb.equal(selectRoot.get<Expression<*>>("id"), id))
        val selectQuery = entityManager.createQuery(selectCri)
        selectQuery.lockMode = LockModeType.PESSIMISTIC_WRITE
        var value = selectQuery.resultList.first().value
        var newValue = (value.get() + CounterPersistence.DEFAULT_CACHE_STEP)

        updateCri.set("value", newValue).where(cb.equal(updateRoot.get<Expression<*>>("id"), id))
        val updateQuery = entityManager.createQuery(updateCri)
        updateQuery.executeUpdate()
        return value.get()
    }

    override fun currentCounter(id: String): CounterImpl {
        var counter: CounterImpl?
        var cacheValue: Cache.ValueWrapper? = cache[id]
        // check cache
        if (cacheValue == null) {
            // if not exits in cache - select from db
            counter = entityManager.find(CounterImpl::class.java, id, LockModeType.PESSIMISTIC_WRITE)
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

    override fun doIncrement(id: String, step: Int): Long {
        var counter: CounterImpl = currentCounter(id)
        if (counter.value.get() + step >= counter.maxValue) {
//            counter.maxValue = counter.value + step + CounterPersistence.DEFAULT_CACHE_STEP
            counter.value.set(updateCounter(id, step))
            counter.maxValue = counter.value.get() + CounterPersistence.DEFAULT_CACHE_STEP
//            counter.value -= step
            counter.value.getAndAdd(-step.toLong())
        }
        counter.value.getAndAdd(step.toLong())
        cache.put(id, counter)
        return counter.value.get()
    }

    override fun insert(id: String, initValue: Long): CounterImpl {
        val counter = CounterImpl(id, AtomicLong(initValue))
        val currVal = counter.value.get()
        counter.value.set(counter.maxValue)
        entityManager.persist(counter)
        entityManager.flush()
        entityManager.detach(counter)
        counter.value.set(currVal)
        counter.maxValue = counter.value.get() + CounterPersistence.DEFAULT_CACHE_STEP
        return counter
    }

    override fun increment(id: String, step: Int): Long {
        var rs: Long
        synchronized(lock) {
            rs = doIncrement(id, step)
        }
        return rs
    }

    override fun currentValue(id: String): Long {
        var rs: Long
        synchronized(lock) {
            rs = currentCounter(id).value.get()
        }
        return rs
    }
}