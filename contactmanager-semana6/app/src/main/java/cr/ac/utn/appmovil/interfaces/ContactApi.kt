import cr.ac.utn.appmovil.identities.ContactRequest
import cr.ac.utn.appmovil.identities.DeleteContactRequest
import retrofit2.Response
import retrofit2.http.*

data class Contact(
    val personId: Long,
    val name: String,
    val lastName: String,
    val provinceCode: Int,
    val birthdate: String,
    val gender: String
)

interface ContactApi {
    @GET("people/{id}")
    suspend fun getContactById(@Path("id") id: String): ContactResponse

    @GET("people")
    suspend fun getAllContacts(): ContactResponse

    @POST("people/")
    suspend fun addContact(@Body contact: ContactRequest): ContactResponse

    @PUT("people/")
    suspend fun updateContact(@Body contact: ContactRequest): ContactResponse

    @DELETE("people/")
    suspend fun deleteContact(@Body contact: DeleteContactRequest): Response<Void>
}
