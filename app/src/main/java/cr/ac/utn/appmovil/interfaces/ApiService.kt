package cr.ac.utn.appmovil.interfaces

import retrofit2.http.*

interface ApiService {
    @GET("people/{id}")
    suspend fun getContactById(@Path("id") id: String): GetContactResponse

    @GET("people/")
    suspend fun getAllContacts(): ApiResponse
    
    @POST("people/")
    suspend fun addContact(@Body contact: ContactRequest): ApiResponse

    @PUT("people/")
    suspend fun updateContact(@Body contact: ContactRequest): ApiResponse

    @DELETE("people/")
    suspend fun deleteContact(@Body contact: DeleteContactRequest): ApiResponse
}

data class GetContactResponse(
    val data: List<ContactRequest>
)

data class ApiResponse(
    val data: List<ContactRequest>?,
    val responseCode: Int,
    val message: String
)

data class ContactRequest(
    val personId: Long,
    val name: String,
    val lastName: String,
    val birthdate: String,
    val provinceCode: Any,
    val gender: String
) {
    fun getProvinceCodeAsInt(): Int {
        return when (provinceCode) {
            is Int -> provinceCode
            is String -> provinceCode.toIntOrNull() ?: 0
            else -> 0
        }
    }
}

data class DeleteContactRequest(
    val personId: String
)
