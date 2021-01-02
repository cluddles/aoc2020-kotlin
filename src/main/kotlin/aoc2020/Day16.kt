package aoc2020

import java.io.File

class Day16 {

    data class Rule(val name: String) {
        // Don't want hash etc to use these
        val ranges: MutableList<IntRange> = ArrayList()
        val possibleIndexes: HashSet<Int> = HashSet()
        override fun toString(): String {
            return "Rule(name=$name, ranges=$ranges, possibleIndexes=$possibleIndexes)"
        }
    }
    data class Ticket(val values: List<Int>)
    data class Notes(val rules: List<Rule>, val tickets: List<Ticket>)

    private fun parseNotes(src: File) : Notes {
        val lines = src.readLines()
        val emptyLines = lines.mapIndexedNotNull { i, line -> if (line.isBlank()) i else null }
        return Notes(
            // Rules
            lines.slice(0 until emptyLines.first())
                .map { line ->
                    line.split(": ").let { sides ->
                        val result = Rule(sides[0])
                        result.ranges.addAll(
                            sides[1].split(" or ").map { range ->
                                range.split("-").let { bounds ->
                                    IntRange(bounds[0].toInt(), bounds[1].toInt())
                                }
                            }
                        )
                        result
                    }
                },
            // Tickets
            lines.slice(emptyLines.first() + 2 until lines.size)
                .filter { line -> line.contains(",") }
                .map { line -> Ticket(line.split(",").map { it.toInt() } ) }
        )
    }

    private fun matchesRule(value: Int, rule: Rule) : Boolean = rule.ranges.any { it.contains(value) }

    fun solvePart1(src: File) : Int {
        val notes = parseNotes(src)
        // Sum of all invalid values (those that can't match any rule)
        return notes.tickets.drop(1)
            .flatMap { ticket -> ticket.values }
            .map { value -> if (notes.rules.none { rule -> matchesRule(value, rule) } ) value else 0 }
            .sum()
    }

    fun parseNotesAndIndex(src: File) : Notes {
        val notes = parseNotes(src)
        notes.rules.forEach { rule -> rule.possibleIndexes.addAll(notes.tickets[0].values.indices) }
        // Phase 1 - strip invalid tickets
        val validTickets = notes.tickets.filter { ticket ->
            ticket.values.all { value ->
                notes.rules.any { rule -> matchesRule(value, rule) }
            }
        }
        // Phase 2 - use remaining (valid) tickets to determine possible indexes for each rule
        validTickets.forEach { ticket ->
            ticket.values.forEachIndexed { i, value ->
                notes.rules
                    .filter { rule -> rule.possibleIndexes.contains(i) && !matchesRule(value, rule) }
                    .forEach { rule -> rule.possibleIndexes.remove(i) }
            }
        }
        // Phase 3 - reduce rule index mappings based on other rules needing the same index
        val processed = HashSet<Rule>()
        while (processed.size != notes.rules.size) {
            val toProcess = notes.rules.filter { !processed.contains(it) && it.possibleIndexes.size <= 1 }
            if (toProcess.isEmpty()) throw IllegalStateException("Could not reduce rule index mappings")
            toProcess.forEach { procRule ->
                notes.rules.filter { it != procRule }.forEach { it.possibleIndexes.removeAll(procRule.possibleIndexes) }
            }
            processed.addAll(toProcess)
        }
        return notes;
    }

    fun solvePart2(src: File) : Long {
        val notes = parseNotesAndIndex(src)
        // Multiply all values for rules with name starting "departure"
        return notes.rules
            .filter { it.name.startsWith("departure") }
            .map { notes.tickets.first().values[it.possibleIndexes.first()] }
            .fold(1L, { a, b -> a * b } )
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day16()
            val file = File("src/main/resources/aoc2020/day16.input")
            println("Part1: " + solver.solvePart1(file))
            println("Part2: " + solver.solvePart2(file))
        }
    }

}