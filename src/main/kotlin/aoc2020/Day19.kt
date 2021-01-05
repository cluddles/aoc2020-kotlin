package aoc2020

import java.io.File
import kotlin.collections.ArrayList

typealias Rulebook = Map<String, Rule>
typealias Rule = Set<String>

class Day19 {

    // Returns (mutable) rulebook and list of input strings
    fun parse(src: File) : Pair<MutableMap<String, Rule>, List<String>> {
        val lines = src.readLines()
        val rulebook = lines.filter { it.contains(": ") }
            .map {
                it.split(": ").let { parts ->
                    Pair(parts[0], parts[1].split(" | ").toSet())
                }
            }.toMap().toMutableMap()
        val input = lines.filter { it.isNotBlank() && !it.contains(": ") }
        return Pair(rulebook, input)
    }

    // Basically the same as my first attempt, except now it returns all possible endpoints for the match
    // instead of just the one with the highest value
    fun matchInner(rulebook: Rulebook, rule: Rule, expr: String, pos: Int) : List<Int> =
        when {
            pos >= expr.length -> emptyList()
            rule.size > 1 -> {
                rule.flatMap { matchInner(rulebook, setOf(it), expr, pos) }
            }
            rule.first().startsWith("\"") -> {
                if (pos < expr.length && expr.substring(pos, pos + 1) == rule.first()[1].toString()) {
                    listOf(pos + 1)
                } else {
                    emptyList()
                }
            }
            else -> {
                // Test all potential match starts against each rule
                var pl = listOf(pos)
                for (r in rule.first().split(" ")) {
                    val pl2 = ArrayList<Int>()
                    for (p in pl) {
                        pl2.addAll(matchInner(rulebook, rulebook[r]!!, expr, p))
                    }
                    pl = pl2
                    if (pl.isEmpty()) break
                }
                pl
            }
        }

    fun match(rulebook: Rulebook, input: String) : Boolean =
        matchInner(rulebook, rulebook["0"]!!, input, 0).any { it == input.length }

    fun solvePart1(src: File) : Int {
        val (rulebook, lines) = parse(src)
        return lines.count { match(rulebook, it) }
    }

    fun solvePart2(src: File) : Int {
        val (rulebook, lines) = parse(src)
        // Modify the rulebook
        rulebook["8"] = setOf("42", "42 8")
        rulebook["11"] = setOf("42 31", "42 11 31")
        return lines.count { match(rulebook, it) }
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day19()
            val file = File("src/main/resources/aoc2020/day19.input")
            println("Part1: " + solver.solvePart1(file))
            println("Part2: " + solver.solvePart2(file))
        }
    }

}