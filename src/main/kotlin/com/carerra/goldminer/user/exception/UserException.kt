package com.carerra.goldminer.user.exception

open class UserException(private val type: UserExceptionType) : RuntimeException()

class UserNotFoundException : UserException(UserExceptionType.USER_NOT_FOUND)

class WrongPasswordException : UserException(UserExceptionType.WRONG_PASSWORD)

enum class UserExceptionType {
    USER_NOT_FOUND,
    WRONG_PASSWORD
}
