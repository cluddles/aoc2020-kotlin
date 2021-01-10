package aoc2020

import util.Dir8
import util.Vec2
import java.io.File

class Day11 {

    private data class SeatMap(val width: Int, val height: Int, val seats: MutableMap<Vec2, Seat> = HashMap())

    private enum class Seat { EMPTY, OCCUPIED }

    // Map of position -> seat state
    private fun parse(src: File) : SeatMap {
        val lines = src.readLines()
        val plan = SeatMap(lines.first().length, lines.size)
        for ((y, line) in lines.withIndex()) {
            for ((x, c) in line.withIndex()) {
                if (c == 'L') plan.seats[Vec2(x, y)] = Seat.EMPTY
            }
        }
        return plan
    }

    private fun adjacentPoints(p: Vec2) : List<Vec2> {
        return Dir8.values().map { p + it.delta }
    }

    // Check seats in 8 adjacent cells
    private fun seatTickAdjacent(pos: Vec2, seat: Seat, map: SeatMap) : Seat {
        val adj = adjacentPoints(pos).filter { map.seats[it] == Seat.OCCUPIED }.count()
        return when {
            adj == 0 -> Seat.OCCUPIED
            adj >= 4 -> Seat.EMPTY
            else -> seat
        }
    }

    private fun look(map: SeatMap, pos: Vec2, delta: Vec2) : Seat? {
        val newPos = pos + delta
        if (newPos.x < 0 || newPos.y < 0 || newPos.x >= map.width || newPos.y >= map.height) return null
        return map.seats[newPos] ?: look(map, newPos, delta)
    }

    // Look along each of the 8 directions for first seat
    private fun seatTickBeam(pos: Vec2, seat: Seat, map: SeatMap) : Seat {
        val adj = Dir8.values().filter { look(map, pos, it.delta) == Seat.OCCUPIED }.count()
        return when {
            adj == 0 -> Seat.OCCUPIED
            adj >= 5 -> Seat.EMPTY
            else -> seat
        }
    }

    private fun seatMapTick(map: SeatMap, seatTickFunc: (a: Vec2, b: Seat, c: SeatMap) -> Seat) : SeatMap {
        val next = SeatMap(map.width, map.height)
        map.seats.entries.forEach { next.seats[it.key] = seatTickFunc(it.key, it.value, map) }
        return next
    }

    private fun solve(seatMap: SeatMap, seatTickFunc: (a: Vec2, b: Seat, c: SeatMap) -> Seat) : Int {
        var next: SeatMap? = null
        do {
            val current = next ?: seatMap
            next = seatMapTick(current, seatTickFunc)
        } while (current != next)
        return next.seats.filter { it.value == Seat.OCCUPIED }.count()
    }

    fun solvePart1(src: File) : Int {
        return solve(parse(src), ::seatTickAdjacent)
    }

    fun solvePart2(src: File) : Int {
        return solve(parse(src), ::seatTickBeam)
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day11()
            val file = File("src/main/resources/aoc2020/day11.input")
            println(solver.solvePart1(file))
            println(solver.solvePart2(file))
        }
    }

}