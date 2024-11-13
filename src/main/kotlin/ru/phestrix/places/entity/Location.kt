package ru.phestrix.places.entity

data class GeocodingResponse(
    val hits: List<Location>,
    val took: Long
)

data class Location(
    val point: GeocodingPoint,
    val osm_id: String,
    val osm_type: String,
    val osm_key: String,
    val name: String,
    val country: String,
    val city: String,
    val state: String,
    val street: String,
    val housenumber: String,
    val postcode: String
)

data class GeocodingPoint(
    val lat: Double,
    val lng: Double
)
