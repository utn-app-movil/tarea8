package cr.ac.utn.appmovil.contactmanager.network

import cr.ac.utn.appmovil.data.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("technicians/")
    fun registerTechnician(@Body technicianData: TechnicianData): Call<RegisterResponse>

    @POST("technicians/validateAuth")
    fun validateAuth(@Body credentials: Map<String, String>): Call<LoginResponse>
}
