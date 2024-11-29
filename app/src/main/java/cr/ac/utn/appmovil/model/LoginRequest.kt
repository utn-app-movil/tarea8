package cr.ac.utn.appmovil.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val id: String,
    val password: String
)



