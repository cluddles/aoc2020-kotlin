package aoc2020

import java.io.File
import kotlin.collections.ArrayList

class Day20 {

    private val N = 0
    private val E = 1
    private val S = 2
    private val W = 3

    data class Tile(val id: Int, val edges: List<String>) {
        constructor(id: Int, n: String, e: String, s: String, w: String) : this(id, listOf(n, e, s, w))
    }

    fun parse(src: File) : Map<Int, Tile> {
        val lines = src.readLines()
        val tiles = HashMap<Int, Tile>()
        // Tile: xxxx, then 10 lines of bitmap data, then blank line
        for (i in lines.indices step 12) {
            parseTile(lines.slice(i..i+10)).let { tiles[it.id] = it }
        }
        return tiles
    }

    fun parseTile(lines: List<String>) : Tile {
        return Tile(
            lines[0].substring(5, lines[0].length - 1).toInt(),
            lines[1],
            lines.takeLast(10).map { it[9] }.joinToString(""),
            lines[10],
            lines.takeLast(10).map { it[0] }.joinToString("")
        )
    }

    fun flipX(tile: Tile) = Tile(tile.id, tile.edges[N].reversed(), tile.edges[W], tile.edges[S].reversed(), tile.edges[E])
    fun flipY(tile: Tile) = Tile(tile.id, tile.edges[S], tile.edges[E].reversed(), tile.edges[N], tile.edges[W].reversed())

    fun rot(tile: Tile, steps: Int) : Tile {
        return when(steps) {
            1 -> Tile(tile.id, tile.edges[W].reversed(), tile.edges[N], tile.edges[E].reversed(), tile.edges[S])
            2 -> flipX(flipY(tile))
            3 -> Tile(tile.id, tile.edges[E], tile.edges[S].reversed(), tile.edges[W], tile.edges[N].reversed())
            else -> throw IllegalArgumentException("Unsupported rot steps $steps")
        }
    }

    fun solvePart1(src: File) : Long {
        val tiles = parse(src)
        val joints = HashMap<Int, HashSet<Int>>()
        for (t in tiles.values) {
            // I think there are 8 permutations in total
            val permutations = listOf(
                t,
                flipX(t),
                rot(t, 1),
                flipY(rot(t, 1)),
                rot(t, 2),
                flipY(t),
                rot(t, 3),
                flipY(rot(t, 3)))
            for ((index, i) in permutations.withIndex()) {
                for (j in tiles.values) {
                    if (i.id == j.id) continue
                    if (i.edges[N] == j.edges[S]) {
                        println("Match N: " + i.id + " (perm $index) " + j.id)
                    } else if (i.edges[E] == j.edges[W]) {
                        println("Match E: " + i.id + " (perm $index) " + j.id)
                    } else if (i.edges[S] == j.edges[N]) {
                        println("Match S: " + i.id + " (perm $index) " + j.id)
                    } else if (i.edges[W] == j.edges[E]) {
                        println("Match W: " + i.id + " (perm $index) " + j.id)
                    } else { continue }
                    joints.getOrPut(i.id, { hashSetOf() } ).add(j.id)
                    joints.getOrPut(j.id, { hashSetOf() } ).add(i.id)
                }
            }
        }
        println(joints)
        return joints.entries.filter { it.value.size == 2 }.map { it.key }.fold(1L, { a, b -> a * b })
    }

    fun solvePart2(src: File) : Long {
        // Okay, so part 2 needs us to actually stitch the image together
        // Going to need to do some hefty refactoring
        // One thing that strikes me: instead of calculating all the silly permutations, can basically
        // just check each edge of a tile against each other edge of the other (flipped and non-flipped)
        // Based on that you can figure out rotation, flipX, flipY
        // Then I think you just place a single corner and iterate out from there?
        return -1L
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day20()
            val file = File("src/main/resources/aoc2020/day20.input")
            println("Part1: " + solver.solvePart1(file))
            // println("Part2: " + solver.solvePart2(file))
        }
    }

}