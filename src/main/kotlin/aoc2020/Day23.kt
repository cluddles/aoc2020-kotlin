package aoc2020

import kotlin.math.sign

class Day23 {

    fun solvePart1(input: String) : String {
        // Current cup will always be the first in the string
        var state = input
        (1..100).forEach { _ ->
            // Destination is current-1
            var destination = state.first() - 1
            // Take the 3 clockwise cups
            val pickup = state.drop(1).take(3)
            // Remove the current and picked up cups; place current at the end
            state = state.drop(4) + state.take(1)
            // Bit of extra logic to make sure destination is present, and tick down (loop to highest)
            while (pickup.contains(destination) || destination < '1') {
                destination--
                if (destination < '1') destination = '9'
            }
            // Insert picked up cups into the correct position
            state = state.split(destination.toString()).let { it[0] + destination + pickup + it[1] }
        }
        // Result is cups listed clockwise from 1 (without 1)
        return state.split("1").let { it[1] + it[0] }
    }

    class CupCircle(input: String, val size: Int) {
        val first: Cup = Cup(Character.getNumericValue(input.first()))
        var current = first

        init {
            first.next = first
            first.prev = first
            (1 until size).forEach {
                insertAfter(if (it < input.length) Character.getNumericValue(input[it]) else it+1)
            }
            current.next = first
            current = first
        }

        // Insert after current; moves along afterwards
        fun insertAfter(value: Int) {
            current.next = Cup(value, current, current.next)
            current.next!!.next!!.prev = current
            next()
        }
        fun next() : Int {
            val result = current.value
            current = current.next!!
            return result
        }
        fun pop() : Int {
            val result = next()
            current.prev = current.prev!!.prev
            current.prev!!.prev!!.next = current
            return result
        }
        fun step(steps: Int) {
            when (steps.sign) {
                -1 -> (steps..-1).forEach { _ -> current = current.prev!! }
                +1 -> (+1..steps).forEach { _ -> current = current.next!! }
                else -> {}
            }
        }
        // Such that next() would return the desired value
        fun find(value: Int) { while (current.value != value) current = current.next!! }

    }

    class Cup(val value: Int, var prev: Cup? = null, var next: Cup? = null)

    fun solvePart2(input: String) : Long {
        // A million cups
        val cups = CupCircle(input, 1000000)
        (1..10000000).forEach {
            println(it)
            // Destination is current-1
            var destination = cups.next() - 1
            // Take the 3 clockwise cups
            val pickup = (1..3).map { cups.pop() }
            // Make sure the destination is available
            while (pickup.contains(destination) || destination < 1) {
                destination--
                if (destination < 1) destination = cups.size - input.length
            }
            // Seek to the destination
            cups.find(destination)
            // Insert picked up cups, then fix the current position
            pickup.forEach { cups.insertAfter(it) }
            cups.step(-3)
        }
        cups.find(1)
        cups.step(1)
        return cups.next().toLong() * cups.next()
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day23()
            val input = "963275481"
            println("Part1: " + solver.solvePart1(input))
            println("Part2: " + solver.solvePart2(input))
        }
    }

}