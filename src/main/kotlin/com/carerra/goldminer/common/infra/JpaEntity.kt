package com.carerra.goldminer.common.infra

interface JpaEntity<T> {
    fun update(domainEntity: T)
    fun toDomainEntity(): T
}
