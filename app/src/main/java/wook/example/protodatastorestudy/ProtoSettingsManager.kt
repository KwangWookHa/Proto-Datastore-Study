package wook.example.protodatastorestudy

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException

class ProtoSettingsManager(context: Context) {

    private val Context.userSettingsStore: DataStore<UserSettings> by dataStore(
        fileName = "user_settings.pb",
        serializer = ProtoSettingsSerializer
    )

    private val dataStore = context.userSettingsStore

    val userSettingsFlow: Flow<UserSettings> = dataStore.data.catch { e ->
        if (e is IOException) {
            emit(UserSettings.getDefaultInstance())
        } else {
            throw e
        }
    }
    suspend fun updateUserSettings(bgColor: UserSettings.BgColor, colorText: String) {
        dataStore.updateData {
            it.toBuilder()
                .setBgColor(bgColor)
                .setColorText(colorText)
                .build()
        }
    }

}