package cr.ac.utn.appmovil.model

import android.os.Parcel
import android.os.Parcelable

data class ApiContact(
    val personId: String,
    val name: String,
    val lastName: String,
    val provinceCode: String,
    val birthdate: String,
    val gender: String,
    val lat: Double,
    val long: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(personId)
        parcel.writeString(name)
        parcel.writeString(lastName)
        parcel.writeString(provinceCode)
        parcel.writeString(birthdate)
        parcel.writeString(gender)
        parcel.writeDouble(lat)
        parcel.writeDouble(long)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ApiContact> {
        override fun createFromParcel(parcel: Parcel): ApiContact = ApiContact(parcel)
        override fun newArray(size: Int): Array<ApiContact?> = arrayOfNulls(size)
    }
}
