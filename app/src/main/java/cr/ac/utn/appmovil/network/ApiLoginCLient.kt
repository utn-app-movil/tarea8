package cr.ac.utn.appmovil.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiLoginClient {
    private const val BASE_URL = "https://apicontainers.azurewebsites.net/"

    val postApiService: ApiLoginService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiLoginService::class.java)
    }
}