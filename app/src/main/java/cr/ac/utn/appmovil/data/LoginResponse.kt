package cr.ac.utn.appmovil.data

import cr.ac.utn.appmovil.contactmanager.network.TechnicianData

data class LoginResponse(
    val responseCode: Int,
    val message: String,
    val data: TechnicianData?
)
