package com.wedo.mwohalkka.core.extension

import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * 2021.10.13 KST 기준으로 세팅
 */
val KSTZoneId: ZoneId = ZoneId.of("+09:00")
const val DateTimeFormatPattern = "YYYY-MM-dd HH:mm:ss"
val DateTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern(DateTimeFormatPattern)
val KST = TimeZone.getTimeZone("Asia/Seoul")!!
val KSTFormatter = DateTimeFormatter.ofPattern(DateTimeFormatPattern).withZone(KST.toZoneId())!!

fun OffsetDateTime.toFormatted() = KSTFormatter.format(this)!!

fun OffsetDateTime.toKST(): OffsetDateTime = this.withOffsetSameInstant(ZoneOffset.ofHours(9))

fun OffsetDateTime.toKSTLocalDate(): LocalDate = this.toKST().toLocalDate()

fun LocalDateTime.toFormatted() = KSTFormatter.format(this.atOffset(ZoneOffset.UTC))!!

fun LocalDate.toKSTOffsetDateTime(time: LocalTime = LocalTime.MIN): OffsetDateTime =
    OffsetDateTime.of(this, time, ZoneOffset.ofHours(9))
