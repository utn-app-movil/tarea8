package cr.ac.utn.appmovil.network

import cr.ac.utn.appmovil.model.ApiContact
import cr.ac.utn.appmovil.model.ApiResponse
import cr.ac.utn.appmovil.model.ApiResponse2
import cr.ac.utn.appmovil.model.ResponseModel
import cr.ac.utn.appmovil.model.Technician
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("technicians/validateAuth")
    suspend fun validateAuth(@Body credentials: Map<String, String>): Response<ResponseModel<Technician>>

    @GET("people/{id}")
    suspend fun getContactById(@Path("id") id: String): Response<ApiResponse2>

    @GET("people")
    suspend fun getAllContacts(): Response<ApiResponse<ApiContact>>

    @POST("people")
    suspend fun createContact(@Body contact: ApiContact): Response<Void>

    @PUT("people")
    suspend fun updateContact(@Body contact: ApiContact): Response<Void>

    @HTTP(method = "DELETE", path = "people", hasBody = true)
    suspend fun deleteContact(@Body body: Map<String, String>): Response<Void>


}