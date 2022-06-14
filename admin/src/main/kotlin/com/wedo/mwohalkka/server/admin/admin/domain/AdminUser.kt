package com.wedo.mwohalkka.server.admin.admin.domain

import com.wedo.mwohalkka.server.common.admin.domain.Admin
import org.springframework.security.core.userdetails.User
import java.io.Serializable

/**
 * 시큐리티 loadByUser 응답 클래스
 */
class AdminUser : User, Serializable {
    var id: Long? = null
    var loginId: String? = null
    var otpSecretKey: String? = null

    constructor(admin: Admin) : super(admin.loginId, admin.password, admin.roles) {
        id = admin.id
        loginId = admin.loginId
        otpSecretKey = admin.otpSecretKey
    }
}
