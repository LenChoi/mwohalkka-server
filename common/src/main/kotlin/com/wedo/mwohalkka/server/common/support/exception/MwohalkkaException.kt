package com.wedo.mwohalkka.server.common.support.exception

class MwohalkkaException(val error: MwohalkkaError) : RuntimeException()

enum class MwohalkkaError(
    val code: Int,
    val messageKey: String? = null
)
