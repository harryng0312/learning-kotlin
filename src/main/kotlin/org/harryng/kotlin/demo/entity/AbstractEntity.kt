package org.harryng.kotlin.demo.entity

import java.io.Serializable

abstract class AbstractEntity<Id : Serializable> : Entity<Id> {
    override var id: Id
        get() = id
        set(id) {
            this.id = id
        }
}