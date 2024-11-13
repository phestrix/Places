package ru.phestrix.places.web

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.phestrix.places.entity.WeatherResponse

@Component
class WeatherWebClient(private val webClient: WebClient, private val apiKey: String) {
    fun getWeatherByCoordinates(coordinates: Coordinates): Mono<WeatherResponse>{
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/weather")
                    .queryParam("lat", coordinates.latitude)
                    .queryParam("lon", coordinates.longitude)
                    .queryParam("appid", apiKey)
                    .build()
            }
            .retrieve()
            .bodyToMono(WeatherResponse::class.java)
    }
}