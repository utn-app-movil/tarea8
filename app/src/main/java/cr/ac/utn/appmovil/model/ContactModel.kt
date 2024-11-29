package cr.ac.utn.appmovil.model
import android.content.Context
import android.content.res.Resources
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager

class ContactModel {
    private var dbManager: IDBManager = MemoryManager
    private lateinit var _context: Context

    constructor(context: Context){
        _context= context
    }

    fun addContact(contact: Contact){
        dbManager.add(contact)
    }

    fun updateContact(contact: Contact){
        dbManager.update(contact)
    }

    fun removeContact(id: String){
        var result = dbManager.getById(id)
        if (result == null)
            throw Exception(Resources.getSystem().getString(R.string.msgNotFoundContact))

        dbManager.remove(id)
    }

    fun getContacts() = dbManager.getAll()

    fun getContact(id: String): Contact{
        var result = dbManager.getById(id)
        if (result == null){
            val message = _context.getString(R.string.msgNotFoundContact).toString()
            throw Exception(message)
        }
        return result
    }

    fun getContactNames(): List<String>{
        val names = mutableListOf<String>()
        val contacts = dbManager.getAll()
        contacts.forEach { i-> names.add(i.FullName)  }
        return names.toList()
    }

}