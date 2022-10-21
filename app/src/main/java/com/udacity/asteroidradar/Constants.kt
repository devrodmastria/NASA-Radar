package com.udacity.asteroidradar

object Constants {
    const val API_QUERY_DATE_FORMAT = "yyyy-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 2
    const val BASE_URL = "https://api.nasa.gov/"

    const val IMAGE_OF_TODAY = "planetary/apod?api_key="

    const val ASTEROID_LIST_FEED_URL = "neo/rest/v1/"
    const val ASTEROID_LIST_FROM = "start_date"
    const val ASTEROID_LIST_TO = "end_date"
    const val ASTEROID_LIST_KEY = "api_key"

    // sample query with date range
    // neo/rest/v1/feed?start_date=2022-10-01&end_date=2022-10-02&api_key=
}