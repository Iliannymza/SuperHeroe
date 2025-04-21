package com.example.superheroe.data

import com.google.gson.annotations.SerializedName


data class SuperheroeSearchResponse(
    val results: List<Superheroe>
)

data class Superheroe(
    val id: String,
    val name: String,
    val image: Image,
    val biography: Biography
)

data class  Biography (
    @SerializedName("full-name") val realNAme: String,
    @SerializedName("place-of-birth") val placeOfBirth: String,
    val publisher: String,
    val alignment: String
)

data class Image(
    val url: String
)