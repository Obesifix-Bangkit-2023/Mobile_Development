package org.obesifix.obesifix.network.body

data class EditBody (
    var name: String,
    var age: Int,
    var height: Float,
    var weight: Float,
    var activity: String,
    var food_type: String
    )