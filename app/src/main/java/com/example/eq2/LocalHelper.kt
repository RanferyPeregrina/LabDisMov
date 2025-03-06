import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*

object LocaleHelper {

    private const val PREFS_NAME = "settings"
    private const val LANGUAGE_KEY = "language"

    fun setLocale(context: Context, language: String) {
        saveLanguagePreference(context, language)
        updateResources(context, language)
    }

    fun loadLocale(context: Context) {
        val savedLanguage = getLanguagePreference(context)
        updateResources(context, savedLanguage)
    }

    private fun saveLanguagePreference(context: Context, language: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(LANGUAGE_KEY, language).apply()
    }

    private fun getLanguagePreference(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(LANGUAGE_KEY, "es") ?: "es" // Español por defecto
    }

    private fun updateResources(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
