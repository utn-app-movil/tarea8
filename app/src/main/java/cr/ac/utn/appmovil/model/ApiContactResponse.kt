package cr.ac.utn.appmovil.model

// Clase de respuesta para envolver la lista de contactos
data class ApiContactResponse(
    val data: List<ApiContact>,
    val responseCode: Int,
    val message: String
)