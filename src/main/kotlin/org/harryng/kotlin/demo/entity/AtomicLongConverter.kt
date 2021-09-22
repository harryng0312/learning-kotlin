package org.harryng.kotlin.demo.entity

import java.util.concurrent.atomic.AtomicLong
import javax.persistence.AttributeConverter

class AtomicLongConverter: AttributeConverter<AtomicLong, Long> {
    override fun convertToDatabaseColumn(attribute: AtomicLong?): Long {
        if (attribute!=null){
            return attribute.get()
        }
        return 0L
    }

    override fun convertToEntityAttribute(dbData: Long?): AtomicLong {
        if(dbData!=null){
            return AtomicLong(dbData)
        }
        return AtomicLong(0L)
    }
}