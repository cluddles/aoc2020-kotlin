package aoc2020

import java.io.File
import kotlin.collections.ArrayList

class Day19 {

    data class Rule(val patterns: Set<String>)

    fun parse(src: File) : Pair<MutableMap<String, Rule>, List<String>> {
        val lines = src.readLines()
        val rulebook = lines.filter { it.contains(": ") }
            .map {
                it.split(": ").let { parts ->
                    Pair(parts[0], Rule(parts[1].split(" | ").toSet()))
                }
            }.toMap().toMutableMap()
        val input = lines.filter { it.isNotBlank() && !it.contains(": ") }
        return Pair(rulebook, input)
    }

    // Some dumb solution where we just expand out EVERYTHING
    // Sort of works for part 1, but is horrible
    // Potentially useful for displaying what rules end up looking like
    fun expandRulebook(rulebook: Map<String, Rule>) : Map<String, Rule> {
        val simplifiedRules = rulebook.entries
            .filter { it.value.patterns.all { p -> p.startsWith("\"") } }
            .map { Pair(it.key, Rule(it.value.patterns.map { p -> stripQuotes(p) }.toSet()) ) }
            .toMap().toMutableMap()

        while (simplifiedRules.size < rulebook.size) {
            val newlySimplified = rulebook.entries
                .filter { !simplifiedRules.contains(it.key) && canExpand(simplifiedRules, it.value) }
                .map { Pair(it.key, expand(simplifiedRules, it.value)) }
                .toMap()
            simplifiedRules.putAll(newlySimplified)
        }

        return simplifiedRules
    }

    fun stripQuotes(value: String) : String = value.substring(1 until value.length - 1)

    fun canExpand(rulebook: Map<String, Rule>, rule: Rule) =
        rule.patterns
            .all { p -> p.split(" ")
                .all { rulebook.contains(it) }
            }

    fun expand(rulebook: Map<String, Rule>, rule: Rule) : Rule =
        Rule(rule.patterns.flatMap { expandPattern(rulebook, it) }.toSet())

    fun expandPattern(rulebook: Map<String, Rule>, pattern: String) : List<String> {
        var result = arrayListOf("")
        for (i in pattern.split(" ")) {
            val newResult = ArrayList<String>()
            for (r in result) {
                for (j in rulebook[i]!!.patterns) {
                    // println("$i -> $j, $r")
                    newResult.add(r + j)
                }
            }
            result = newResult
        }
        return result
    }

    fun matchExpanded(rulebook: Map<String, Rule>, input: String) = rulebook["0"]!!.patterns.contains(input)

    fun matchInner(
        rulebook: Map<String, Rule>,
        rule: Rule,
        expr: String, pos: Int,
        ruleIndex: String,
        depth: Int
    ) : Int {
        println(" ".repeat(pos) + expr.substring(pos) + " -> $ruleIndex:$rule")

        return when {
            pos >= expr.length -> -1
            // This doesn't work for part 2 - forces OR to consume as much as possible, might not want it to
            rule.patterns.size > 1 -> {
                rule.patterns.maxOf { matchInner(rulebook, Rule(setOf(it)), expr, pos, ruleIndex, depth) }
            }
            rule.patterns.first().startsWith("\"") -> {
                if (pos < expr.length && expr.substring(pos, pos + 1) == rule.patterns.first()[1].toString()) {
                    pos + 1
                } else {
                    -1
                }
            }
            else -> {
                var p = pos
                for (r in rule.patterns.first().split(" ")) {
                    p = if (depth <= 0 && r == ruleIndex) {
                        -1
                    } else {
                        matchInner(rulebook, rulebook[r]!!, expr, p, r, if (r == ruleIndex) depth - 1 else depth)
                    }
                    if (p == -1) break
                }
                p
            }
        }
    }

    fun match(rulebook: Map<String, Rule>, input: String, recursionHack: Boolean) : Boolean {
        return if (!recursionHack) {
            matchInner(rulebook, rulebook["0"]!!, input, 0, "0", 1) == input.length
        } else {
            println(input)
            (0..9).any { matchInner(rulebook, rulebook["0"]!!, input, 0, "0", it) == input.length }
        }
    }

    fun solvePart1(src: File) : Int {
        val (rulebook, lines) = parse(src)
        return lines.count { match(rulebook, it, false) }
    }

