package ru.phestrix.places.dto

import ru.phestrix.places.entity.Place
import ru.phestrix.places.entity.PlaceDetailsResponse
import ru.phestrix.places.entity.WeatherResponse

data class PlacesDto(
     val weatherResponse: WeatherResponse,
     val placesAndDescription: List<Pair<Place, PlaceDetailsResponse>>
)
