package com.wedo.mwohalkka.core.grid

import com.querydsl.core.types.EntityPath
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.JPQLQuery
import com.wedo.mwohalkka.core.repository.SearchContext

open class GridReq<E> : SearchContext<E> {
    internal var page = 0
    internal var rows = 0
    internal var total = 0
    internal var records = 0

    var path: EntityPath<E>? = null
    var order: OrderSpecifier<*>? = null

    constructor(path: EntityPath<E>) {
        this.path = path
    }

    constructor(path: EntityPath<E>, order: OrderSpecifier<*>) {
        this.path = path
        this.order = order
    }

    override fun getEntityPathBase(): EntityPath<E> {
        return path!!
    }

    override fun applySearchCriteria(q: JPQLQuery<E>) {
        applyFieldCriteria(q)
        applyPagingCriteria(q)
    }

    override fun applyOrder(q: JPQLQuery<E>) {
        org.springframework.util.Assert.notNull(this.order)
        q.orderBy(*arrayOf<OrderSpecifier<*>?>(this.order))
    }

    override fun applyFieldCriteria(q: JPQLQuery<E>) {}
    override fun applyPagingCriteria(q: JPQLQuery<E>) {
        if (rows > 0) {
            q.limit(rows.toLong())
            q.offset(((page - 1) * rows).toLong())
        }
    }

    override fun setTotalPages(totalPages: Int) {
        total = totalPages
    }

    override fun getTotalPages(): Int {
        return total
    }

    override fun getRowsPerPage(): Int {
        return rows
    }

    override fun getPage(): Int {
        return page
    }

    open fun setPage(page: Int) {
        this.page = page
    }

    open fun getRows(): Int {
        return rows
    }

    open fun setRows(rows: Int) {
        this.rows = rows
    }

    open fun getRecords(): Int {
        return records
    }

    override fun setRecords(records: Int) {
        this.records = records
    }
}
