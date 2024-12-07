package ru.phestrix.places.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class PlacesResponse @JsonCreator constructor(
    @JsonProperty("type") val type: String,
    @JsonProperty("features") val features: List<Place>
)

data class Place @JsonCreator constructor(
    @JsonProperty("type") val type: String,
    @JsonProperty("properties") val properties: PlaceProperties,
    @JsonProperty("geometry") val geometry: Geometry
)

data class Geometry @JsonCreator constructor(
    @JsonProperty("type") val type: String,
    @JsonProperty("coordinates") val coordinates: List<Double>
)

data class PlaceProperties @JsonCreator constructor(
    @JsonProperty("name") val name: String?,
    @JsonProperty("kind") val kind: String?,
    @JsonProperty("xid") val xid: String
)