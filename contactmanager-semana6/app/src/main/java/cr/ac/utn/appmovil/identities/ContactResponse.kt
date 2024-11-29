import com.google.gson.annotations.SerializedName

data class ContactResponse(
    @SerializedName("data") val data: List<Contact>
)