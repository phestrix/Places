package ru.phestrix.places.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import ru.phestrix.places.entity.Weather

@Service
class WeatherService(private val webClient: WebClient) {
    suspend fun getWeather(lon: Double, lat: Double): Weather {

    }
}