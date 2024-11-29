package cr.ac.utn.appmovil.data

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val id: String,
    val name: String,
    val lastName: String,
    val password: String,
    val isActive: Boolean,
    val isTemporary: Boolean
)

