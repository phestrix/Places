package ru.phestrix.places.web

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "api.keys")
data class ApiKeyConfig (
    var graphhopper: String = "",
    var openweathermap: String = "",
    var opentripmap: String = ""
)