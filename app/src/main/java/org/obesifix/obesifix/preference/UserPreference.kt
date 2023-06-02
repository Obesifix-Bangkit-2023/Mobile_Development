package org.obesifix.obesifix.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreference private constructor(private val dataStore: DataStore<Preferences>){
    //lokal hanya id dan token(otomatis di firebase class)
    //need revision

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[USERID_KEY] ?:"",
                preferences[NAME_KEY] ?:"",
                preferences[EMAIL_KEY] ?:"",
                preferences[PICTURE_KEY] ?:"",
                preferences[AGE_KEY] ?: 0,
                preferences[GENDER_KEY] ?: "",
                preferences[HEIGHT_KEY] ?: 0.0f,
                preferences[WEIGHT_KEY] ?: 0.0f,
                preferences[ACTIVITY_KEY] ?: "",
                preferences[FOODTYPE_KEY] ?: "",
                preferences[CREATEDAT_KEY] ?: "",
                preferences[UPDATEDAT_KEY] ?: "",
                preferences[STATE_KEY] ?: false,
                preferences[TOKEN_KEY] ?: "",
            )
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    suspend fun saveUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USERID_KEY] = user.user_id
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[PICTURE_KEY] = user.picture
            preferences[AGE_KEY] = user.age
            preferences[GENDER_KEY] = user.gender
            preferences[HEIGHT_KEY] = user.height
            preferences[WEIGHT_KEY] = user.weight
            preferences[ACTIVITY_KEY] = user.activity
            preferences[FOODTYPE_KEY] = user.foodType
            preferences[CREATEDAT_KEY] = user.createdAt
            preferences[UPDATEDAT_KEY] = user.updatedAt
            preferences[STATE_KEY] = user.isLogin
            preferences[TOKEN_KEY] = user.token
        }
    }

    suspend fun saveUserId(id: String) {
        dataStore.edit { preferences ->
            preferences[USERID_KEY] = id
        }
    }

    fun getUserId(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USERID_KEY] ?: ""
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[USERID_KEY] =""
            preferences[NAME_KEY] =""
            preferences[EMAIL_KEY] =""
            preferences[PICTURE_KEY] =""
            preferences[AGE_KEY] = 0
            preferences[GENDER_KEY] = ""
            preferences[HEIGHT_KEY] = 0.0f
            preferences[WEIGHT_KEY] = 0.0f
            preferences[ACTIVITY_KEY] = ""
            preferences[FOODTYPE_KEY] = ""
            preferences[CREATEDAT_KEY] = ""
            preferences[UPDATEDAT_KEY] = ""
            preferences[STATE_KEY] = false
            preferences[TOKEN_KEY] = ""
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        //api
        private val USERID_KEY = stringPreferencesKey("user_id")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PICTURE_KEY = stringPreferencesKey("picture")
        private val AGE_KEY = intPreferencesKey("age")
        private val GENDER_KEY = stringPreferencesKey("gender")
        private val HEIGHT_KEY = floatPreferencesKey("height")
        private val WEIGHT_KEY = floatPreferencesKey("weight")
        private val ACTIVITY_KEY = stringPreferencesKey("activity")
        private val FOODTYPE_KEY = stringPreferencesKey("foodType") //need regex string list or vice versa
        private val CREATEDAT_KEY = stringPreferencesKey("createdAt")
        private val UPDATEDAT_KEY = stringPreferencesKey("updatedAt")
        //firebase
        private val STATE_KEY = booleanPreferencesKey("state") //isLogin
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}