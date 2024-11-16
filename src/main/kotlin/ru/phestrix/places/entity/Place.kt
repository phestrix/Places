package ru.phestrix.places.entity

data class PlacesResponse(
    val features: List<Place>
)

data class Place(
    val id: String,
    val geometry: Geometry,
    val properties: PlaceProperties
)

data class Geometry(
    val coordinates: List<Double>
)

data class PlaceProperties(
    val xid: String,
    val name: String?,
    val kinds: String,
    val dist: Double
)