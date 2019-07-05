package uk.co.baconi.pka.td.servicedetails

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import uk.co.baconi.pka.td.DepartureStatus.*
import uk.co.baconi.pka.td.R
import uk.co.baconi.pka.td.departureStatus
import uk.co.baconi.pka.td.getColourCompat
import uk.co.baconi.pka.tdb.openldbws.responses.CallingPoint
import kotlin.math.min

class CallingPointsDrawable(
    private val context: Context,
    private val all: List<CallingPoint>
): Drawable() {

    private val unknownPaint = Paint().apply {
        color = context.getColourCompat(R.color.search_result_departure_time_etd_unknown)
    }
    private val noReportPaint = Paint().apply {
        color = context.getColourCompat(R.color.search_result_departure_time_no_report)
    }
    private val onTimePaint = Paint().apply {
        color = context.getColourCompat(R.color.search_result_departure_time_on_time)
    }
    private val delayedPaint = Paint().apply {
        color = context.getColourCompat(R.color.search_result_departure_time_delayed)
    }
    private val cancelledPaint = Paint().apply {
        color = context.getColourCompat(R.color.search_result_departure_time_cancelled)
    }
    private val estimatedPaint = Paint().apply {
        color = context.getColourCompat(R.color.search_result_departure_time_estimated)
    }

    private val connectionPaint = Paint().apply {
        color = context.getColourCompat(R.color.service_details_calling_point_connection)
    }

    override fun draw(canvas: Canvas) {

        val maxWidth = bounds.width().toFloat()
        val maxHeight = bounds.height().toFloat()

        val numberOfCellsWide = 3f
        val numberOfCellsHigh = 1f + (all.lastIndex * 2)

        val singleCellWidth = maxWidth / numberOfCellsWide
        val singleCellHeight = maxHeight / numberOfCellsHigh

        val radius: Float = min(singleCellWidth, singleCellHeight) / 2f

        // Update paint colours to know how wide the stroke should be.
        val strokeWidth = radius / 3f
        unknownPaint.strokeWidth = strokeWidth
        noReportPaint.strokeWidth = strokeWidth
        onTimePaint.strokeWidth = strokeWidth
        delayedPaint.strokeWidth = strokeWidth
        cancelledPaint.strokeWidth = strokeWidth
        estimatedPaint.strokeWidth = strokeWidth
        connectionPaint.strokeWidth = strokeWidth

        val cellCentreX = singleCellWidth + (singleCellWidth / 2f)
        val cellCentreY = singleCellHeight / 2f

        val calculateCellCentreY = { index: Int -> cellCentreY + (singleCellHeight * (index * 2f)) }

        val lastCellCentreY = calculateCellCentreY(all.lastIndex)

        canvas.drawLine(cellCentreX, cellCentreY, cellCentreX, lastCellCentreY, connectionPaint)

        // TODO - Consider marking start point, based on search details
        // TODO - Consider marking endpoint point, based on search details

        all.forEachIndexed { index, entry ->

            // TODO - Extract something that also works with the SearchResultsAdapter
            val paint = when(entry.departureStatus) {
                null -> unknownPaint
                NO_REPORT -> noReportPaint
                ON_TIME -> onTimePaint
                DELAYED -> delayedPaint
                CANCELLED -> cancelledPaint
                HH_MM -> estimatedPaint
            }

            val adjustedCellCentreY = calculateCellCentreY(index)

            if(entry.hasEstimatedTime) {
                paint.configureAsDonut()
            } else {
                paint.configureAsCircle()
            }

            canvas.drawCircle(cellCentreX, adjustedCellCentreY, radius, paint)
        }
    }

    override fun setAlpha(alpha: Int) {
        // This method is required
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // This method is required
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    private fun Paint.configureAsDonut() {
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
    }

    private fun Paint.configureAsCircle() {
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.FILL_AND_STROKE
    }
}