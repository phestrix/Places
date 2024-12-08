package ru.phestrix.places.web

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.phestrix.places.entity.Place
import ru.phestrix.places.entity.PlaceDetailsResponse
import ru.phestrix.places.web.config.ApiKeyConfig

@Component
class PlaceDescriptionApiClient(
    @Qualifier("openTripMapWebClient") private val webClient: WebClient,
    private val apiKeyConfig: ApiKeyConfig
) {
    fun getPlaceDetails(xid: String): Mono<PlaceDetailsResponse> {
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/xid/${xid}")
                    .queryParam("apikey", apiKeyConfig.opentripmap)
                    .build()
            }
            .retrieve()
            .bodyToMono(PlaceDetailsResponse::class.java)
    }

    /*fun getLimitedPlacesDetails(places: List<Place>): Mono<List<Pair<Place, PlaceDetailsResponse>>> {
        val limitedPlaces = places.take(10)
        val detailsMonos = limitedPlaces.map { feature ->
            getPlaceDetails(feature.properties.xid).onErrorResume { Mono.empty() }.map { details -> feature to details }
        }
        val mono = Mono.zip(detailsMonos) { results ->
            @Suppress("UNCHECKED_CAST")
            results.map { it as Pair<Place, PlaceDetailsResponse> }
        }
        return mono
    }*/

    fun getLimitedPlacesDetails(places: List<Place>): Mono<List<Pair<Place, PlaceDetailsResponse>>> {
        println("Received places for details fetching: $places")
        val limitedPlaces = places.take(5)
        println("Limited places to fetch details for: $limitedPlaces")

        val detailsMonos = limitedPlaces.map { feature ->
            println("Fetching details for place: ${feature.properties.xid}")
            getPlaceDetails(feature.properties.xid)
                .onErrorResume { error ->
                    println("Error fetching details for ${feature.properties.xid}: ${error.message}")
                    Mono.empty()
                }
                .map { details ->
                    println("Fetched details for ${feature.properties.xid}: $details")
                    feature to details
                }
        }

        return Mono.zip(detailsMonos) { results ->
            println("Zipped results: $results")
            @Suppress("UNCHECKED_CAST")
            results.map { it as Pair<Place, PlaceDetailsResponse> }
        }.doOnError { error ->
            println("Error in getLimitedPlacesDetails: ${error.message}")
        }
    }
}