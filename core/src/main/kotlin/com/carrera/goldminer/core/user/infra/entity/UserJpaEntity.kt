package com.carrera.goldminer.core.user.infra.entity

import com.carrera.goldminer.core.common.infra.JpaEntity
import com.carrera.goldminer.core.user.domain.entity.User
import com.carrera.goldminer.core.user.domain.value.UserType
import javax.persistence.*

@Entity(name = "users")
class UserJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var userName: String,
    var password: String = "",
    @Enumerated(EnumType.STRING)
    var userType: UserType,
) : JpaEntity<User> {
    constructor(user: User) : this(
        user.id,
        user.userName,
        user.password,
        user.type
    )

    override fun update(domainEntity: User) {
        userName = domainEntity.userName
        password = domainEntity.password
    }

    override fun toDomainEntity(): User {
        return User(
            id,
            userName,
            password,
            userType
        )
    }
}
