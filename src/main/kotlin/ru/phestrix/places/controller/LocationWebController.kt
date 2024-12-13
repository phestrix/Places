package ru.phestrix.places.controller

import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Mono
import ru.phestrix.places.dto.LocationDto
import ru.phestrix.places.dto.PlacesDto
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
    fun getLocationInfo(@RequestParam(required = false) query: String?, model: Model, session: HttpSession): String {
        if (query.isNullOrBlank()) {
            val cachedLocations = session.getAttribute("locations") as? List<LocationDto>
            if (cachedLocations != null) {
                model.addAttribute("locations", cachedLocations)
                return "location-list"
            }
            return "location-info"
        }

        try {
            val locationsDto = locationService.searchLocations(query).block()
            if (locationsDto?.locations.isNullOrEmpty()) {
                model.addAttribute("error", "No locations found for \"$query\".")
            } else {
                val locations = locationsDto!!.locations
                model.addAttribute("locations", locations)
                session.setAttribute("locations", locations)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            model.addAttribute("error", e.message ?: "Unknown error occurred.")
        }

        return "location-info"
    }

    @GetMapping("/location/details")
    fun getLocationDetails(
        @RequestParam lat: Double,
        @RequestParam lng: Double,
        @RequestParam(required = false) osmValue: String?,
        @RequestParam(required = false) osmKey: String?,
        @RequestParam(required = false) city: String?,
        model: Model,
        session: HttpSession
    ): String {
        try {
            val coordinates = Coordinates(lat, lng)

            val weatherMono = weatherService.getWeather(coordinates)
            val placesMono = placesService.getInterestingPlaces(coordinates, 1000)

            val result = Mono.zip(weatherMono, placesMono)
                .flatMap { tuple ->
                    val weather = tuple.t1
                    val places = tuple.t2

                    placeDetailsService.getLimitedPlacesDetails(places)
                        .map { detailsList -> PlacesDto(weather, detailsList) }
                }
                .block()

            if (result != null) {
                model.addAttribute("placesDto", result)
                model.addAttribute("selectedLocation", mapOf(
                    "osm_key" to (osmKey?: "Unknown key"),
                    "osm_value" to (osmValue ?: "Unknown value"),
                    "city" to (city ?: "Unknown city")
                ))
            } else {
                model.addAttribute("error", "Failed to retrieve place details.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            model.addAttribute("error", e.message ?: "Unknown error occurred.")
        }

        return "location-info"
    }

    @GetMapping("/location/list")
    fun showLocationList(session: HttpSession, model: Model): String {
        val locations = session.getAttribute("locations") as? List<LocationDto>
        if (locations.isNullOrEmpty()) {
            model.addAttribute("error", "No locations found. Please search again.")
            return "location-info"
        }

        model.addAttribute("locations", locations)
        return "location-list"
    }
}