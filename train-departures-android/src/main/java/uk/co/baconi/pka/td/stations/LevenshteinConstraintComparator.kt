package uk.co.baconi.pka.td.stations

import java.util.*
import kotlin.Comparator

/**
 * Compares the Levenshtein distance of a specific field and a constraint value.
 */
class LevenshteinConstraintComparator<A>(
    private val constraint: CharSequence,
    private val fieldExtractor: (A) -> CharSequence,
    private val ignoreCase: Boolean = false
) : Comparator<A> {

    override fun compare(lhs: A, rhs: A): Int {

        val lhsField = if(ignoreCase) {
            fieldExtractor(lhs).toString().toLowerCase(Locale.getDefault())
        } else {
            fieldExtractor(lhs)
        }

        val rhsField = if(ignoreCase) {
            fieldExtractor(rhs).toString().toLowerCase(Locale.getDefault())
        } else {
            fieldExtractor(rhs)
        }

        return levenshtein(lhsField, constraint).compareTo(levenshtein(rhsField, constraint))
    }

    /**
     * Taken from https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Kotlin
     */
    private fun levenshtein(lhs : CharSequence, rhs : CharSequence) : Int {
        val lhsLength = lhs.length
        val rhsLength = rhs.length

        var cost = IntArray(lhsLength + 1) { it }
        var newCost = IntArray(lhsLength + 1) { 0 }

        for (i in 1..rhsLength) {
            newCost[0] = i

            for (j in 1..lhsLength) {
                val editCost= if(lhs[j - 1] == rhs[i - 1]) 0 else 1

                val costReplace = cost[j - 1] + editCost
                val costInsert = cost[j] + 1
                val costDelete = newCost[j - 1] + 1

                newCost[j] = minOf(costInsert, costDelete, costReplace)
            }

            val swap = cost
            cost = newCost
            newCost = swap
        }

        return cost[lhsLength]
    }
}