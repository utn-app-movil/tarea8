package cr.ac.utn.appmovil.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiContactClient {
    private const val BASE_URL = "https://movil-vaccine-api.azurewebsites.net/"

    val api: ApiContactService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiContactService::class.java)
    }
}