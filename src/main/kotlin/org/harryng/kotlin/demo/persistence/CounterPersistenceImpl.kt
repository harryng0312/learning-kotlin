package org.harryng.kotlin.demo.persistence

import org.harryng.kotlin.demo.entity.CounterImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.concurrent.ConcurrentMapCache
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Expression

open class CounterPersistenceImpl : CounterPersistence {

    companion object {
        //        @Volatile
        var lock = Object()
    }

    @PersistenceContext(name = "primary")
    private lateinit var defaultEntityManager: EntityManager

    @Autowired
    @Qualifier("cacheManager")
    private lateinit var cacheManager: CacheManager
    private val cache get() = cacheManager.getCache("counter")


    override val entityManager: EntityManager
        get() = defaultEntityManager

    private fun updateCounter(id: String, value: Long) {
        val cb = entityManager.criteriaBuilder
        val cd = cb.createCriteriaUpdate(CounterImpl::class.java)
        val root = cd.from(CounterImpl::class.java)
        cd.set("value", value).where(cb.equal(root.get<Expression<*>>("id"), id))
        val updateQuery = entityManager.createQuery(cd)
        updateQuery.executeUpdate()
    }

    @Throws(NullPointerException::class)
    protected fun currentCounter(id: String): CounterImpl {
        var counter: CounterImpl
        var cacheValue: Cache.ValueWrapper? = cache[id]
        // check cache
        if (cacheValue == null) {
            // if not exits in cache - select from db
            counter = entityManager.find(CounterImpl::class.java, id)
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
        val counter = CounterImpl(id, initValue)
        val currVal = counter.value
        counter.value = counter.maxValue
        entityManager.persist(counter)
        entityManager.flush()
        entityManager.detach(counter)
        counter.value = currVal
        counter.maxValue = counter.value + CounterPersistence.DEFAULT_CACHE_STEP
        return counter
    }

    override fun increment(id: String, step: Int): Long {
        lateinit var counter: CounterImpl
        synchronized(lock) {
            counter = currentCounter(id)
            if(counter.value + step >= counter.maxValue){
                counter.maxValue = counter.value + step + CounterPersistence.DEFAULT_CACHE_STEP
                updateCounter(id, counter.maxValue)
            }
            counter.value += step
            cache.put(id, counter)
        }
        return counter.value
    }

    override fun currentValue(id: String): Long {
        synchronized(lock) {
            return currentCounter(id).value
        }
    }
}