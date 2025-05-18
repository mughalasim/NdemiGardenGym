package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.mock.getMockWeightEntity
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.DateConstants.formatDayMonthYear
import com.ndemi.garden.gym.ui.widgets.dialog.AlertDialogWidget
import cv.domain.entities.WeightEntity
import org.joda.time.DateTime

@Composable
fun WeightWidget(
    weightEntity: WeightEntity,
    onDeleteWeight: (WeightEntity) -> Unit = {},
) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier =
            Modifier
                .padding(top = padding_screen)
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row {
            TextWidget(
                text = DateTime(weightEntity.dateMillis).toString(formatDayMonthYear),
                color = AppTheme.colors.primary,
            )
            TextWidget(
                modifier = Modifier.padding(horizontal = padding_screen_small),
                text = weightEntity.weight,
            )
        }
        Icon(
            modifier = Modifier.clickable { showDialog = !showDialog },
            imageVector = Icons.Default.Close,
            tint = AppTheme.colors.error,
            contentDescription = stringResource(id = R.string.txt_delete),
        )
    }
    if (showDialog) {
        AlertDialogWidget(
            title = stringResource(R.string.txt_are_you_sure),
            message = stringResource(R.string.txt_are_you_sure_delete_weight),
            onDismissed = { showDialog = !showDialog },
            positiveButton = stringResource(R.string.txt_delete),
            positiveOnClick = {
                showDialog = !showDialog
                onDeleteWeight.invoke(weightEntity)
            },
            negativeButton = stringResource(R.string.txt_cancel),
            negativeOnClick = {
                showDialog = !showDialog
            },
        )
    }
}

@AppPreview
@Composable
private fun WeightWidgetPreview() =
    AppThemeComposable {
        Column {
            WeightWidget(weightEntity = getMockWeightEntity())
            WeightWidget(weightEntity = getMockWeightEntity())
        }
    }
