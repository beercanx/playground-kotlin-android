package uk.co.baconi.pka.td

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_departure_search.*
import uk.co.baconi.pka.tdb.station.StationCodes

class DepartureSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_departure_search)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar
                .make(view, "Test lookup: ${StationCodes.firstByCode("SHF")}", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}
