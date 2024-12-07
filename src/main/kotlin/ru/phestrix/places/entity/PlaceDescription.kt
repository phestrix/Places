package ru.phestrix.places.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

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


data class WikipediaExtract @JsonCreator constructor(
    @JsonProperty("title") val title: String = "",
    @JsonProperty("text") val text: String = "",
    @JsonProperty("html") val html: String = ""
)