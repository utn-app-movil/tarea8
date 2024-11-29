package cr.ac.utn.appmovil.model

import cr.ac.utn.appmovil.data.UserData
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val data: UserData?,
    val responseCode: Int,
    val message: String?
)

