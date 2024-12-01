package cr.ac.utn.appmovil.model

data class ApiResponse2(
    val data: List<ApiContact>,
    val responseCode: Int,
    val message: String
)
