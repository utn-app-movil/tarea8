package cr.ac.utn.appmovil.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Configuración central de Retrofit y OkHttp para consumir APIs.
 */
object ApiClient {

    // URL base de la API de autenticación
    private const val AUTH_BASE_URL = "https://apicontainers.azurewebsites.net/"

    // Configuración de JSON para Kotlinx Serialization
    private val json = Json {
        ignoreUnknownKeys = true // Ignora claves desconocidas en el JSON
        isLenient = true         // Permite formatos JSON flexibles
        encodeDefaults = true    // Incluye valores predeterminados al serializar
    }

    // Configuración del cliente HTTP con OkHttp
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Registra los cuerpos de solicitudes y respuestas
            })
            .build()
    }

    // Configuración de Retrofit para la API de autenticación
    private val authRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL) // Establece la URL base de la API de autenticación
            .client(okHttpClient) // Asigna el cliente HTTP configurado
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType())) // Usa el convertidor de Kotlinx Serialization
            .build()
    }

    // Exposición de la API de autenticación configurada con Retrofit
    val authApi: AuthApi by lazy {
        authRetrofit.create(AuthApi::class.java)
    }
}
