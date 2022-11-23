package com.carrera.goldminer.core.common.infra

interface JpaEntity<T> {
    fun update(domainEntity: T)
    fun toDomainEntity(): T
}
