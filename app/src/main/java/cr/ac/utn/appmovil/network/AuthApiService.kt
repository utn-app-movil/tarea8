package cr.ac.utn.appmovil.network

import cr.ac.utn.appmovil.data.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @POST("technicians/validateAuth")
    fun validateAuth(@Body credentials: Map<String, String>): Call<LoginResponse>

    @GET("technicians")
    fun getTechnicians(): Call<List<TechnicianData>>
}
