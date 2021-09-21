package org.harryng.kotlin.demo.persistence;

import org.harryng.kotlin.demo.entity.CounterImpl;
import javax.persistence.EntityManager;

public interface CounterPersistence_bak {
    EntityManager getEntityManager();
    int DEFAULT_STEP = 1;
    long DEFAULT_INIT_VALUE = 1L;
    int DEFAULT_CACHE_STEP = 20;

    CounterImpl insert(String id, Long initValue) throws RuntimeException;
    Long increment(String id, int step) throws RuntimeException;
    Long currentValue(String id) throws RuntimeException;
}