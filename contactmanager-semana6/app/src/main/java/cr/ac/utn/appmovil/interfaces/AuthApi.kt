import retrofit2.http.Body
import retrofit2.http.POST

data class AuthRequest(val username: String, val password: String)
data class AuthResponse(val status: String, val token: String)

interface AuthApi {
    @POST("technicians/validateAuth")
    suspend fun validateAuth(@Body authRequest: AuthRequest): AuthResponse
}
