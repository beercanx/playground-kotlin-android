package uk.co.baconi.pka.td.stations

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import uk.co.baconi.pka.common.stations.StationCode
import uk.co.baconi.pka.td.R

class StationCodeAdapter(context: Context) : ArrayAdapter<StationCode>(
    context, viewResource, mutableListOf()
) {

    companion object {
        private const val viewResource: Int = R.layout.search_criteria_station_view_item
    }

    private lateinit var filter: Filter

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var dropDownInflater: LayoutInflater? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(inflater, position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = dropDownInflater ?: inflater
        return createViewFromResource(inflater, position, convertView, parent)
    }

    override fun getFilter(): Filter {
        if(!this::filter.isInitialized) {
            filter = StationCodeFilter(this)
        }
        return filter
    }

    private fun createViewFromResource(inflater: LayoutInflater, position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View = if (
            convertView?.findViewById<TextView>(R.id.search_criteria_station_avatar) == null ||
            convertView.findViewById<TextView>(R.id.search_criteria_station_name) == null
        ) {
            inflater.inflate(viewResource, parent, false)
        } else {
            convertView
        }

        val stationCode = getItem(position)

        stationCode?.crsCode?.let { crsCode ->
            view.findViewById<TextView>(R.id.search_criteria_station_avatar)?.text = crsCode
        }

        stationCode?.stationName?.let { stationName ->
            view.findViewById<TextView>(R.id.search_criteria_station_name)?.text = stationName
        }

        return view
    }

    override fun setDropDownViewTheme(theme: Resources.Theme?) {
        dropDownInflater = when {
            theme == null -> null
            theme == inflater.context.theme -> inflater
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> LayoutInflater.from(ContextThemeWrapper(context, theme))
            else -> null
        }
    }
}