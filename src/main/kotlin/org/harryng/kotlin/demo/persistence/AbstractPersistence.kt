package org.harryng.kotlin.demo.persistence

import org.harryng.kotlin.demo.entity.Entity
import java.io.Serializable
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query
import javax.persistence.criteria.*

abstract class AbstractPersistence<T : Entity<Id>, Id : Serializable>(override val entityClass: Class<T>) :
    BasePersistence<T, Id> {

    @PersistenceContext(name = "primary")
    private lateinit var defaultEntityManager: EntityManager
    protected val entityManager: EntityManager get() = defaultEntityManager

//    protected val jpaRepository: SimpleJpaRepository<T, Id> by lazy { SimpleJpaRepository(entityClass, defaultEntityManager) }

    override fun selectById(id: Id): T? {
        return entityManager.find(entityClass, id)
    }

    override fun insert(obj: T): Int {
        entityManager.persist(obj)
        return 1
    }

    override fun update(obj: T): Int {
        entityManager.merge(obj)
//        val cb: CriteriaBuilder = entityManager.criteriaBuilder
//        val criteriaUpdate: CriteriaUpdate<T> = cb.createCriteriaUpdate(entityClass)
//        val root: Root<T> = criteriaUpdate.from(entityClass)
//        criteriaUpdate.set()
//        criteriaUpdate.where(cb.equal(root.get<Expression<*>>("id"), obj.id))
//        val query: Query = entityManager.createQuery(criteriaUpdate)
//        val rs: Int = query.executeUpdate()
////        jpaRepository.deleteById(id)
//        return rs
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