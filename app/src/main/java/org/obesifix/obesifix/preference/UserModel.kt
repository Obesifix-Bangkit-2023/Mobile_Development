package org.obesifix.obesifix.preference

data class UserModel(
    val userId: String,
    val accessToken: String,
    val refreshToken: String,
    val isLogin: Boolean,
)