package util

/**
 * Representation of a 2D integer vector
 *
 * @property x X component
 * @property y Y component
 */
data class Vec(val x: Int, val y: Int) {
    operator fun plus(o: Vec) : Vec { return Vec(x + o.x, y + o.y) }
}
