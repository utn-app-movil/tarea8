package cr.ac.utn.appmovil.model

open class LoginResponse(
    val data: UserData?,
    val responseCode: Int,
    val message: String
)