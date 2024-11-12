package ru.phestrix.places.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import ru.phestrix.places.entity.Location

@Service
class LocationService(private val webClient: WebClient) {

    suspend fun getLocations(query: String, apiKey: String): List<Location> {
        val uri = "https://graphhopper.com/api/1/geocode?q=$query&key=$apiKey"
        return withContext(Dispatchers.IO){
            val response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String::class.java)
                .block()
            parseLocations(response.toString())
        }
    }

    private fun parseLocations(response: String): List<Location> {
        //TODO: parse json to list of locs
        return listOf()
    }
}