package cv.domain.mappers

import cv.domain.entities.MemberEntity
import cv.domain.entities.WeightEntity
import cv.domain.enums.DateFormatType
import cv.domain.presentationModels.MemberDashboardPresentationModel
import cv.domain.presentationModels.MemberEditPresentationModel
import cv.domain.presentationModels.MemberPresentationModel
import cv.domain.repositories.DateProviderRepository
import cv.domain.usecase.NumberFormatUseCase

interface MemberPresentationMapper {
    fun getModel(entity: MemberEntity): MemberPresentationModel

    fun getEditModel(entity: MemberEntity): MemberEditPresentationModel

    fun getDashboardModel(
        entity: MemberEntity,
        trackedWeights: List<WeightEntity>,
        workouts: Int = 0,
    ): MemberDashboardPresentationModel
}

class MemberPresentationMapperImp(
    private val dateProviderRepository: DateProviderRepository,
    private val numberFormatUseCase: NumberFormatUseCase,
) : MemberPresentationMapper {
    override fun getModel(entity: MemberEntity): MemberPresentationModel {
        val membershipRenewalPair = dateProviderRepository.toPaymentPlanDuration(entity.renewalFutureDateMillis)
        return MemberPresentationModel(
            id = entity.id,
            fullName = "${entity.firstName} ${entity.lastName}",
            email = entity.email,
            profileImageUrl = entity.profileImageUrl,
            hasCoach = entity.hasCoach,
            isActive = entity.activeNowDateMillis != null,
            lastActive =
                if (entity.activeNowDateMillis == null) {
                    ""
                } else {
                    dateProviderRepository.activeStatusDuration(
                        entity.activeNowDateMillis,
                        dateProviderRepository.getDate().time,
                    )
                },
            activeNowDateMillis = entity.activeNowDateMillis,
            amountDue = numberFormatUseCase.getCurrencyFormatted(entity.amountDue),
            phoneNumber = entity.phoneNumber,
            memberType = entity.memberType.name,
            emailVerified = entity.emailVerified,
            hasPaidMembership = entity.renewalFutureDateMillis != null,
            residentialStatus =
                if (entity.apartmentNumber.isEmpty()) {
                    "Guest"
                } else {
                    "Apartment ${entity.apartmentNumber}"
                },
            membershipRenewalDate = membershipRenewalPair.first,
            membershipWarningLevel = membershipRenewalPair.second,
        )
    }

    override fun getEditModel(entity: MemberEntity) =
        MemberEditPresentationModel(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            email = entity.email,
            profileImageUrl = entity.profileImageUrl,
            hasCoach = entity.hasCoach,
            phoneNumber = entity.phoneNumber,
            memberType = entity.memberType.name,
            apartmentNumber = entity.apartmentNumber,
            registrationDate = dateProviderRepository.format(entity.registrationDateMillis, DateFormatType.DAY_MONTH_YEAR),
            height = if (entity.height == 0.0) "" else numberFormatUseCase.getHeight(entity.height).toString(),
            heightUnit = numberFormatUseCase.getHeightUnit(),
        )

    override fun getDashboardModel(
        entity: MemberEntity,
        trackedWeights: List<WeightEntity>,
        workouts: Int,
    ) = MemberDashboardPresentationModel(
        id = entity.id,
        fullName = "${entity.firstName} ${entity.lastName}",
        profileImageUrl = entity.profileImageUrl,
        hasCoach = entity.hasCoach,
        isActive = entity.activeNowDateMillis != null,
        lastActive =
            if (entity.activeNowDateMillis == null) {
                ""
            } else {
                dateProviderRepository.activeStatusDuration(
                    entity.activeNowDateMillis,
                    dateProviderRepository.getDate().time,
                )
            },
        activeNowDateMillis = entity.activeNowDateMillis,
        amountDue = numberFormatUseCase.getCurrencyFormatted(entity.amountDue),
        hasPaidMembership = entity.renewalFutureDateMillis != null,
        registrationDate = dateProviderRepository.format(entity.registrationDateMillis, DateFormatType.DAY_MONTH_YEAR),
        weight = if (trackedWeights.isEmpty()) "-" else numberFormatUseCase.getWeight(trackedWeights.first().weight).toString(),
        weightUnit = numberFormatUseCase.getWeightUnit(),
        height = numberFormatUseCase.getHeight(entity.height).toString(),
        heightUnit = numberFormatUseCase.getHeightUnit(),
        bmiValue = if (trackedWeights.isEmpty()) 0.0 else numberFormatUseCase.getBMI(trackedWeights.first(), entity.height),
        workouts = workouts.toString(),
    )
}
