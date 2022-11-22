package com.carerra.goldminer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GoldminerApplication

fun main(args: Array<String>) {
    runApplication<GoldminerApplication>(*args)
}
