package ru.phestrix.places

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import ru.phestrix.places.web.ApiKeysConfig

@SpringBootApplication
@EnableConfigurationProperties(ApiKeysConfig::class)
class PlacesApplication

fun main(args: Array<String>) {
	runApplication<PlacesApplication>(*args)
}
