package org.harryng.kotlin.demo.service

import org.harryng.kotlin.demo.entity.CounterImpl
import org.harryng.kotlin.demo.kernel.CounterPersistence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import kotlin.jvm.Throws

class CounterServiceImpl : CounterService {

    @Autowired
//    @Qualifier("counterPersistence")
    @Qualifier("counterLockerPersistence")
    lateinit var persistence: CounterPersistence

    @Throws(RuntimeException::class)
    override fun insert(id: String, initValue: Long) : CounterImpl{
        return persistence.insert(id)
    }

    @Throws(RuntimeException::class)
    override fun increment(id:String, step: Int) : Long {
        return persistence.increment(id, step)
    }

    @Throws(RuntimeException::class)
    override fun currentValue(id:String): Long {
        return persistence.currentValue(id)
    }
}