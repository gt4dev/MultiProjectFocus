package gtr.mpfocus.hotest

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import dev.hotest.hotest
import dev.hotest.variants.variant
import dev.hotest.variants.variants
import kotlin.test.Test
import kotlin.test.assertEquals


// todo: move to HOTest project


// todo: instead of 'runComposeUiTest' introduce 'own lambda', similar to 'runComposeUiTest'
@OptIn(ExperimentalTestApi::class)
class VariantsNestedInLambdaTests {

    @Test
    fun `check variants nested in lambda`() {
        val stamps = mutableListOf<String>()
        hotest {
            stamps.add("start")
            runComposeUiTest {
                variants {
                    stamps.add("vs A")

                    variant {
                        stamps.add("v 1")
                    }

                    variant {
                        stamps.add("v 2")
                    }
                }
            }
            stamps.add("end")
        }

        val expected = listOf(
            // loop 1
            "start", "vs A", "v 1", "end",
            // loop 2
            "start", "vs A", "v 2", "end",
        )

        assertEquals(expected, stamps)
    }

}