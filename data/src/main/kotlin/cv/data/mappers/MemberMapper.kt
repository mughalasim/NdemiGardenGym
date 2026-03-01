package cv.data.mappers

import cv.data.models.MemberModel
import cv.domain.entities.MemberEntity

interface MemberMapper {
    fun getEntity(
        model: MemberModel,
        emailVerified: Boolean = false,
    ): MemberEntity

    fun getModel(entity: MemberEntity): MemberModel
}
