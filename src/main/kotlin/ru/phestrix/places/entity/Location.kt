package ru.phestrix.places.entity

data class Location(
    val point: Pair<Double, Double>,
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
