package cr.ac.utn.appmovil.network

import cr.ac.utn.appmovil.model.ResponseModel
import cr.ac.utn.appmovil.model.Technician
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("technicians/validateAuth")
    suspend fun validateAuth(@Body credentials: Map<String, String>): Response<ResponseModel<Technician>>

}