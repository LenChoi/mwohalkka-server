package com.wedo.mwohalkka.core.repository

import com.querydsl.core.types.EntityPath
import com.querydsl.jpa.JPQLQuery

/**
 * Grid 검색조건 및 Paging 컨텍스트
 * @param <E>
 */
interface SearchContext<E> {
    fun applySearchCriteria(q: JPQLQuery<E>)
    fun applyFieldCriteria(q: JPQLQuery<E>)
    fun applyPagingCriteria(q: JPQLQuery<E>)
    fun applyOrder(q: JPQLQuery<E>)
    fun getEntityPathBase(): EntityPath<E>
    fun setTotalPages(totalRows: Int)
    fun setRecords(records: Int)
    fun getTotalPages(): Int
    fun getPage(): Int
    fun getRowsPerPage(): Int
}
