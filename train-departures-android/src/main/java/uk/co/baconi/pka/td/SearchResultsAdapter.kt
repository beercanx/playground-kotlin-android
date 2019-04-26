package uk.co.baconi.pka.td

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import uk.co.baconi.pka.tdb.openldbws.responses.ServiceItem

class SearchResultsAdapter(private val searchResults: MutableList<ServiceItem>) : RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class SearchResultsViewHolder(
        layout: View,
        val avatar: TextView,
        val destination: TextView,
        val tickerLine: TextView,
        val departureTime: TextView
    ) : RecyclerView.ViewHolder(layout)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder {

        // create a new view
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)

        val avatar: TextView = layout.findViewById(R.id.search_result_avatar)
        val destination: TextView = layout.findViewById(R.id.search_result_destination)
        val tickerLine: TextView = layout.findViewById(R.id.search_result_ticker_line)
        val departureTime: TextView = layout.findViewById(R.id.search_result_departure_time)

        return SearchResultsViewHolder(layout, avatar, destination, tickerLine, departureTime)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) {

        val context = holder.itemView.context

        val service = searchResults[position]
        val platform = service.platform
        val destination = service.destination?.first()
        val destinationName = destination?.locationName
        val destinationCrs = destination?.crs
        val departureTime = service.std

        val actualDepartureTime = when(service.etd) {
            null -> context.getString(R.string.search_result_etd_null)
            "On time" -> context.getString(R.string.search_result_etd_on_time)
            "Delayed" -> context.getString(R.string.search_result_etd_delayed)
            else -> context.getString(R.string.search_result_etd_other, service.etd)
        }

        // TODO - Dynamically change colour depending on display text
        holder.avatar.text = platform
        holder.destination.text = context.getString(
            R.string.search_result_destination, destinationName, destinationCrs
        )
        holder.tickerLine.text = context.getString(
            R.string.search_result_ticker_line, departureTime, destinationName, platform, actualDepartureTime
        )
        holder.departureTime.text = departureTime
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = searchResults.size
}