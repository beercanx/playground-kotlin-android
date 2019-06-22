package uk.co.baconi.pka.td.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uk.co.baconi.pka.td.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .commit()
    }
}
