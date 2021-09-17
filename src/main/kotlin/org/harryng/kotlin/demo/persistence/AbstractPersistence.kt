package org.harryng.kotlin.demo.persistence

import org.harryng.kotlin.demo.entity.Entity
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
//    @Autowired
//    @Qualifier("entityManagerFactory")
    @PersistenceContext(name = "primary")
    private lateinit var defaultEntityManager: EntityManager
    override val entityManager: EntityManager
        get() = defaultEntityManager

    override fun selectById(id: Id): T? {
        return entityManager.find(entityClass, id)
    }

    override fun insert(obj: T): Int {
        entityManager.persist(obj)
        return 1
    }

    override fun update(obj: T): Int {
        entityManager.merge(obj)
        return 1
    }

    override fun delete(id: Id): Int {
        val cb: CriteriaBuilder = entityManager.criteriaBuilder
        val criteriaDelete: CriteriaDelete<T> = cb.createCriteriaDelete(entityClass)
        val root: Root<T> = criteriaDelete.from(entityClass)
        criteriaDelete.where(cb.equal(root.get<Expression<*>>("id"), id))
        val query: Query = entityManager.createQuery(criteriaDelete)
        val rs: Int = query.executeUpdate()
        return rs
    }
}