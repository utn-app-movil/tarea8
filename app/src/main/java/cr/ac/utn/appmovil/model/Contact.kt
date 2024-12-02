package cr.ac.utn.appmovil.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Contact(
    @SerializedName("personId")
    val personId: Long?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("lastName")
    val lastName: String?,

    @SerializedName("provinceCode")
    val provinceCode: Int?,

    @SerializedName("birthdate")
    val birthdate: String?,

    @SerializedName("gender")
    val gender: String?,

    @SerializedName("address")
    val address: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("phone")
    val phone: Long?
) : Serializable
