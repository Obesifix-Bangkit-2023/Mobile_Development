package org.obesifix.obesifix.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
class UserPreference private constructor(private val dataStore: DataStore<Preferences>){
    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[USERID_KEY] ?:"",
                preferences[ACCESSTOKEN_KEY] ?:"",
                preferences[REFRESHTOKEN_KEY] ?:"",
                preferences[STATE_KEY] ?: false,
            )
        }
    }

    fun getUserData(): Flow<UserDataModel> {
        return dataStore.data.map { preferences ->
            UserDataModel(
                userId = preferences[USERID_KEY] ?: "",
                name = preferences[NAME_KEY] ?: "",
                email = preferences[EMAIL_KEY] ?: "",
                picture = preferences[PICTURE_KEY] ?: "",
                age = preferences[AGE_KEY] ?: 0,
                gender = preferences[GENDER_KEY] ?: "",
                height = preferences[HEIGHT_KEY] ?: 0.0,
                weight = preferences[WEIGHT_KEY] ?: 0.0,
                activity = preferences[ACTIVITY_KEY] ?: "",
                foodType = preferences[FOODTYPE_KEY] ?: "",
                createdAt = preferences[CREATEDAT_KEY] ?: "",
                updatedAt = preferences[UPDATEDAT_KEY] ?: ""
            )
        }
    }

    suspend fun saveUser(user: UserModel) { //login
        dataStore.edit { preferences ->
            preferences[USERID_KEY] = user.userId
            preferences[ACCESSTOKEN_KEY] = user.accessToken
            preferences[REFRESHTOKEN_KEY] =user.refreshToken
            preferences[STATE_KEY] = user.isLogin
        }
    }

    suspend fun saveUserData(userData: UserDataModel) {
        dataStore.edit { preferences ->
            preferences[USERID_KEY] = userData.userId
            preferences[NAME_KEY] = userData.name
            preferences[EMAIL_KEY] = userData.email
            preferences[PICTURE_KEY] = userData.picture
            preferences[AGE_KEY] = userData.age
            preferences[GENDER_KEY] = userData.gender
            preferences[HEIGHT_KEY] = userData.height
            preferences[WEIGHT_KEY] = userData.weight
            preferences[ACTIVITY_KEY] = userData.activity
            preferences[FOODTYPE_KEY] = userData.foodType
            preferences[CREATEDAT_KEY] = userData.createdAt
            preferences[UPDATEDAT_KEY] = userData.updatedAt
        }
    }

    fun getUserId(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[USERID_KEY] ?: ""
        }
    }
    fun getAccessTokenUser(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[ACCESSTOKEN_KEY] ?: ""
        }
    }

    fun getUserLogin(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[STATE_KEY] ?: false
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[USERID_KEY] =""
            preferences[ACCESSTOKEN_KEY] = ""
            preferences[REFRESHTOKEN_KEY] = ""
            preferences[STATE_KEY] = false
            preferences[NAME_KEY] = ""
            preferences[EMAIL_KEY] = ""
            preferences[PICTURE_KEY] = ""
            preferences[AGE_KEY] = 0
            preferences[GENDER_KEY] = ""
            preferences[HEIGHT_KEY] = 0.0
            preferences[WEIGHT_KEY] = 0.0
            preferences[ACTIVITY_KEY] = ""
            preferences[FOODTYPE_KEY] = ""
            preferences[CREATEDAT_KEY] = ""
            preferences[UPDATEDAT_KEY] = ""
            //CONTINUE TO REMOVE
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val USERID_KEY = stringPreferencesKey("user_id")
        private val ACCESSTOKEN_KEY = stringPreferencesKey("access_token_id")
        private val REFRESHTOKEN_KEY = stringPreferencesKey("refresh_token_id")
        private val STATE_KEY = booleanPreferencesKey("state")

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PICTURE_KEY = stringPreferencesKey("picture")
        private val AGE_KEY = intPreferencesKey("age")
        private val GENDER_KEY = stringPreferencesKey("gender")
        private val HEIGHT_KEY = doublePreferencesKey("height")
        private val WEIGHT_KEY = doublePreferencesKey("weight")
        private val ACTIVITY_KEY = stringPreferencesKey("activity")
        private val FOODTYPE_KEY = stringPreferencesKey("food_type")
        private val CREATEDAT_KEY = stringPreferencesKey("created_at")
        private val UPDATEDAT_KEY = stringPreferencesKey("updated_at")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}