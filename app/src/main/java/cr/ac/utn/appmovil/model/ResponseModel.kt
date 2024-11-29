package cr.ac.utn.appmovil.model

data class ResponseModel<T>(
    val data: T?,
    val responseCode: Int,
    val message: String?
)
