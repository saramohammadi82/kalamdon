package kalamdon.app.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Typeface


class App : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        const val SPLASH_TIME: Long = 1000 * 3
        lateinit var typeFaceRegular: Typeface
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        typeFaceRegular = Typeface.createFromAsset(assets, "vazir.ttf")
        replaceFont()
    }


    private fun replaceFont() {
        val staticField = Typeface::class.java
            .getDeclaredField("MONOSPACE")
        staticField.isAccessible = true
        staticField.set(null, typeFaceRegular)
    }
}