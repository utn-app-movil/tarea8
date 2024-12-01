package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        lstContactList = findViewById(R.id.lstContactList)

        // Llamada a la API para obtener todos los contactos
        ApiContactClient.api.getAllContacts().enqueue(object : Callback<ApiContactResponse> {
            override fun onResponse(
                call: Call<ApiContactResponse>,
                response: Response<ApiContactResponse>
            ) {
                if (response.isSuccessful) {
                    processContactList(response.body())
                } else {
                    Toast.makeText(
                        this@ContactListActivity,
                        "Error al cargar contactos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiContactResponse>, t: Throwable) {
                Toast.makeText(
                    this@ContactListActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun processContactList(apiResponse: ApiContactResponse?) {
        if (apiResponse == null) {
            Toast.makeText(
                this@ContactListActivity,
                "Error: Respuesta de la API vacía",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Filtrar contactos válidos
        contactList = apiResponse.data.filter { contact ->
            contact.personId != null && contact.personId.toString().toLongOrNull() != null
        }

        if (contactList.isNotEmpty()) {
            val contactNames = contactList.mapNotNull { contact ->
                try {
                    val personId = contact.personId?.toString() ?: "ID no válido"
                    "$personId - ${contact.name ?: "Sin nombre"} ${contact.lastName ?: "Sin apellido"}"
                } catch (e: Exception) {
                    Log.e("ContactListActivity", "Error procesando contacto: ${e.message}")
                    null // Excluye contactos con errores
                }
            }

            // Mostrar contactos en el ListView
            contactAdapter = ArrayAdapter(
                this@ContactListActivity,
                android.R.layout.simple_list_item_1,
                contactNames
            )
            lstContactList.adapter = contactAdapter
        } else {
            Toast.makeText(this@ContactListActivity, "No hay contactos válidos", Toast.LENGTH_SHORT).show()
        }
    }
}
