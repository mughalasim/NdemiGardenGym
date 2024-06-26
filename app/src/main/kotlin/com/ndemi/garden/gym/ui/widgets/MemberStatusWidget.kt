package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Accessibility
import androidx.compose.material.icons.rounded.RunCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.icon_image_size_large
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toActiveStatusDuration
import com.ndemi.garden.gym.ui.utils.toDaysDuration
import cv.domain.entities.MemberEntity
import cv.domain.entities.getMockMemberEntity
import org.joda.time.DateTime

@Suppress("detekt.MagicNumber")
@Composable
fun MemberStatusWidget(
    modifier: Modifier = Modifier,
    memberEntity: MemberEntity,
    showDetails: Boolean = false,
    onMemberTapped: (memberEntity: MemberEntity) -> Unit = {},
) {
    Column(
        modifier =
        modifier
            .padding(bottom = padding_screen_small)
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = line_thickness,
                color = AppTheme.colors.backgroundChip,
                shape = RoundedCornerShape(border_radius),
            )
            .padding(padding_screen)
            .clickable { if (showDetails) onMemberTapped.invoke(memberEntity) },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImageWidget(
                profileImageUrl = memberEntity.profileImageUrl,
                isLarge = false
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = padding_screen)
            ) {
                TextRegularBold(
                    text = memberEntity.getFullName(),
                )

                TextSmall(
                    text = DateTime.now().toActiveStatusDuration(
                        startDate = DateTime(memberEntity.activeNowDate)
                    )
                )
            }

            if (showDetails) {
                Column {
                    if (memberEntity.isActiveNow()) {
                        Icon(
                            modifier = Modifier
                                .width(icon_image_size_large)
                                .height(icon_image_size_large),
                            imageVector = Icons.Rounded.RunCircle,
                            contentDescription = null,
                            tint = AppTheme.colors.highLight,
                        )
                    }

                    if (memberEntity.hasCoach) {
                        Icon(
                            modifier = Modifier
                                .width(icon_image_size_large)
                                .height(icon_image_size_large),
                            imageVector = Icons.Rounded.Accessibility,
                            contentDescription = null,
                            tint = AppTheme.colors.highLight,
                        )
                        TextSmall(
                            text = "Coach",
                            color = AppTheme.colors.highLight
                        )
                    }
                }
            }
        }


        if (showDetails) {
            TextRegular(
                modifier = Modifier.padding(top = padding_screen_small),
                text = "Email: " + memberEntity.email,
            )
            TextRegularBold(
                modifier = Modifier.padding(top = padding_screen_small),
                color = if (memberEntity.apartmentNumber.isNullOrEmpty()) {
                    AppTheme.colors.backgroundError
                } else {
                    AppTheme.colors.highLight
                },
                text = memberEntity.getResidentialStatus(),
            )
            if (memberEntity.hasPaidMembership()) {
                TextRegularBold(
                    modifier = Modifier.padding(top = padding_screen_small),
                    text = "Payment due: "
                            + DateTime(memberEntity.renewalFutureDate).toDaysDuration(),
                )
            } else {
                TextRegularBold(
                    modifier = Modifier.padding(top = padding_screen_small),
                    text = "Membership Expired",
                    color = AppTheme.colors.backgroundError
                )
            }
        }
    }
}

@AppPreview
@Composable
fun MemberWidgetPreview() {
    AppThemeComposable {
        Column {
            MemberStatusWidget(memberEntity = getMockMemberEntity(), showDetails = true)
            MemberStatusWidget(memberEntity = getMockMemberEntity(), showDetails = false)
        }
    }
}
