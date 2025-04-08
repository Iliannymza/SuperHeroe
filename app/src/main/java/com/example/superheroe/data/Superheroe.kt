package com.example.superheroe.data


data class SuperheroeSearchResponse(
    val results: List<Superheroe>
)

data class Superheroe(
    val id: String,
    val name: String,
    val image: Image
)

data class Image(
    val url: String
)