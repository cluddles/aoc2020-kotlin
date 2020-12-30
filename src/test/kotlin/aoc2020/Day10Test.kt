package aoc2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day10Test {

    private val solution = Day10()

    @Test fun solvePart1_sampleInput1() {
        assertThat(solution.solvePart1(File("src/test/resources/aoc2020/day10.sample.1"))).isEqualTo(7*5)
    }
    @Test fun solvePart1_sampleInput2() {
        assertThat(solution.solvePart1(File("src/test/resources/aoc2020/day10.sample.2"))).isEqualTo(22*10)
    }

    @Test fun solvePart2_sampleInput1() {
        assertThat(solution.solvePart2(File("src/test/resources/aoc2020/day10.sample.1"))).isEqualTo(8)
    }
    @Test fun solvePart2_sampleInput2() {
        assertThat(solution.solvePart2(File("src/test/resources/aoc2020/day10.sample.2"))).isEqualTo(19208)
    }

}