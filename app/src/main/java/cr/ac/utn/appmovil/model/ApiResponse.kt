package cr.ac.utn.appmovil.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("data")
    val data: List<T>
)
