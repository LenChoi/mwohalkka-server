package com.wedo.mwohalkka.core.repository

import com.querydsl.core.types.EntityPath
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.core.types.dsl.PathBuilderFactory
import com.querydsl.jpa.JPQLQuery
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.Querydsl
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import java.io.Serializable
import java.util.*
import javax.persistence.EntityManager
import kotlin.math.ceil

/**
 * 추가 기능을 제공하는 JpaRepository Base 구현
 * @param <E>
 * @param <ID>
 */
class BaseRepositoryImpl<E, ID : Serializable> : SimpleJpaRepository<E, ID>, BaseRepository<E, ID> {
    var entityManager: EntityManager
    var querydsl: Querydsl
    var pathBuilder: PathBuilder<*>

    constructor(jpaEntityInformation: JpaEntityInformation<E, ID>, entityManager: EntityManager) : super(
        jpaEntityInformation,
        entityManager
    ) {
        this.entityManager = entityManager
        this.pathBuilder = PathBuilderFactory().create(jpaEntityInformation.javaType)
        this.querydsl = Querydsl(this.entityManager, pathBuilder)
    }

    override fun detach(entity: E) {
        entityManager.detach(entity)
    }

    override fun clear() {
        entityManager.clear()
    }

    override fun search(sc: SearchContext<E>): List<E> {
        sc.getEntityPathBase().let {
            calcCount(sc, it)
            return getResults(sc, it)
        }
        return Collections.emptyList()
    }

    fun getResults(sc: SearchContext<E>, path: EntityPath<E>): List<E> {
        @Suppress("UNCHECKED_CAST")
        val q = querydsl.createQuery(path) as JPQLQuery<E>
        sc.applySearchCriteria(q)
        sc.applyOrder(q)
        return q.select(path).fetch()
    }

    fun calcCount(sc: SearchContext<E>, path: EntityPath<E>) {
        @Suppress("UNCHECKED_CAST")
        val q = querydsl.createQuery(path) as JPQLQuery<E>
        sc.applyFieldCriteria(q)
        val records = q.fetchCount()
        sc.setRecords(records.toInt())
        sc.setTotalPages(ceil(records.toDouble() / sc.getRowsPerPage().toDouble()).toInt())
    }
}
