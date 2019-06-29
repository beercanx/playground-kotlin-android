package uk.co.baconi.pka.td.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import kotlinx.android.synthetic.main.content_app_bar_layout.*
import uk.co.baconi.pka.td.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        supportFragmentManager.transaction {
            replace(R.id.settings_container, SettingsFragment())
        }
    }
}
