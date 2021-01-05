package aoc2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day20Test {

    private val solution = Day20()
    private val tile = Day20.Tile(123, "00001010", "00110100", "11000000", "00101000")

    @Test fun rot1() {
        assertThat(solution.rot(tile, 1)).isEqualTo(
            Day20.Tile(123, "00010100", "00001010", "00101100", "11000000")
        )
    }

    @Test fun rot2() {
        assertThat(solution.rot(tile, 2)).isEqualTo(
            Day20.Tile(123, "00000011", "00010100", "01010000", "00101100")
        )
    }

    @Test fun rot3() {
        assertThat(solution.rot(tile, 3)).isEqualTo(
            Day20.Tile(123, "00110100", "00000011", "00101000", "01010000")
        )
    }

    @Test fun flipX() {
        assertThat(solution.flipX(tile)).isEqualTo(
            Day20.Tile(123, "01010000", "00101000", "00000011", "00110100")
        )
    }

    @Test fun flipY() {
        assertThat(solution.flipY(tile)).isEqualTo(
            Day20.Tile(123, "11000000", "00101100", "00001010", "00010100")
        )
    }

    @Test fun solvePart1_sampleInput() {
        assertThat(solution.solvePart1(File("src/test/resources/aoc2020/day20.sample"))).isEqualTo(20899048083289L)
    }

}