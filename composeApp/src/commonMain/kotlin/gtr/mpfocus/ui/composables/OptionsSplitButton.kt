@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package gtr.mpfocus.ui.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription

object OptionsSplitButtonTestTags {
    private const val PREFIX = "options_split_button"
    const val LEADING_BUTTON = "${PREFIX}_leading_button"
    const val TRAILING_BUTTON = "${PREFIX}_trailing_button"
    const val DROPDOWN_ROW = "${PREFIX}_dropdown_row"
}

@Composable
fun OptionsSplitButton(
    options: List<String>,
    currentOptionIdx: Int?,
    onOptionClicked: (optionIdx: Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val safeSelection = currentOptionIdx?.takeIf { it in options.indices }
    val optionName = safeSelection?.let { options[it] } ?: "..."

    OptionsSplitButtonScaffold(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onLeadingClick = { onOptionClicked(safeSelection) },
        leadingButtonModifier = Modifier.testTag(OptionsSplitButtonTestTags.LEADING_BUTTON),
        trailingButtonModifier = Modifier.testTag(OptionsSplitButtonTestTags.TRAILING_BUTTON),
        leadingButtonContent = { Text("Open $optionName") },
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
    trailingButtonDescription: String = "Select option",
    leadingButtonContent: @Composable RowScope.() -> Unit,
    dropdownContent: @Composable ColumnScope.() -> Unit,
    leadingButtonModifier: Modifier = Modifier,
    trailingButtonModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        SplitButtonLayout(
            leadingButton = {
                SplitButtonDefaults.OutlinedLeadingButton(
                    onClick = onLeadingClick,
                    modifier = leadingButtonModifier,
                ) {
                    leadingButtonContent()
                }
            },
            trailingButton = {
                TooltipBox(
                    positionProvider =
                        TooltipDefaults.rememberTooltipPositionProvider(
                            TooltipAnchorPosition.Above,
                        ),
                    tooltip = { PlainTooltip { Text(trailingButtonDescription) } },
                    state = rememberTooltipState(),
                ) {
                    SplitButtonDefaults.OutlinedTrailingButton(
                        checked = expanded,
                        onCheckedChange = onExpandedChange,
                        modifier =
                            trailingButtonModifier.semantics {
                                stateDescription = if (expanded) "Expanded" else "Collapsed"
                                contentDescription = trailingButtonDescription
                            },
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
