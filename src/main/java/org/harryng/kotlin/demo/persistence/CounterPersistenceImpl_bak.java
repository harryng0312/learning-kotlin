package org.harryng.kotlin.demo.persistence;

import org.harryng.kotlin.demo.entity.CounterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

class CounterPersistenceImpl_bak implements CounterPersistence_bak {

    //    companion object {

    private volatile Object lock = new Object();
//    }

    @PersistenceContext(name = "primary")
    private EntityManager defaultEntityManager;

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    private Cache getCache() {
        return cacheManager.getCache("counter");
    }

    @Override
    public EntityManager getEntityManager() {
        return defaultEntityManager;
    }

    private void updateCounter(String id, Long value) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaUpdate<CounterImpl> cd = cb.createCriteriaUpdate(CounterImpl.class);
        Root<CounterImpl> root = cd.from(CounterImpl.class);
        cd.set("value", value).where(cb.equal(root.get("id"), id));
        Query updateQuery = getEntityManager().createQuery(cd);
        updateQuery.executeUpdate();
    }

    //    @Throws(NullPointerException::class)
    protected CounterImpl currentCounter(String id) throws NullPointerException {
        CounterImpl counter = null;
        Cache.ValueWrapper cacheValue = getCache().get(id);
        // check cache
        if (cacheValue == null) {
            // if not exits in cache - select from db
            counter = getEntityManager().find(CounterImpl.class, id, LockModeType.NONE);
            // if not exits in db - create into db and cache
            if (counter == null) {
                counter = insert(id, CounterPersistence_bak.DEFAULT_INIT_VALUE);
            }
            getCache().put(id, counter);
            cacheValue = getCache().get(id);
        }
        counter = (CounterImpl) cacheValue.get();
        return counter;
    }

    @Override
    public CounterImpl insert(String id, Long initValue) throws RuntimeException {
        CounterImpl counter = new CounterImpl(id, initValue);
        Long currVal = counter.getValue();
        counter.setValue(counter.getMaxValue());
        getEntityManager().persist(counter);
        getEntityManager().flush();
        getEntityManager().detach(counter);
        counter.setValue(currVal);
        counter.setMaxValue(counter.getValue() + CounterPersistence_bak.DEFAULT_CACHE_STEP);
        return counter;
    }

    @Override
    public Long increment(String id, int step) throws RuntimeException {
        CounterImpl counter = null;
        synchronized (lock) {
            counter = currentCounter(id);
            if (counter.getValue() + step >= counter.getMaxValue()) {
                counter.setMaxValue(counter.getValue() + step + CounterPersistence_bak.DEFAULT_CACHE_STEP);
                updateCounter(id, counter.getMaxValue());
            }
            counter.setMaxValue(counter.getValue() + step);
            getCache().put(id, counter);
        }
        return counter.getValue();
    }

    //    @Synchronized
    @Override
    public Long currentValue(String id) throws RuntimeException {
        Long rs = 0L;
        synchronized (lock) {
            rs = currentCounter(id).getValue();
        }
        return rs;
    }
}