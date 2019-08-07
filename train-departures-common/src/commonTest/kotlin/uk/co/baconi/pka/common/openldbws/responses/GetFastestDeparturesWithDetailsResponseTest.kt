package uk.co.baconi.pka.common.openldbws.responses

import uk.co.baconi.pka.common.openldbws.requests.DeparturesType

class GetFastestDeparturesWithDetailsResponseTest : BaseDeparturesWithDetailsResponseTest() {
    override val type = DeparturesType.FastestDeparturesWithDetails
}