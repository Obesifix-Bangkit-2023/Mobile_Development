package org.obesifix.obesifix.preference

data class UserModel(
    //api
    val user_id: String,
    val name: String,
    val email: String,
    val picture: String,
    val age: Int,
    val gender: String,
    val height: Float,
    val weight: Float,
    val activity: String,
    val foodType: String, //need regex everytime
    val createdAt: String,
    val updatedAt: String,
    //firebase
    val isLogin: Boolean,
    val token: String
)