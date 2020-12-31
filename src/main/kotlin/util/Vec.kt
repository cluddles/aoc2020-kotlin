package util

import kotlin.math.abs

/**
 * Representation of a 2D integer vector
 *
 * @property x X component
 * @property y Y component
 */
data class Vec(val x: Int, val y: Int) {
    operator fun plus(o: Vec) : Vec { return Vec(x + o.x, y + o.y) }
    operator fun times(o: Int) : Vec { return Vec(x * o, y * o) }
    fun manhattan() : Int { return abs(x) + abs(y) }
}
