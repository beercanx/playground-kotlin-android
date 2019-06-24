package uk.co.baconi.pka.td.servicedetails

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import uk.co.baconi.pka.td.R
import uk.co.baconi.pka.td.getColourCompat
import uk.co.baconi.pka.tdb.openldbws.responses.CallingPoint
import kotlin.math.min

class CallingPointsDrawable(
    private val context: Context,
    private val previous: List<CallingPoint>,
    private val current: CallingPoint,
    private val subsequent: List<CallingPoint>,
    private val all: List<CallingPoint>
): Drawable() {

    private val previousColour: Paint = Paint().apply {
        color = context.getColourCompat(R.color.service_details_calling_point_previous)
    }
    private val currentColour: Paint = Paint().apply {
        color = context.getColourCompat(R.color.service_details_calling_point_current)
    }
    private val subsequentColour: Paint = Paint().apply {
        color = context.getColourCompat(R.color.service_details_calling_point_subsequent)
    }
    private val connectionColour: Paint = Paint().apply {
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

        val cellCentreX = singleCellWidth + (singleCellWidth / 2f)
        val cellCentreY = singleCellHeight / 2f

        val lastCellCentreY = cellCentreY + (singleCellHeight * (all.lastIndex * 2f))

        canvas.drawLine(cellCentreX, cellCentreY, cellCentreX, lastCellCentreY, connectionColour)

        // TODO - Consider marking start point, based on search details
        // TODO - Consider marking endpoint point, based on search details

        all.forEachIndexed { index, entry ->

            val colour = when {
                current == entry -> currentColour
                previous.contains(entry) -> previousColour
                subsequent.contains(entry) -> subsequentColour
                else -> connectionColour
            }

            canvas.drawCircle(cellCentreX, cellCentreY + (singleCellHeight * (index * 2f)), radius, colour)
        }
    }

    override fun setAlpha(alpha: Int) {
        // This method is required
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // This method is required
    }

    override fun getOpacity(): Int =
        // Must be PixelFormat.UNKNOWN, TRANSLUCENT, TRANSPARENT, or OPAQUE
        PixelFormat.OPAQUE
}