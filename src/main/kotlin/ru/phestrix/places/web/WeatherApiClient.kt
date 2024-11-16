package ru.phestrix.places.web

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.phestrix.places.entity.WeatherResponse
import ru.phestrix.places.util.Coordinates
import ru.phestrix.places.web.config.ApiKeyConfig

@Component
class WeatherApiClient(@Qualifier("weatherWebClient") private val webClient: WebClient, private val apiKeyConfig: ApiKeyConfig) {
    fun getWeatherByCoordinates(coordinates: Coordinates): Mono<WeatherResponse>{
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/weather")
                    .queryParam("lat", coordinates.latitude)
                    .queryParam("lon", coordinates.longitude)
                    .queryParam("appid", apiKeyConfig.openweathermap)
                    .build()
            }
            .retrieve()
            .bodyToMono(WeatherResponse::class.java)
    }
}