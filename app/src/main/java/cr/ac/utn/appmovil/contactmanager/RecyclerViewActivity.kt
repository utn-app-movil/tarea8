package cr.ac.utn.appmovil.contactmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.ContactRequest
import cr.ac.utn.appmovil.model.Dbmodel
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_CONTACTID
import cr.ac.utn.appmovil.util.util
import cr.ac.utn.appmovil.interfaces.OnItemClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerViewActivity : AppCompatActivity(), OnContactClickListener {

    private lateinit var customAdapter: RecyclerCustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        val recycler = findViewById<RecyclerView>(R.id.rcvContactList)
        recycler.layoutManager = LinearLayoutManager(this)

        loadContacts(recycler)
    }

    private fun loadContacts(recycler: RecyclerView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.contactApiService.getAllContacts()

                if (response.data?.isNotEmpty() == true) {
                    withContext(Dispatchers.Main) {
                        customAdapter = RecyclerCustomAdapter(response.data, this@RecyclerViewActivity)
                        recycler.adapter = customAdapter
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RecyclerViewActivity, "No hay contactos disponibles", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RecyclerViewActivity, "Error al cargar contactos: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    override fun onContactClick(contact: ContactRequest) {
        util.openActivity(this, ContactActivity::class.java, EXTRA_MESSAGE_CONTACTID, contact.personId.toString())
    }
}