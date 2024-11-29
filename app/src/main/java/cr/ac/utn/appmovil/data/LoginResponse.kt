package cr.ac.utn.appmovil.data

data class LoginResponse(
    val data: UserData,
    val message: String,
    val responseCode: Int
)
