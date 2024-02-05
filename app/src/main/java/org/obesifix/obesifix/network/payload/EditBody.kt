package org.obesifix.obesifix.network.payload

data class EditBody (
    var name: String,
    var age: Int,
    var height: Float,
    var weight: Float,
    var activity: String,
    var food_type: String
    )