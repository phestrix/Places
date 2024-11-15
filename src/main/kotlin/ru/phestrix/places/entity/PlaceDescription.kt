package ru.phestrix.places.entity

data class PlaceDetailsResponse(
    val xid: String,
    val name: String?,
    val address: PlaceAddress,
    val kinds: String,
    val wikipedia_extracts: WikipediaExtract?,
    val image: String?
)


data class PlaceAddress(
    val city: String?,
    val road: String?,
    val house: String?,
    val country: String?
)


data class WikipediaExtract(
    val text: String
)