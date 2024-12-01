package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.model.ApiContact
import cr.ac.utn.appmovil.network.RetrofitContactClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiContactListActivity : AppCompatActivity() {

    private lateinit var contactListView: ListView
    private lateinit var contacts: List<ApiContact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_contact_list)

        contactListView = findViewById(R.id.contactListView)
        fetchContacts()
    }

    private fun fetchContacts() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitContactClient.apiService.getAllContacts()
                runOnUiThread {
                    if (response.isSuccessful) {
                        contacts = response.body()?.data ?: emptyList()
                        if (contacts.isNotEmpty()) {
                            val adapter = ApiContactListAdapter(this@ApiContactListActivity, contacts)
                            contactListView.adapter = adapter

                            contactListView.onItemClickListener =
                                AdapterView.OnItemClickListener { _, _, position, _ ->
                                    val selectedContact = contacts[position]
                                    openContactDetail(selectedContact)
                                }
                        } else {
                            Toast.makeText(
                                this@ApiContactListActivity,
                                getString(R.string.no_contacts_available),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@ApiContactListActivity,
                            getString(R.string.error_fetching_contacts),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@ApiContactListActivity,
                        getString(R.string.error_generic, e.localizedMessage ?: "Unknown error"),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun openContactDetail(contact: ApiContact) {
        val intent = Intent(this, ApiContactDetailActivity::class.java)
        intent.putExtra("contact", contact)
        startActivity(intent)
    }
}
