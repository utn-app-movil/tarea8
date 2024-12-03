package cr.ac.utn.appmovil.model

data class ApiContact(
    val personId: Long,
    var name: String,
    var lastName: String,
    var provinceCode: Int,
    var birthdate: String,
    var gender: String
)
