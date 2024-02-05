package org.obesifix.obesifix.network.payload

data class RegisterBody(
    var name: String,
    var email: String,
    var password: String,
    var age: Int,
    var gender: String,
    var height: Float,
    var weight: Float,
    var activity: String,
    var food_type: String,
)
