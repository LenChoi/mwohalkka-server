package com.wedo.mwohalkka.server.admin.security

import org.springframework.security.core.AuthenticationException

class SecurityException(msg: String? = null) : AuthenticationException(msg)
