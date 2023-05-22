package org.obesifix.obesifix.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.map
import java.util.concurrent.Flow

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){
//    fun getUser(): Flow<UserModel>{
//        return dataStore.data.map { preferences ->
//            UserModel(
//
//            )
//        }
//    }

}