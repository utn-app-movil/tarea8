package cr.ac.utn.appmovil.identities

import android.graphics.Bitmap

class Contact {
    private var _id: String =""
    private var _name: String =""
    private var _lastName: String=""
    private var _phone: Int = 0
    private var _email: String=""
    private var _address: String=""
    private var _country: String=""
    private lateinit var _photo: Bitmap

    constructor()

    constructor(id: String, name: String, lastName: String, phone: Int, email: String, address: String, country: String, photo: Bitmap){
        this._id= id
        this._name= name
        this._lastName = lastName
        this._phone=phone
        this._email=email
        this._address= address
        this._country= country
    }

    var Id: String
        get() = this._id
        set(value) {this._id = value}

    var Name: String
        get() = this._name
        set(value) {this._name = value}

    var LastName: String
        get() = this._lastName
        set(value) {this._lastName = value}

    val FullName get() = this._name + " " + this._lastName

    var Phone: Int
        get() = this._phone
        set(value) {this._phone = value}

    var Email: String
        get() = this._email
        set(value) {this._email = value}

    var Address: String
        get() = this._address
        set(value) {this._address = value}

    var Country: String
        get() = this._country
        set(value) {this._country = value}

    var Photo: Bitmap
        get() = this._photo
        set(value) {this._photo = value}
}