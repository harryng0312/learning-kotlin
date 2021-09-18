package org.harryng.kotlin.demo.persistence

import org.harryng.kotlin.demo.entity.UserImpl
import javax.persistence.TypedQuery

class UserPersistenceImpl : AbstractPersistence<UserImpl, Long>(UserImpl::class.java), UserPersistence {
    override fun selectByUsername(username: String): UserImpl {
        val jpql= ""
        val query: TypedQuery<UserImpl> = entityManager.createQuery(jpql, entityClass)
        return query.resultList.first()
    }
}