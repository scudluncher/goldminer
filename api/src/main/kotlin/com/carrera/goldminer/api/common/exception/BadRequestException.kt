package com.carrera.goldminer.api.common.exception

class BadRequestException : RuntimeException {
    constructor()
    constructor(message: String) : super(message)
    constructor(e: Throwable) : super(e)
}
