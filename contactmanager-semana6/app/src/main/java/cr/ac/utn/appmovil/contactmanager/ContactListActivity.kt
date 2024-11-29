package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import cr.ac.utn.appmovil.data.DBManager
import cr.ac.utn.appmovil.identities.Contact

class ContactListActivity : AppCompatActivity() {

    private lateinit var dbManager: DBManager
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var lstContactList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        dbManager = DBManager(this)
        lstContactList = findViewById(R.id.lstContactList)

        val contacts = dbManager.getAll()

        contactAdapter = ContactAdapter(this, R.layout.list_item_contact, contacts)
        lstContactList.adapter = contactAdapter

        lstContactList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedContact = contactAdapter.getItem(position) as Contact
            val intent = Intent(this, ContactActivity::class.java).apply {
                putExtra("CONTACT_ID", selectedContact.Id)
            }
            startActivity(intent)
        }

        lstContactList.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, position, _ ->
            val selectedContact = contactAdapter.getItem(position) as Contact
            dbManager.remove(selectedContact.Id)
            reloadContactList()
            Toast.makeText(this, "Contacto eliminado: ${selectedContact.FullName}", Toast.LENGTH_SHORT).show()
            true
        }
    }

    override fun onResume() {
        super.onResume()
        reloadContactList()
    }

    private fun reloadContactList() {
        val contacts = dbManager.getAll()
        contactAdapter = ContactAdapter(this, R.layout.list_item_contact, contacts)
        lstContactList.adapter = contactAdapter
    }
}
