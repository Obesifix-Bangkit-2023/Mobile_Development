package org.obesifix.obesifix.preference

data class UserDataModel(
    val userId: String,
    val name: String,
    val email: String,
    val picture: String,
    val age: Int,
    val gender: String,
    val height: Double,
    val weight: Double,
    val activity: String,
    val foodType: String,
    val createdAt: String,
    val updatedAt: String
)
