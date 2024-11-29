package cr.ac.utn.appmovil.interfaces

import cr.ac.utn.appmovil.identities.Contact

interface IDBManager {
    fun add (contact: Contact)
    fun update (contact: Contact)
    fun remove (id: String)
    fun getAll(): List<Contact>
    fun getById(id: String): Contact?
}