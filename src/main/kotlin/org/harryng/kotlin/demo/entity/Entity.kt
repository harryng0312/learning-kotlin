package org.harryng.kotlin.demo.entity

import java.io.Serializable

interface Entity<Id: Serializable> {
    var id: Id
}