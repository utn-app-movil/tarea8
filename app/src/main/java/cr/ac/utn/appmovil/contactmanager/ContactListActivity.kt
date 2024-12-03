package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.model.ApiContact
import cr.ac.utn.appmovil.model.ApiContactResponse
import cr.ac.utn.appmovil.network.ApiContactClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactListActivity : AppCompatActivity() {

    private lateinit var lstContactList: ListView
    private lateinit var contactAdapter: ArrayAdapter<String>
    private var contactList: List<ApiContact> = listOf()

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        lstContactList = findViewById(R.id.lstContactList)
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.btnSearch)

        // Llamada a la API para obtener todos los contactos
        ApiContactClient.api.getAllContacts().enqueue(object : Callback<ApiContactResponse> {
            override fun onResponse(
                call: Call<ApiContactResponse>,
                response: Response<ApiContactResponse>
            ) {
                if (response.isSuccessful) {
                    val contacts = response.body()?.data ?: listOf()  // Asegurarse de obtener los contactos
                    if (contacts.isNotEmpty()) {
                        processContactList(contacts) // Llamar al procesamiento de contactos
                    } else {
                        Toast.makeText(this@ContactListActivity, "No hay contactos disponibles", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ContactListActivity, "Error al cargar contactos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiContactResponse>, t: Throwable) {
                Toast.makeText(this@ContactListActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        // Configurar el botón de búsqueda
        searchButton.setOnClickListener {
            val contactId = searchEditText.text.toString()
            if (contactId.isNotEmpty()) {
                searchContactById(contactId)
            } else {
                Toast.makeText(this@ContactListActivity, "Por favor ingresa un ID de contacto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun processContactList(contactList: List<ApiContact>) {
        // Filtrar contactos válidos
        val validContacts = contactList.filter { contact ->
            contact.personId != null
        }

        if (validContacts.isNotEmpty()) {
            val contactIds = validContacts.map { contact ->
                // Verificar si el ID es un número válido antes de mostrarlo
                val personId = contact.personId?.toString()
                if (personId != null && personId.toLongOrNull() != null) {
                    // Si es un número válido, mostrarlo
                    "ID: $personId - ${contact.name} ${contact.lastName}"
                } else {
                    // Si no es válido, mostrar un mensaje alternativo
                    "ID no válido - ${contact.name} ${contact.lastName}"
                }
            }

            // Crear el adaptador y asignarlo al ListView
            contactAdapter = ArrayAdapter(
                this@ContactListActivity,
                android.R.layout.simple_list_item_1,
                contactIds
            )
            lstContactList.adapter = contactAdapter

            // Configurar el listener para los clicks en la lista
            lstContactList.setOnItemClickListener { _, _, position, _ ->
                val selectedContactId = validContacts[position].personId
                if (selectedContactId != null) {
                    // Pasar el ID al ContactActivity para editar
                    val intent = Intent(this@ContactListActivity, ContactActivity::class.java)
                    intent.putExtra("contactId", selectedContactId)
                    startActivity(intent)
                }
            }
        } else {
            Toast.makeText(this@ContactListActivity, "No hay contactos válidos", Toast.LENGTH_SHORT).show()
        }
    }


    private fun searchContactById(contactId: String) {
        val contactIdLong = contactId.toLongOrNull()

        if (contactIdLong == null) {
            Toast.makeText(this, "El ID de contacto debe ser un número válido.", Toast.LENGTH_SHORT).show()
            return
        }

        val contactIdInt = contactIdLong.toInt()

        // Llamada a la API pasando el contactId como Int
        ApiContactClient.api.getContactById(contactIdInt).enqueue(object : Callback<ApiContact> {
            override fun onResponse(call: Call<ApiContact>, response: Response<ApiContact>) {
                if (response.isSuccessful) {
                    val contact = response.body()
                    if (contact != null) {
                        Toast.makeText(this@ContactListActivity, "Contacto encontrado: ${contact.name} ${contact.lastName}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ContactListActivity, "Contacto no encontrado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ContactListActivity, "Error al cargar el contacto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiContact>, t: Throwable) {
                Toast.makeText(this@ContactListActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
