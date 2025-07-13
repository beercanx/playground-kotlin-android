package uk.co.baconi.pka.td.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction

import uk.co.baconi.pka.td.R
import uk.co.baconi.pka.td.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.settingsAppBar.toolbar)
        supportFragmentManager.transaction {
            replace(R.id.settings_container, SettingsFragment())
        }
    }
}
