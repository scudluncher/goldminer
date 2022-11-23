package com.carerra.goldminer.common

class BadRequestException : RuntimeException {
    constructor()
    constructor(message: String) : super(message)
    constructor(e: Throwable) : super(e)
}
