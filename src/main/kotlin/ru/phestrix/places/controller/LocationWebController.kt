package ru.phestrix.places.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Mono
import ru.phestrix.places.entity.Place
import ru.phestrix.places.entity.PlaceDetailsResponse
import ru.phestrix.places.service.LocationService
import ru.phestrix.places.service.PlaceDetailsService
import ru.phestrix.places.service.PlacesService
import ru.phestrix.places.service.WeatherService
import ru.phestrix.places.util.Coordinates

@Controller
@RequestMapping("/web")
class LocationWebController(
    private val locationService: LocationService,
    private val weatherService: WeatherService,
    private val placesService: PlacesService,
    private val placeDetailsService: PlaceDetailsService
) {

    @GetMapping("/location/info")
    fun getLocationInfo(@RequestParam(required = false) query: String?, model: Model): String{
        if (query.isNullOrBlank()) {
            // Если параметр query отсутствует, просто возвращаем форму
            return "location-info"
        }
        return locationService.searchLocations(query)
            .flatMap{locations ->
                if(locations.isEmpty()){
                    return@flatMap Mono.error<Map<String, Any>>(RuntimeException("No locations found for query: $query"))
                }

                val selectedLocation = locations.first()
                val coordinates = Coordinates(selectedLocation.point.lat, selectedLocation.point.lng)

                val weatherMono = weatherService.getWeather(coordinates)
                val placesMono = placesService.getInterestingPlaces(coordinates, 1000)

                Mono.zip(weatherMono, placesMono)
                    .flatMap{tuple ->
                        val weather = tuple.t1
                        val places = tuple.t2

                        val detailMonos = places.map{
                                place -> placeDetailsService.getPlaceDetails(place.properties.xid)
                            .map{details -> place to details}
                        }

                        Mono.zip(detailMonos){array ->
                            @Suppress("UNCHECKED_CAST")
                            array.map{it as Pair<Place, PlaceDetailsResponse>}
                        }.map{placeDetails ->
                            mapOf(
                                "location" to selectedLocation,
                                "weather" to weather,
                                "placeDetails" to placeDetails
                            )
                        }
                    }

            }.doOnSuccess{
                    result -> model.addAttribute("locationInfo", result)
            }
            .doOnError{
                    error -> model.addAttribute("error", error.message?: "Unknown error occuried")
            }
            .thenReturn("location-info").toString()
    }
}