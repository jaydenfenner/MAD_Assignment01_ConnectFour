package com.example.mad_assignmen01_connectfour

/**
 * Collection of methods for determining a score for each position
 *
 * All methods take a Position instance and return a signed integer
 */
object PositionHeuristics {


    /**
     * Simplest and worst, just assign equal (draw) weight to all positions
     */
    fun alwaysDraw(pos: Position) = 0
}

//fun testGetScore(pos: Position): Int {
//    return PositionHeuristics.randomPlay(pos)
//}