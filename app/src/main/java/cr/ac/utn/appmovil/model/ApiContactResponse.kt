package cr.ac.utn.appmovil.model

data class ApiContactResponse(
    val data: List<ApiContact>,
    val responseCode: Int,
    val message: String
)