package com.wedo.mwohalkka.core.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable

/**
 * Base repository
 *
 * @param <E>  Entity
 * @param <ID> ID
 */
@NoRepositoryBean
interface BaseRepository<E, ID : Serializable?> : JpaRepository<E, ID> {
    /**
     * entity를 detach(session에서 제거)
     *
     * @param entity
     */
    fun detach(entity: E)

    /**
     * Session cache를 모두 제거해준다. 모든 엔티티에 대해서 detach
     */
    fun clear()

    /**
     * SearchContext를 기준으로 동적 페이징 쿼리 실행후 결과를 리턴.
     * 클라이언트에 리턴할 페이징 관련 정보는 ctx에 남김.
     *
     * @param ctx
     * @return
     */
    fun search(ctx: SearchContext<E>): List<E>
}
