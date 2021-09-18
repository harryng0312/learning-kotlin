package org.harryng.kotlin.demo.persistence

import org.harryng.kotlin.demo.entity.Entity
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.findByIdOrNull
import java.io.Serializable
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaDelete
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Root

abstract class AbstractPersistence<T : Entity<Id>, Id : Serializable>(override val entityClass: Class<T>) :
    BasePersistence<T, Id> {

    @PersistenceContext(name = "primary")
    private lateinit var defaultEntityManager: EntityManager
    protected val entityManager: EntityManager get() = defaultEntityManager

    protected val jpaRepository: SimpleJpaRepository<T, Id> by lazy { SimpleJpaRepository(entityClass, defaultEntityManager) }

    override fun selectById(id: Id): T? {
        return jpaRepository.findByIdOrNull(id)
    }

    override fun insert(obj: T): Int {
        jpaRepository.save(obj)
        return 1
    }

    override fun update(obj: T): Int {
        jpaRepository.save(obj)
        return 1
    }

    override fun delete(id: Id): Int {
        val cb: CriteriaBuilder = entityManager.criteriaBuilder
        val criteriaDelete: CriteriaDelete<T> = cb.createCriteriaDelete(entityClass)
        val root: Root<T> = criteriaDelete.from(entityClass)
        criteriaDelete.where(cb.equal(root.get<Expression<*>>("id"), id))
        val query: Query = entityManager.createQuery(criteriaDelete)
        val rs: Int = query.executeUpdate()
//        jpaRepository.deleteById(id)
        return rs
    }
}