package cr.ac.utn.appmovil.model

data class ApiContact(
    val personId: Int,
    val name: String,
    val lastName: String,
    val provinceCode: Int,
    val birthdate: String,
    val gender: String,
)