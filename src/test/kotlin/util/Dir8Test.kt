package util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Dir8Test {

    @Test fun rotate_negative360() {
        assertThat(Dir8.N.rotate(-8)).isEqualTo(Dir8.N)
    }
    @Test fun rotate_negative90() {
        assertThat(Dir8.N.rotate(-2)).isEqualTo(Dir8.W)
    }

    @Test fun rotate_positive360() {
        assertThat(Dir8.N.rotate(8)).isEqualTo(Dir8.N)
    }
    @Test fun rotate_positive90() {
        assertThat(Dir8.N.rotate(2)).isEqualTo(Dir8.E)
    }

}