package com.wedo.mwohalkka.core.grid

import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.*
import com.querydsl.jpa.JPQLQuery
import org.hibernate.criterion.MatchMode
import org.springframework.util.StringUtils
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime

class ExpressionUtils {
    companion object {
        fun like(query: JPQLQuery<*>, path: StringPath, value: String?, mode: MatchMode) {
            if (StringUtils.hasText(value)) {
                query.where(*arrayOf<Predicate>(path.like(mode.toMatchString(value))))
            }
        }

        fun notLike(
            query: JPQLQuery<*>,
            path: StringPath,
            value: kotlin.String?,
            mode: org.hibernate.criterion.MatchMode
        ) {
            if (org.springframework.util.StringUtils.hasText(value)) {
                query.where(*arrayOf<Predicate>(path.notLike(mode.toMatchString(value))))
            }
        }

        fun <T> eq(query: JPQLQuery<*>, path: SimpleExpression<T>, value: T?) {
            if (value != null) query.where(path.eq(value))
        }

        fun eq(query: JPQLQuery<*>, path: SimpleExpression<String>, value: String?) {
            if (StringUtils.hasText(value)) query.where(path.eq(value))
        }

        fun <T> neq(query: JPQLQuery<*>, path: SimpleExpression<T>, value: T?) {
            if (value != null) {
                query.where(*arrayOf<Predicate?>(path.ne(value)))
            }
        }

        fun <T> eqId(query: JPQLQuery<*>, path: SimpleExpression<Long>, id: Long) {
            if (id > 0) query.where(path.eq(id))
        }

        fun eq(query: JPQLQuery<*>, path: StringPath, value: String) {
            if (StringUtils.hasText(value)) {
                query.where(*arrayOf<Predicate>(path.eq(value)))
            }
        }

        fun <T : Enum<T>?> eq(q: JPQLQuery<*>, path: EnumPath<T>?, value: String?, cl: Class<T>?) {
            if (StringUtils.hasText(value)) {
                eq(q, path as StringPath, java.lang.Enum.valueOf(cl, value) as String)
            }
        }

        fun <T> isNull(query: JPQLQuery<*>, path: SimpleExpression<T>) {
            query.where(*arrayOf<Predicate>(path.isNull))
        }

        fun <T> isNotNull(query: JPQLQuery<*>, path: SimpleExpression<T>) {
            query.where(*arrayOf<Predicate>(path.isNotNull))
        }

        fun <T> before(query: JPQLQuery<*>, path: DateTimePath<LocalDateTime>, before: LocalDateTime) {
            if (before != null) {
                query.where(*arrayOf(path.before(before)))
            }
        }

        fun <T> after(query: JPQLQuery<*>, path: DatePath<LocalDateTime>, after: LocalDateTime) {
            if (after != null) {
                query.where(*arrayOf<com.querydsl.core.types.Predicate>(path.after(after)))
            }
        }

        fun <T> before(query: JPQLQuery<*>, path: DatePath<LocalDate>, before: LocalDate?) {
            if (before != null) {
                query.where(*arrayOf<com.querydsl.core.types.Predicate>(path.before(before)))
            }
        }

        fun beforeOrEqual(query: JPQLQuery<*>, path: DatePath<LocalDate>, before: LocalDate?) {
            if (before != null) {
                query.where(*arrayOf(path.before(before).or(path.eq(before))))
            }
        }

        fun <T> after(query: JPQLQuery<*>, path: DatePath<LocalDate>, after: LocalDate?) {
            if (after != null) {
                query.where(*arrayOf<com.querydsl.core.types.Predicate>(path.after(after)))
            }
        }

        fun afterOrEqual(query: JPQLQuery<*>, path: DatePath<LocalDate>, after: LocalDate?) {
            if (after != null) {
                query.where(*arrayOf(path.after(after).or(path.eq(after))))
            }
        }

        fun afterOrEqual(query: JPQLQuery<*>, path: DateTimePath<OffsetDateTime>, after: OffsetDateTime?) {
            if (after != null) {
                query.where(*arrayOf(path.after(after).or(path.eq(after))))
            }
        }

        fun beforeOrEqual(query: JPQLQuery<*>, path: DateTimePath<OffsetDateTime>, after: OffsetDateTime?) {
            if (after != null) {
                query.where(*arrayOf(path.before(after).or(path.eq(after))))
            }
        }

        fun gt(query: JPQLQuery<*>, path: NumberPath<*>, value: BigInteger?) {
            if (value != null) {
                query.where(*arrayOf<Predicate>(path.gt(value)))
            }
        }

        fun lt(query: JPQLQuery<*>, path: NumberPath<*>, value: BigInteger?) {
            if (value != null) {
                query.where(*arrayOf<Predicate>(path.lt(value)))
            }
        }

        fun goe(query: JPQLQuery<*>, path: NumberPath<*>, value: BigInteger?) {
            if (value != null) {
                query.where(*arrayOf<Predicate>(path.goe(value)))
            }
        }

        fun loe(query: JPQLQuery<*>, path: NumberPath<*>, value: BigInteger?) {
            if (value != null) {
                query.where(*arrayOf<Predicate>(path.loe(value)))
            }
        }

        fun <T : Enum<T>?> equalsIgnoreCase(query: JPQLQuery<*>, path: EnumPath<T>, value: String?) {
            if (StringUtils.hasText(value)) {
                query.where(*arrayOf<Predicate>(path.stringValue().equalsIgnoreCase(value)))
            }
        }
    }
}
