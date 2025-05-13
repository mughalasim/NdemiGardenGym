package cv.domain

import cv.domain.enums.DomainErrorType

sealed interface DomainResult<out D> {
    data class Success<out D>(val data: D) : DomainResult<D>

    data class Error<out D>(val error: DomainErrorType) : DomainResult<D>
}
