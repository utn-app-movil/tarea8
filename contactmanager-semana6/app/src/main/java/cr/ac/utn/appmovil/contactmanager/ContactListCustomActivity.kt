package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import cr.ac.utn.appmovil.data.DBManager
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_CONTACTID
import cr.ac.utn.appmovil.util.util
import androidx.appcompat.app.AppCompatActivity

class ContactListCustomActivity : AppCompatActivity() {
    private lateinit var lstContactList: ListView
    private lateinit var dbManager: DBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list_custom)

        dbManager = DBManager(this)
        lstContactList = findViewById(R.id.lstContactListCustom)

        loadContacts()

        lstContactList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedContact = lstContactList.adapter.getItem(position) as Contact
            util.openActivity(this, ContactActivity::class.java, EXTRA_MESSAGE_CONTACTID, selectedContact.Id)
        }
    }

    override fun onResume() {
        super.onResume()
        loadContacts()
    }

    private fun loadContacts() {
        val contacts = dbManager.getAll()
        val adapter = ContactAdapter(this, R.layout.list_item_contact, contacts)
        lstContactList.adapter = adapter
    }
}
