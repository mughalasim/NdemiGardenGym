package cv.data.mappers

import com.google.firebase.Timestamp
import cv.data.models.MemberModel
import cv.domain.entities.MemberEntity
import cv.domain.enums.MemberType
import cv.domain.repositories.DateProviderRepository
import java.util.Date

class MemberMapperImp(
    private val dateProviderRepository: DateProviderRepository,
) : MemberMapper {
    override fun getModel(entity: MemberEntity): MemberModel = entity.toModel()

    override fun getEntity(
        model: MemberModel,
        emailVerified: Boolean,
    ): MemberEntity = model.toEntity(emailVerified)

    private fun MemberEntity.toModel() =
        MemberModel(
            id = id,
            firstName = firstName.replaceFirstChar(Char::uppercase).trim(),
            lastName = lastName.replaceFirstChar(Char::uppercase).trim(),
            email = email,
            activeNowDate = activeNowDateMillis?.let { Timestamp(Date(it)) } ?: run { null },
            renewalFutureDate = renewalFutureDateMillis?.let { Timestamp(Date(it)) } ?: run { null },
            registrationDate = Timestamp(Date(registrationDateMillis)),
            apartmentNumber = apartmentNumber.replaceFirstChar(Char::uppercase).trim(),
            profileImageUrl = profileImageUrl,
            hasCoach = hasCoach,
            amountDue = amountDue,
            phoneNumber = phoneNumber,
            memberType = memberType.name,
            height = height,
        )

    private fun MemberModel.toEntity(emailVerified: Boolean = false): MemberEntity =
        MemberEntity(
            id = id,
            firstName = firstName,
            lastName = lastName,
            email = email,
            registrationDateMillis = registrationDate.toDate().time,
            renewalFutureDateMillis =
                renewalFutureDate?.toDate()?.time?.let {
                    if (dateProviderRepository.isAfterNow(it)) it else null
                } ?: run { null },
            activeNowDateMillis = activeNowDate?.toDate()?.time ?: run { null },
            apartmentNumber = apartmentNumber,
            profileImageUrl = profileImageUrl.orEmpty(),
            hasCoach = hasCoach,
            amountDue = amountDue,
            phoneNumber = phoneNumber,
            memberType = memberType.toMemberType(),
            emailVerified = emailVerified,
            height = height,
        )

    private fun String.toMemberType(): MemberType =
        when (this) {
            "SUPER_ADMIN" -> MemberType.SUPER_ADMIN
            "ADMIN" -> MemberType.ADMIN
            "SUPERVISOR" -> MemberType.SUPERVISOR
            else -> MemberType.MEMBER
        }
}
