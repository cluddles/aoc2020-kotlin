package util

import kotlin.math.abs

/**
 * Representation of a 3D integer vector
 *
 * @property x X component
 * @property y Y component
 * @property z Z component
 */
data class Vec3(val x: Int, val y: Int, val z: Int) {
    operator fun plus(o: Vec3) : Vec3 { return Vec3(x + o.x, y + o.y, z + o.z) }
    operator fun times(o: Int) : Vec3 { return Vec3(x * o, y * o, z * o) }
    fun manhattan() : Int { return abs(x) + abs(y) + abs(z) }
}
