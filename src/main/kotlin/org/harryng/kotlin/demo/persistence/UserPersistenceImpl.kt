package org.harryng.kotlin.demo.persistence

import org.harryng.kotlin.demo.entity.UserImpl
import javax.persistence.TypedQuery

class UserPersistenceImpl : AbstractPersistence<UserImpl, Long>(UserImpl::class.java), UserPersistence {
    override fun selectByUsername(username: String): UserImpl? {
        val jpql = "select u from ${UserImpl::class.qualifiedName} u where u.username=:username"
        val query: TypedQuery<UserImpl> = entityManager.createQuery(jpql, entityClass)
        query.setParameter("username", username)
        return query.resultList.first()
    }
}