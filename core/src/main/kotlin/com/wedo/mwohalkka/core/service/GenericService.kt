package com.wedo.mwohalkka.core.service

import com.wedo.mwohalkka.core.repository.BaseRepository
import com.wedo.mwohalkka.core.repository.SearchContext
import java.io.Serializable

/**
 * 공통 표준 서비스
 *
 * @param <E> Entity
 * @param <ID> Identifier
 * @param <R> Repository
 */
interface GenericService<E, ID : Serializable, R : BaseRepository<E, ID>> {
    fun add(entity: E): E

    fun update(entity: E): E

    fun deleteById(id: ID)

    fun delete(entity: E)

    fun get(id: ID): E

    fun find(id: ID): E

    fun search(sc: SearchContext<E>): List<E> {
        throw UnsupportedOperationException()
    }
}
