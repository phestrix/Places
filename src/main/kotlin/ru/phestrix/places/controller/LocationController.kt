package ru.phestrix.places.controller

/*@RestController
@RequestMapping("/api")
class LocationController(
    private val locationService: LocationService,
    private val weatherService: WeatherService,
    private val placesService: PlacesService,
    private val placeDetailsService: PlaceDetailsService
) {
    @GetMapping("/locations")
    fun searchLocations(@RequestParam query: String): Mono<List<Location>> {
        return locationService.searchLocations(query)
    }

    @GetMapping("/places")
    fun getPlaces(
        @RequestParam lat: Double,
        @RequestParam lon: Double,
        @RequestParam(required = false, defaultValue = "1000") radius: Int
    ): Mono<List<Place>> {
        val coordinates = Coordinates(lat, lon)
        return placesService.getInterestingPlaces(coordinates, radius)
    }

    @GetMapping("/place/details")
    fun getPlaceDetails(@RequestParam xid: String): Mono<PlaceDetailsResponse> {
        return placeDetailsService.getPlaceDetails(xid)
    }

    @GetMapping("/weather")
    fun getWeather(@RequestParam lat: Double, @RequestParam lon: Double): Mono<WeatherResponse> {
        val coordinates = Coordinates(lat, lon)
        return weatherService.getWeather(coordinates)
    }

    @GetMapping("/location/info")
    fun getLocationInfo(@RequestParam query: String, model: Model): String {
        locationService.searchLocations(query)
            .flatMap { locations ->
                if (locations.isEmpty()) {
                    return@flatMap Mono.error<Map<String, Any>>(RuntimeException("No locations found for query: $query"))
                }

                val selectedLocation = locations.first()
                val coordinates = Coordinates(selectedLocation.point.lat, selectedLocation.point.lng)

                val weatherMono = weatherService.getWeather(coordinates)
                val placesMono = placesService.getInterestingPlaces(coordinates, 1000)

                Mono.zip(weatherMono, placesMono)
                    .flatMap { tuple ->
                        val weather = tuple.t1
                        val places = tuple.t2

                        val detailMonos =
                            placeDetailsService.getLimitedPlacesDetails(places)//places.map{ place -> placeDetailsService.getPlaceDetails(place.properties.xid).map{details -> place to details} }
                        detailMonos.flatMap { detailsList ->
                            val placeDetails = detailsList.map { it }
                            Mono.just(placeDetails)
                        }.map { placeDetails: List<Pair<Place, PlaceDetailsResponse>> ->
                            mapOf(
                                "location" to selectedLocation,
                                "weather" to weather,
                                "placeDetails" to placeDetails
                            )
                        }
                    }

            }.doOnSuccess { result ->
                model.addAttribute("locationInfo", result)
            }
            .doOnError { error ->
                model.addAttribute("error", error.message ?: "Unknown error occuried")
            }.block()

        return "location-info"
    }
}*/