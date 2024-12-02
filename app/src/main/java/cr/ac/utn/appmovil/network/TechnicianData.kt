package cr.ac.utn.appmovil.network

data class TechnicianData(
    val id: String,
    val name: String,
    val lastName: String,
    val isActive: Boolean,
    val password: String,
    val isTemporary: Boolean
)
