package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import cr.ac.utn.appmovil.interfaces.ApiService
import kotlinx.coroutines.launch
import RetrofitClient.createService




class ContactListCustomActivity : AppCompatActivity() {
    private lateinit var lstContactList: ListView
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list_custom)

        lstContactList = findViewById(R.id.lstContactListCustom)

        contactAdapter = ContactAdapter(
            context = this,
            resource = R.layout.list_item_contact,
            dataSource = mutableListOf()
        )
        lstContactList.adapter = contactAdapter

        setupItemClickListener()

        loadContactsFromApi()
    }

    override fun onResume() {
        super.onResume()
        loadContactsFromApi()
    }

    private fun setupItemClickListener() {
        lstContactList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            try {
                val selectedContact = contactAdapter.getContactAt(position)

                val intent = Intent(this, ContactActivity::class.java).apply {
                    putExtra("CONTACT_ID", selectedContact.personId.toString())
                }
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Error al seleccionar el contacto: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loadContactsFromApi() {
        lifecycleScope.launch {
            try {
                val apiService = createService(ApiService::class.java)

                val response = apiService.getAllContacts()

                contactAdapter.clear()
                contactAdapter.addAll()
            } catch (e: Exception) {
                Toast.makeText(
                    this@ContactListCustomActivity,
                    "Error al cargar contactos: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}