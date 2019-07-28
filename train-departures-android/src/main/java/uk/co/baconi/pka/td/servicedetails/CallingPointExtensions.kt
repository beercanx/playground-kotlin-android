package uk.co.baconi.pka.td.servicedetails

import uk.co.baconi.pka.common.openldbws.services.CallingPoint

val CallingPoint.hasEstimatedTime: Boolean
    get() = this.estimatedTime.isNullOrBlank().not()
