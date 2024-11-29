package cr.ac.utn.appmovil.identities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Contact {
    private var _id: String = ""
    private var _name: String = ""
    private var _lastName: String = ""
    private var _phone: Int = 0
    private var _email: String = ""
    private var _address: String = ""
    private var _country: String = ""
    private var _photo: Bitmap? = null // Cambiado a Bitmap opcional

    constructor()

    constructor(
        id: String,
        name: String,
        lastName: String,
        phone: Int,
        email: String,
        address: String,
        country: String,
        photo: Bitmap?
    ) {
        this._id = id
        this._name = name
        this._lastName = lastName
        this._phone = phone
        this._email = email
        this._address = address
        this._country = country
        this._photo = photo
    }

    var Id: String
        get() = this._id
        set(value) {
            this._id = value
        }

    var Name: String
        get() = this._name
        set(value) {
            this._name = value
        }

    var LastName: String
        get() = this._lastName
        set(value) {
            this._lastName = value
        }

    val FullName: String
        get() = "$_name $_lastName"

    var Phone: Int
        get() = this._phone
        set(value) {
            this._phone = value
        }

    var Email: String
        get() = this._email
        set(value) {
            this._email = value
        }

    var Address: String
        get() = this._address
        set(value) {
            this._address = value
        }

    var Country: String
        get() = this._country
        set(value) {
            this._country = value
        }

    var Photo: Bitmap
        get() = this._photo ?: generateDefaultBitmap() // Devuelve un valor predeterminado si _photo es null
        set(value) {
            this._photo = value
        }

    private fun generateDefaultBitmap(): Bitmap {
        val width = 200
        val height = 200
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()

        paint.color = Color.LTGRAY
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        paint.color = Color.BLACK
        paint.textSize = 40f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("No Image", width / 2f, height / 2f - (paint.ascent() + paint.descent()) / 2, paint)

        return bitmap
    }
}
