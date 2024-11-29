package cr.ac.utn.appmovil.network

import cr.ac.utn.appmovil.model.LoginRequest
import cr.ac.utn.appmovil.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiLoginService {
    @POST("technicians/validateAuth")
    suspend fun validateAuth(@Body loginRequest: LoginRequest): LoginResponse
}
