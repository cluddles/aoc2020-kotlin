package aoc2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day22Test {

    private val solution = Day22()

    @Test fun solvePart1_sampleInput() {
        assertThat(solution.solvePart1(File("src/test/resources/aoc2020/day22.sample"))).isEqualTo(306)
    }

}