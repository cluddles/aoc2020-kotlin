package util

/**
 * Representation of combined cardinal (N, E, S, W) and ordinal (NE, SE, SW, NW) directions
 *
 * @property delta The 2d vector representation of this direction
 */
enum class Dir8(val delta: Vec) {
    N (Vec(0, -1)),
    NE(Vec(1, -1)),
    E (Vec(1, 0)),
    SE(Vec(1, 1)),
    S (Vec(0, 1)),
    SW(Vec(-1, 1)),
    W (Vec(-1, 0)),
    NW(Vec(-1, -1)),
}