    fun solvePart2(src: File) : Int {
        val (rulebook, lines) = parse(src)
        // For sample.2
        //   31: bbaba | bbbaa | babab | babaa | babba | baaba | baaab | ababb | abaab | abbab | abaaa | abbaa | abbba | aabab | aabaa | aabba
        //   42: babbb | baabb | bbaab | bbabb | bbbab | bbbbb | abbbb | aabbb | aaaab | aaabb | aaaba | ababa | bbbba | aaaaa | baaaa | bbaaa
        //
        // For input
        //   31: aaaaabba | aaaaabaa | aaaaaabb | aaaaaaab | aaaabbbb | aaaabbba | aaaabaab | aaababaa | aaabbbaa | aaabbaaa | aaabbbab | aaabbaab | aaababab | aaabaaab | aaabaabb | aababbaa | aabababa | aabaabba | aabbbaba | aabbbbbb | aabbbaab | aabbabbb | aabbabab | aabbaaab | aababaab | aabaaaab | aabaabab | aababbbb | aabaaabb | ababbabb | abaaaabb | abbbbabb | abbbaabb | abbaaabb | abbbbbbb | abbabbbb | abaabbbb | abaaabbb | abbabbab | abbaabab | abaabbab | abaaabab | abbabaab | abbaaaab | abaaaaab | abbabaaa | abbaabaa | ababbaaa | abababaa | abaabbaa | ababbbba | abababba | abaabbba | abaaabba | abbbbbba | abbbabba | abbabbba | abbbbaba | abaababa | abbbaaba | abbaaaba | ababaaba | bbbbbbbb | bbbbabbb | bbbbaabb | bbbbbbab | bbbbabab | bbbaabab | bbbbbaab | bbbbaaab | bbbabaab | bbbaaaab | bbabbbab | bbabaaab | bbaabbab | bbaabaab | bbabbbbb | bbabbabb | bbabaabb | bbaabbbb | bbaaabbb | bbaaaabb | bbbbabba | bbbaabba | bbaaabba | bbbabbba | bbbbbaba | bbbbaaba | bbbaaaba | bbabaaba | bbabbaaa | bbababaa | bbaabbaa | bbaabaaa | bbaaabaa | bbbbabaa | bbbaaaaa | babbbabb | babbaabb | baabbabb | baabaabb | baaababb | baaaaabb | bababbbb | baabbbbb | baaaabbb | bababbab | bababaab | baabbbab | baaaabab | baabbaab | baabaaab | baaabaab | babbbbaa | babbabaa | bababaaa | babababa | babbbbba | babaabba | baaabbba | baaabbaa | baaababa | baaabaaa | baaaabba | baaaabaa | baaaaaba | baababaa | baabbbba
        //   42: babbbaaa | babbaaaa | babaaaaa | bababbaa | babaabaa | baabbaaa | baabaaaa | baaaaaaa | baabbbaa | bbabaaaa | bbaaaaaa | bbabbbaa | bbbbbbaa | bbbbbaaa | bbbbaaaa | bbbabbaa | bbbabaaa | bbbaabaa | abbbabaa | abaaabaa | aabbabaa | aabaabaa | aabbbbaa | aaaabbaa | abbbbbaa | abbabbaa | ababbbaa | abbbaaaa | abbaaaaa | ababaaaa | abaaaaaa | aabbaaaa | aabaaaaa | aaabaaaa | aaaaaaaa | abbbbaaa | abaabaaa | aabbbaaa | aababaaa | aaaabaaa | aabbaaba | aabaaaba | aaabaaba | aaaaaaba | aaabbaba | aaaababa | abbababa | ababbaba | abaaaaba | abbaabba | aabbabba | aaababba | aababbba | aabbbbba | aaabbbba | babbabba | bababbba | baababba | bbbbbbba | bbabbbba | bbababba | bbaabbba | bbbababa | bbaababa | bbabbaba | baabbaba | babbbaba | bbaaaaba | babaaaba | babbaaba | baabaaba | aabaabbb | aaaaabbb | aaababbb | abbbabbb | abbaabbb | abababbb | ababbbbb | aaabbbbb | abbababb | abaababb | aabababb | aaaababb | aabbbabb | aaabbabb | aabbaabb | ababaabb | abbbbaab | ababbaab | abaabaab | abbbaaab | ababaaab | aabbbbab | aababbab | aaaabbab | aaaaabab | abbbbbab | abbbabab | ababbbab | abababab | baaabbbb | baababbb | baababab | baaabbab | baaaaaab | babbbbab | babbbaab | babbabab | babbaaab | babbbbbb | babbabbb | babababb | babaaabb | babaabbb | babaabab | babaaaab | bbbaabbb | bbbaaabb | bbbabbbb | bbbabbab | bbbababb | bbbbbabb | bbaaabab | bbaaaaab | bbaababb | bbababbb | bbabbaab | bbababab

        // Modify the rulebook
        rulebook["8"] = Rule(setOf("42", "42 8"))
        rulebook["11"] = Rule(setOf("42 31", "42 11 31"))
        val result = lines.filter { match(rulebook, it, true) }
        result.forEach { println(it) }
        return result.size
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