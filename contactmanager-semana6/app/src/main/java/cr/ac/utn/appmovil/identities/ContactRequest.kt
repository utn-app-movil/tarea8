package cr.ac.utn.appmovil.identities

data class ContactRequest(
    val personId: Long,
    val name: String,
    val lastName: String,
    val provinceCode: Int,
    val birthdate: String,
    val gender: String
)