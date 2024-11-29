package cr.ac.utn.appmovil.repository


import cr.ac.utn.appmovil.model.LoginRequest
import cr.ac.utn.appmovil.model.LoginResponse
import cr.ac.utn.appmovil.network.AuthApi
import retrofit2.Response

/**
 * Repositorio para manejar las operaciones relacionadas con autenticación y técnicos.
 * Esta clase actúa como una capa intermedia entre la API y las capas superiores.
 */
class AuthRepository(private val authApi: AuthApi) {

    /**
     * Realiza la autenticación del usuario enviando su ID y contraseña.
     * @param id ID del usuario.
     * @param password Contraseña del usuario.
     * @return Respuesta de la API con los datos del usuario autenticado o un error.
     */
    suspend fun login(id: String, password: String): Response<LoginResponse> {
        val loginRequest = LoginRequest(id, password)
        return authApi.login(loginRequest)
    }

    /**
     * Obtiene la lista completa de técnicos registrados.
     * @return Respuesta de la API con la lista de técnicos.
     */
    suspend fun getTechnicians(): Response<List<LoginResponse>> {
        return authApi.getTechnicians()
    }

    /**
     * Registra un nuevo técnico en el sistema.
     * @param id ID del técnico.
     * @param name Nombre del técnico.
     * @param lastName Apellido del técnico.
     * @param password Contraseña del técnico.
     * @param isActive Estado del técnico (activo o no).
     * @param isTemporary Estado temporal del técnico.
     * @return Respuesta de la API confirmando el registro o un error.
     *
    suspend fun registerTechnician(
        id: String,
        name: String,
        lastName: String,
        password: String,
        isActive: Boolean,
        isTemporary: Boolean
    ): Response<RegisterResponse> {
        val registerRequest = RegisterRequest(
            id = id,
            name = name,
            lastName = lastName,
            password = password,
            isActive = isActive,
            isTemporary = isTemporary
        )
        return authApi.registerTechnician(registerRequest)
    }
    */
}
