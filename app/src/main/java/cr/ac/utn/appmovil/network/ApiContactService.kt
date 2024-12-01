package cr.ac.utn.appmovil.network

import cr.ac.utn.appmovil.model.ApiContact
import cr.ac.utn.appmovil.model.ApiContactResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiContactService {
    @GET("people/")
    fun getAllContacts(): Call<ApiContactResponse>

    @GET("people/{id}")
    fun getContactById(@Path("id") id: Int): Call<ApiContact>

    @POST("people/")
    fun createContact(@Body contact: ApiContact): Call<Void>

    @PUT("people/")
    fun updateContact(@Body contact: ApiContact): Call<Void>

    @HTTP(method = "DELETE", path = "people/", hasBody = true)
    fun deleteContact(@Body contact: ApiContact): Call<Void>
}
