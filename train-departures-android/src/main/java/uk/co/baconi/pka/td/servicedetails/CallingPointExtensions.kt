package uk.co.baconi.pka.td.servicedetails

import uk.co.baconi.pka.tdb.openldbws.responses.CallingPoint

val CallingPoint.hasEstimatedTime: Boolean
    get() = this.estimatedTime.isNullOrBlank().not()
