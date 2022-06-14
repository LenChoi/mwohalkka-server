package com.wedo.mwohalkka.server.common.user.domain

import com.wedo.mwohalkka.server.common.support.jpa.BaseAggregateRoot
import java.time.LocalDate
import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

/**
 * 유저
 */
@Entity
class User(
    /** 이름 **/
    var name: String,

    /** 이메일 **/
    var email: String,

    /** 연락처 **/
    var phone: String,

    /** 셍년월일 **/
    val birthDate: LocalDate,

    /** 상태 **/
    @Enumerated(EnumType.STRING)
    val status: UserStatus = UserStatus.CREATED,

    /** 생성 일시 **/
    val createdAt: OffsetDateTime = OffsetDateTime.now()
) : BaseAggregateRoot<User>()
