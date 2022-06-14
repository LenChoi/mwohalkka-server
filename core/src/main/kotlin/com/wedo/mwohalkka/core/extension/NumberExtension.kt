package com.wedo.mwohalkka.core.extension

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

private val amountFormatter = DecimalFormat("###,###.##################")

val BigDecimal?.amountFormat: String
    get() = this?.run { amountFormatter.format(this) } ?: ""

fun BigDecimal?.setScaleCoinAmount(outputDecimal: Int): String {
    return this?.setScale(outputDecimal, RoundingMode.DOWN).amountFormat
}

fun BigDecimal.notSame(target: BigDecimal): Boolean {
    return !this.same(target)
}

fun BigDecimal.same(target: BigDecimal): Boolean {
    return this.compareTo(target) == 0
}
