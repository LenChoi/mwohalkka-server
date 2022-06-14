package com.wedo.mwohalkka.core.service

import com.wedo.mwohalkka.core.repository.BaseRepository
import com.wedo.mwohalkka.core.repository.SearchContext
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable

/**
 * Generic Service 구현
 *
 * @param <E> Entity
 * @param <ID> Identifier
 * @param <R> Repository
 */
@Transactional
class GenericServiceImpl<E, ID : Serializable, R : BaseRepository<E, ID>> : GenericService<E, ID, R> {
    @Autowired
    lateinit var mapper: ModelMapper

    @Autowired
    lateinit var repository: R

    @Autowired
    lateinit var messageSource: MessageSource

    override fun add(entity: E): E {
        return repository.save(entity)
    }

    override fun update(entity: E): E {
        return repository.save(entity)
    }

    override fun deleteById(id: ID) {
        repository.deleteById(id)
    }

    override fun delete(entity: E) {
        repository.delete(entity)
    }

    override fun get(id: ID): E {
        return repository.getOne(id)
    }

    override fun find(id: ID): E {
        return repository.findById(id).orElse(null)
    }

    override fun search(sc: SearchContext<E>): List<E> {
        return repository.search(sc)
    }
}
