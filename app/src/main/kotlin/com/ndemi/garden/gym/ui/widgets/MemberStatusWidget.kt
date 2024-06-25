package com.ndemi.garden.gym.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ndemi.garden.gym.R
import com.ndemi.garden.gym.ui.theme.AppTheme
import com.ndemi.garden.gym.ui.theme.AppThemeComposable
import com.ndemi.garden.gym.ui.theme.border_radius
import com.ndemi.garden.gym.ui.theme.icon_image_size_large
import com.ndemi.garden.gym.ui.theme.icon_image_size_profile_small
import com.ndemi.garden.gym.ui.theme.line_thickness
import com.ndemi.garden.gym.ui.theme.padding_screen
import com.ndemi.garden.gym.ui.theme.padding_screen_small
import com.ndemi.garden.gym.ui.utils.AppPreview
import com.ndemi.garden.gym.ui.utils.toActiveStatusDuration
import com.ndemi.garden.gym.ui.utils.toDaysDuration
import com.ndemi.garden.gym.ui.utils.toMembershipStatusString
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(8f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(memberEntity.profileImageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_app_foreground),
                    fallback = painterResource(R.drawable.ic_app_foreground),
                    error = painterResource(R.drawable.ic_app_foreground),
                    contentDescription = "profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(end = padding_screen)
                        .clip(CircleShape)
                        .border(
                            border = BorderStroke(line_thickness, AppTheme.colors.highLight),
                            shape = CircleShape
                        )
                        .width(icon_image_size_profile_small)
                        .height(icon_image_size_profile_small)
                )

                TextRegularBold(
                    text = memberEntity.getFullName() + ": " + DateTime.now()
                        .toActiveStatusDuration(
                            startDate = DateTime(memberEntity.activeNowDate)
                        ),
                )
            }

            if (showDetails) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    if (memberEntity.isActiveNow()) {
                        Icon(
                            modifier = Modifier
                                .width(icon_image_size_large)
                                .height(icon_image_size_large),
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            tint = AppTheme.colors.highLight,
                        )
                    } else if (!memberEntity.hasPaidMembership()) {
                        Icon(
                            modifier = Modifier
                                .width(icon_image_size_large)
                                .height(icon_image_size_large),
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = null,
                            tint = AppTheme.colors.backgroundError,
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
                color = AppTheme.colors.highLight,
                text = memberEntity.getResidentialStatus(),
            )
            TextRegular(
                modifier = Modifier.padding(top = padding_screen_small),
                text = "Membership due date: "
                        + memberEntity.renewalFutureDate.toMembershipStatusString(),
            )
            if (memberEntity.hasPaidMembership()) {
                TextRegularBold(
                    modifier = Modifier.padding(top = padding_screen_small),
                    color = AppTheme.colors.highLight,
                    text = "Payment due: "
                            + DateTime(memberEntity.renewalFutureDate).toDaysDuration(),
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
