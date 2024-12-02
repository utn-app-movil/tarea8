
import cr.ac.utn.appmovil.model.Contact
import cr.ac.utn.appmovil.network.ApiResponse
import retrofit2.Call
import retrofit2.http.*

interface ContactApiService {

    @GET("people/")
    fun getAllContacts(): Call<ApiResponse<List<Contact>>>

    @GET("people/{id}")
    fun getContactById(@Path("id") id: Long): Call<ApiResponse<List<Contact>>>

    @POST("people/")
    fun createContact(@Body person: Contact): Call<ApiResponse<Contact>>

    @PUT("people/")
    fun updateContact(@Body person: Contact): Call<ApiResponse<Contact>>

    @HTTP(method = "DELETE", path = "people/", hasBody = true)
    fun deleteContact(@Body person: Map<String, String>): Call<ApiResponse<Void>>
}