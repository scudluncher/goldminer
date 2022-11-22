package com.carerra.goldminer.common

class BadRequestException : RuntimeException {
    constructor()
    constructor(e: Throwable) : super(e)
}
