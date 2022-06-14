package com.wedo.mwohalkka.server.common.support.event

import com.wedo.mwohalkka.server.common.support.exception.ExceptionUtils
import javax.servlet.http.HttpServletRequest

/**
 * 통보 가능 애플리케이션 이벤트
 * - notification 서비스 적용대상
 */
interface NotifiableEvent {
    fun notificationMessage(): String
}

open class AppEvent(val message: String) : NotifiableEvent {
    override fun notificationMessage(): String {
        return message
    }
}

open class SlackEvent(
    val message: String,
    val mention: String = "",
    val channel: String? = null
) : NotifiableEvent {
    override fun notificationMessage(): String {
        return "$mention $message"
    }
}

open class UnhandledExceptionEvent(e: Exception, req: HttpServletRequest? = null, channel: String?) : SlackEvent(
    stackTrace(e, req), "<!channel>", channel)

fun stackTrace(e: Exception, req: HttpServletRequest?): String {
    val sw = ExceptionUtils.simplifyMessage(e)

    return "Error: ${req?.requestURI} *${e.message}*\n$sw"
}
