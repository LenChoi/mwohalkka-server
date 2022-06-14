package com.wedo.mwohalkka.server.common.support.exception

import java.io.PrintWriter
import java.io.StringWriter

class ExceptionUtils {
    companion object {
        fun simplifyMessage(throwable: Throwable): String {
            val sw = StringWriter()
            throwable.printStackTrace(PrintWriter(sw))

            return truncate(sw.toString())
        }

        fun truncate(messages: String): String {
            return messages.split("\n")
                .filter { !(it.contains(START_WITH) && it.containsAny(BLOCKED_PACKAGES_START_WITH)) }
                .joinToString("\n")
        }

        private const val START_WITH = "at "
        private val BLOCKED_PACKAGES_START_WITH =
            listOf(
                "org.spring", "java.base", "org.hibernate",
                "org.apache", "com.sun", "javax.servlet",
                "com.fasterxml.jackson", "jdk.internal",
                "io.netty", "reactor.core", "reactor.netty"
            )
    }
}

fun String.containsAny(keywords: List<String>): Boolean {
    for (keyword in keywords) {
        if (this.contains(keyword)) return true
    }
    return false
}
