package kalamdon.app.android

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(private val context: Context) {
    companion object {
        const val KEY_INT_LEVEL = "KEY_LAST_LEVELS"
        const val KEY_INT_SCORE = "KEY_SCORE"

    }

    private fun sharedPreferencesLoadObject(key: String, defValue: Any): Any {
        val sharedPref: SharedPreferences = context.getSharedPreferences(context.packageName, 0)
        return when (defValue) {
            is Int -> sharedPref.getInt(key, defValue)
            is Boolean -> sharedPref.getBoolean(key, defValue)
            else -> sharedPref.getString(key, defValue as String)!!
        }

    }

    fun deleteAll() {
        val sharedPref: SharedPreferences = context.getSharedPreferences(context.packageName, 0)
        val editor = sharedPref.edit()
        editor.clear().apply()
    }

    fun sharedPreferencesLoad(key: String, defValue: String): String {
        return sharedPreferencesLoadObject(key, defValue) as String
    }

    fun sharedPreferencesLoad(key: String, defValue: Int): Int {
        return sharedPreferencesLoadObject(key, defValue) as Int
    }

    fun sharedPreferencesLoad(key: String, defValue: Boolean): Boolean {
        return sharedPreferencesLoadObject(key, defValue) as Boolean
    }

    fun sharedPreferencesSave(key: String, value: Any) {
        val sharedPref: SharedPreferences = context.getSharedPreferences(context.packageName, 0)
        val editor = sharedPref.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
        }
        return editor.apply()
    }


}
