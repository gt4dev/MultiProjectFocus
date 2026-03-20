package gtr.mpfocus.ui.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.*
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class OptionsSplitButtonTest {

    private val sampleOptions = listOf(
        "option idx 0",
        "option idx 1",
        "option idx 2",
        "option idx 3",
        "option idx 4",
    )

    @Test
    fun openComposable_showsLeadingButtonAndDropDownList() = runComposeUiTest {
        setContent {
            MaterialTheme {
                OptionsSplitButton(
                    options = sampleOptions,
                    currentOptionIdx = 3,
                    onOptionClicked = {},
                )
            }
        }

        onNodeWithText("Open option idx 3").assertIsDisplayed()
        onNodeWithTag(OptionsSplitButtonTestTags.TRAILING_BUTTON).performClick()

        waitForIdle()

        onAllNodesWithTag(OptionsSplitButtonTestTags.DROPDOWN_ROW).assertCountEquals(5)
    }

    @Test
    fun clickLeadingButton_invokesOnOptionClicked() = runComposeUiTest {
        var clickedOptionIdx: Int? = null

        setContent {
            MaterialTheme {
                OptionsSplitButton(
                    options = sampleOptions,
                    currentOptionIdx = 3,
                    onOptionClicked = { selectedOptionIdx ->
                        clickedOptionIdx = selectedOptionIdx
                    },
                )
            }
        }

        onNodeWithText("Open option idx 3").assertIsDisplayed()
        onNodeWithTag(OptionsSplitButtonTestTags.LEADING_BUTTON).performClick()

        waitForIdle()

        assertEquals(3, clickedOptionIdx)
    }

    @Test
    fun clickTrailingButtonAndClickOptionInDropDown_invokesOnOptionClicked() = runComposeUiTest {
        var clickedOptionIdx: Int? = null

        setContent {
            MaterialTheme {
                OptionsSplitButton(
                    options = sampleOptions,
                    currentOptionIdx = 3,
                    onOptionClicked = { selectedOptionIdx ->
                        clickedOptionIdx = selectedOptionIdx
                    },
                )
            }
        }

        onNodeWithTag(OptionsSplitButtonTestTags.TRAILING_BUTTON).performClick()
        onNodeWithText("option idx 4").assertIsDisplayed()
        onNodeWithText("option idx 4").performClick()

        waitForIdle()

        assertEquals(4, clickedOptionIdx)
    }
}
