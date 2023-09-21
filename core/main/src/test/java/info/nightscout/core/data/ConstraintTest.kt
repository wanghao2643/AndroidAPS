package info.nightscout.core.data

import app.aaps.shared.tests.TestBase
import com.google.common.truth.Truth.assertThat
import info.nightscout.core.constraints.ConstraintObject
import org.junit.jupiter.api.Test

/**
 * Created by mike on 19.03.2018.
 */
class ConstraintTest : TestBase() {

    @Test fun doTests() {
        val b = ConstraintObject(true, aapsLogger)
        assertThat(b.value()).isTrue()
        assertThat(b.getReasons()).isEmpty()
        assertThat(b.getMostLimitedReasons()).isEmpty()
        b.set(false)
        assertThat(b.value()).isFalse()
        assertThat(b.getReasons()).isEmpty()
        assertThat(b.getMostLimitedReasons()).isEmpty()
        b.set(true, "Set true", this)
        assertThat(b.value()).isTrue()
        assertThat(b.getReasons()).isEqualTo("ConstraintTest: Set true")
        assertThat(b.getMostLimitedReasons()).isEqualTo("ConstraintTest: Set true")
        b.set(false, "Set false", this)
        assertThat(b.value()).isFalse()
        assertThat(b.getReasons()).isEqualTo("ConstraintTest: Set true\nConstraintTest: Set false")
        assertThat(b.getMostLimitedReasons()).isEqualTo("ConstraintTest: Set true\nConstraintTest: Set false")
        val d = ConstraintObject(10.0, aapsLogger)
        d.set(5.0, "Set 5d", this)
        assertThat(d.value()).isWithin(0.01).of(5.0)
        assertThat(d.getReasons()).isEqualTo("ConstraintTest: Set 5d")
        assertThat(d.getMostLimitedReasons()).isEqualTo("ConstraintTest: Set 5d")
        d.setIfSmaller(6.0, "Set 6d", this)
        assertThat(d.value()).isWithin(0.01).of(5.0)
        assertThat(d.getReasons()).isEqualTo("ConstraintTest: Set 5d\nConstraintTest: Set 6d")
        assertThat(d.getMostLimitedReasons()).isEqualTo("ConstraintTest: Set 5d")
        d.setIfSmaller(4.0, "Set 4d", this)
        assertThat(d.value()).isWithin(0.01).of(4.0)
        assertThat(d.getReasons()).isEqualTo("ConstraintTest: Set 5d\nConstraintTest: Set 6d\nConstraintTest: Set 4d")
        assertThat(d.getMostLimitedReasons()).isEqualTo("ConstraintTest: Set 4d")
        assertThat(d.originalValue()).isWithin(0.01).of(10.0)
        d.setIfDifferent(7.0, "Set 7d", this)
        assertThat(d.value()).isWithin(0.01).of(7.0)
        assertThat(d.getReasons()).isEqualTo("ConstraintTest: Set 5d\nConstraintTest: Set 6d\nConstraintTest: Set 4d\nConstraintTest: Set 7d")
        assertThat(d.getMostLimitedReasons()).isEqualTo("ConstraintTest: Set 4d\nConstraintTest: Set 7d")
        assertThat(d.originalValue()).isWithin(0.01).of(10.0)
    }
}
