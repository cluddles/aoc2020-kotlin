package aoc2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day25Test {

    private val solution = Day25()

    @Test fun solvePart1_sampleInput() {
        assertThat(solution.solvePart1(File("src/test/resources/aoc2020/day25.sample"))).isEqualTo(14897079L)
    }

}