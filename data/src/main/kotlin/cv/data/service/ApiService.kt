package cv.data.service

import cv.data.models.LanguageModel
import cv.data.retrofit.ApiResult
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("language/{locale}.json")
    suspend fun getLanguage(
        @Path(value = "locale") locale: String,
    ): ApiResult<LanguageModel>
}
