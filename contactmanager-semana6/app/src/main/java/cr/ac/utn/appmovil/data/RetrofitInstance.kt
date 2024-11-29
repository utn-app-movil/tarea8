import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://movil-vaccine-api.azurewebsites.net/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ContactApi by lazy {
        retrofit.create(ContactApi::class.java)
    }

    val authApi: AuthApi by lazy {
        retrofit.newBuilder()
            .baseUrl("https://apicontainers.azurewebsites.net/")
            .build()
            .create(AuthApi::class.java)
    }
}
