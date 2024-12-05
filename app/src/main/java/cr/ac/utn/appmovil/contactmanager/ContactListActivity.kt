package cr.ac.utn.appmovil.contactmanager

import RetrofitClient
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cr.ac.utn.appmovil.interfaces.DeleteContactRequest
import kotlinx.coroutines.launch

class ContactListActivity : AppCompatActivity() {

    private lateinit var contactAdapter: ContactAdapter
    private lateinit var lstContactList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        lstContactList = findViewById(R.id.lstContactList)

        contactAdapter = ContactAdapter(this, R.layout.list_item_contact, mutableListOf())
        lstContactList.adapter = contactAdapter

        loadContactsFromApi()

        lstContactList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedContact = contactAdapter.getItem(position)!!
            val intent = Intent(this, ContactActivity::class.java).apply {
                putExtra("CONTACT_ID", selectedContact.personId)
            }
            startActivity(intent)
        }

        lstContactList.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, position, _ ->
            val selectedContact = contactAdapter.getItem(position)!!
            deleteContact(selectedContact.personId.toString())
            true
        }
    }

    override fun onResume() {
        super.onResume()
        loadContactsFromApi()
    }

    private fun loadContactsFromApi() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.contactApiService.getAllContacts()

                val contacts = response

                contactAdapter.clear()
                contactAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(this@ContactListActivity, "Error al cargar contactos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteContact(contactId: String) {
        lifecycleScope.launch {
            try {
                val deleteRequest = DeleteContactRequest(contactId)
                RetrofitClient.contactApiService.deleteContact(deleteRequest)
                Toast.makeText(this@ContactListActivity, "Contacto eliminado", Toast.LENGTH_SHORT).show()
                loadContactsFromApi()
            } catch (e: Exception) {
                Toast.makeText(this@ContactListActivity, "Error al eliminar contacto: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}