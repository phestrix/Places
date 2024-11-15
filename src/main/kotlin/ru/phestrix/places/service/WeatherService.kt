package ru.phestrix.places.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import ru.phestrix.places.entity.WeatherResponse
import ru.phestrix.places.util.Coordinates
import ru.phestrix.places.web.WeatherApiClient

@Service
class WeatherService(private val weatherApiClient: WeatherApiClient) {
    fun getWeather(coordinates: Coordinates): Mono<WeatherResponse> {
        return weatherApiClient.getWeatherByCoordinates(coordinates)
    }
}