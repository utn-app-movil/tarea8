package cr.ac.utn.appmovil.identities

import com.google.gson.annotations.SerializedName

data class DeleteContactRequest(
    @SerializedName("personId") val personId: String
)
