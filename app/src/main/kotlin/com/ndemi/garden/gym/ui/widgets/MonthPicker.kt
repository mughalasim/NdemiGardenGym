package com.ndemi.garden.gym.ui.widgets

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.icon_image_size_large
import com.ndemi.garden.gym.ui.theme.line_thickness_small
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.utils.AppPreview

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MonthPicker(
    visible: Boolean,
    currentMonth: Int,
    currentYear: Int,
    confirmButtonCLicked: (Int, Int) -> Unit,
    cancelClicked: () -> Unit
) {

    val months = listOf(
        "JAN",
        "FEB",
        "MAR",
        "APR",
        "MAY",
        "JUN",
        "JUL",
        "AUG",
        "SEP",
        "OCT",
        "NOV",
        "DEC"
    )

    var month by remember {
        mutableStateOf(months[currentMonth])
    }

    var year by remember {
        mutableIntStateOf(currentYear)
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    if (visible) {
        AlertDialog(
            backgroundColor = AppTheme.colors.backgroundButtonDisabled,
            shape = RoundedCornerShape(border_radius),
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(icon_image_size_large)
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource,
                                    onClick = {
                                        year--
                                    }
                                ),
                            tint = AppTheme.colors.highLight,
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                            contentDescription = null
                        )

                        TextRegular(
                            modifier = Modifier.padding(horizontal = padding_screen),
                            text = year.toString(),
                        )

                        Icon(
                            modifier = Modifier
                                .size(icon_image_size_large)
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource,
                                    onClick = {
                                        year++
                                    }
                                ),
                            tint = AppTheme.colors.highLight,
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }

                    Card(
                        modifier = Modifier
                            .padding(top = padding_screen)
                            .fillMaxWidth(),
                        backgroundColor = AppTheme.colors.backgroundButtonDisabled,
                        elevation = 0.dp
                    ) {
                        FlowRow(
                            horizontalArrangement = Arrangement.Center
                        ) {
                            months.forEach {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = interactionSource,
                                            onClick = { month = it }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {

                                    val animatedSize by animateDpAsState(
                                        targetValue = if (month == it) 60.dp else 0.dp,
                                        animationSpec = tween(
                                            durationMillis = 500,
                                            easing = LinearOutSlowInEasing
                                        ), label = ""
                                    )

                                    Box(
                                        modifier = Modifier
                                            .size(animatedSize)
                                            .background(
                                                color = if (month == it) {
                                                    AppTheme.colors.backgroundScreen
                                                } else {
                                                    Color.Transparent
                                                },
                                                shape = RoundedCornerShape(border_radius)
                                            )
                                    )

                                    TextSmall(
                                        text = it,
                                        color =
                                        if (month == it) AppTheme.colors.textPrimary else AppTheme.colors.highLight,
                                    )
                                }
                            }
                        }
                    }
                }
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = padding_screen, bottom = padding_screen),
                    horizontalArrangement = Arrangement.End
                ) {

                    OutlinedButton(
                        modifier = Modifier.padding(end = padding_screen),
                        onClick = {
                            cancelClicked()
                        },
                        shape = CircleShape,
                        border = BorderStroke(line_thickness_small, color = Color.Transparent),
                        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent)
                    ) {
                        TextRegular(text = "Cancel",)
                    }

                    OutlinedButton(
                        modifier = Modifier.padding(end = padding_screen),
                        onClick = {
                            confirmButtonCLicked(
                                months.indexOf(month) + 1,
                                year
                            )
                        },
                        shape = RoundedCornerShape(border_radius),
                        border = BorderStroke(line_thickness_small, color = AppTheme.colors.highLight),
                        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent)
                    ) {
                        TextRegular(
                            text = "Confirm",
                        )
                    }

                }
            },
            onDismissRequest = {}
        )
    }
}

@AppPreview
@Composable
fun MonthPickerPreview(){
    AppThemeComposable {
        MonthPicker(
            visible = true,
            currentMonth = 9,
            currentYear = 2024,
            confirmButtonCLicked = {_,_ -> },
            cancelClicked = {}
        )
    }
}
