package util

import kotlin.math.abs

/**
 * Representation of a 2D integer vector
 *
 * @property x X component
 * @property y Y component
 */
data class Vec2(val x: Int, val y: Int) {
    operator fun plus(o: Vec2) : Vec2 { return Vec2(x + o.x, y + o.y) }
    operator fun times(o: Int) : Vec2 { return Vec2(x * o, y * o) }
    fun manhattan() : Int { return abs(x) + abs(y) }
}
