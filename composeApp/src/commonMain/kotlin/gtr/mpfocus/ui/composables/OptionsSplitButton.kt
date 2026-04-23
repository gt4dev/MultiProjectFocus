@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package gtr.mpfocus.ui.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag

object OptionsSplitButtonTestTags {
    const val LEADING_BUTTON = "options_split_button_leading_button"
    const val TRAILING_BUTTON = "options_split_button_trailing_button"
    const val DROPDOWN_ROW = "options_split_button_dropdown_row"
}

@Composable
fun OptionsSplitButton(
    options: List<String>,
    currentOptionIdx: Int?,
    onOptionClicked: (optionIdx: Int?) -> Unit,
    leadingButtonText: String,
    leadingButtonDescription: String? = null,
    trailingButtonDescription: String? = null,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val safeSelection = currentOptionIdx?.takeIf { it in options.indices }

    OptionsSplitButtonScaffold(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onLeadingClick = { onOptionClicked(safeSelection) },
        leadingButtonDescription = leadingButtonDescription,
        trailingButtonDescription = trailingButtonDescription,
        leadingButtonModifier = Modifier.testTag(OptionsSplitButtonTestTags.LEADING_BUTTON),
        trailingButtonModifier = Modifier.testTag(OptionsSplitButtonTestTags.TRAILING_BUTTON),
        leadingButtonContent = { Text(leadingButtonText) },
        dropdownContent = {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    modifier = Modifier.testTag(OptionsSplitButtonTestTags.DROPDOWN_ROW),
                    text = { Text(option) },
                    onClick = {
                        onOptionClicked(index)
                        expanded = false
                    },
                )
            }
        },
    )
}

@Composable
private fun OptionsSplitButtonScaffold(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onLeadingClick: () -> Unit,
    leadingButtonDescription: String? = null,
    trailingButtonDescription: String? = null,
    leadingButtonContent: @Composable RowScope.() -> Unit,
    dropdownContent: @Composable ColumnScope.() -> Unit,
    leadingButtonModifier: Modifier = Modifier,
    trailingButtonModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        SplitButtonLayout(
            leadingButton = {
                SimpleTooltip(text = leadingButtonDescription) {
                    SplitButtonDefaults.OutlinedLeadingButton(
                        onClick = onLeadingClick,
                        modifier = leadingButtonModifier,
                    ) {
                        leadingButtonContent()
                    }
                }
            },
            trailingButton = {
                SimpleTooltip(text = trailingButtonDescription) {
                    SplitButtonDefaults.OutlinedTrailingButton(
                        checked = expanded,
                        onCheckedChange = onExpandedChange,
                        modifier = trailingButtonModifier
                    ) {
                        val rotation: Float by
                        animateFloatAsState(
                            targetValue = if (expanded) 180f else 0f,
                            label = "Trailing Icon Rotation",
                        )
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            modifier =
                                Modifier
                                    .size(SplitButtonDefaults.TrailingIconSize)
                                    .graphicsLayer {
                                        this.rotationZ = rotation
                                    },
                            contentDescription = null,
                        )
                    }
                }
            },
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
            dropdownContent()
        }
    }
}
