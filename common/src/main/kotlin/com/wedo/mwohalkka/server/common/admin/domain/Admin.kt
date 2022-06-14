package com.wedo.mwohalkka.server.common.admin.domain

import com.wedo.mwohalkka.server.common.support.jpa.BaseEntity
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.*

/**
 * 어드민
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
class Admin(
    /** 로그인 ID **/
    @Column(length = 50, nullable = false)
    var loginId: String,

    /** 이름 **/
    @Column(length = 50, nullable = false)
    var name: String,

    /** 로그인 PW **/
    @Column(length = 100, nullable = false)
    var password: String,

    /** 휴대폰 번호 **/
    @Column(length = 30, nullable = false)
    var phone: String,

    /** 슬랙 아이디 **/
    @Column(length = 30, nullable = false)
    var slackId: String,

    /** 최근 업데이트 시간 **/
    @LastModifiedDate
    var lastUpdated: Instant = Instant.now(),

    /** 권한 */
    @ElementCollection(targetClass = AdminRole::class)
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    var roles: List<AdminRole> = listOf(AdminRole.ADMIN),

    @Column(length = 80)
    var otpSecretKey: String? = null,

    @Transient
    var passwordHolder: String = ""
) : BaseEntity()
