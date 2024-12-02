package cr.ac.utn.appmovil.contactmanager

import ContactApiService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.EditText
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.model.Contact
import cr.ac.utn.appmovil.network.ApiContact
import cr.ac.utn.appmovil.network.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactAPIActivity : AppCompatActivity(), ContactAPIAdapter.OnItemClickListener {

    private lateinit var contactApiService: ContactApiService
    private lateinit var adapter: ContactAPIAdapter
    private var peopleList = mutableListOf<Contact>()

    private lateinit var searchIdEditText: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_contact)

        val recyclerView = findViewById<RecyclerView>(R.id.peopleRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ContactAPIAdapter(peopleList, this)
        recyclerView.adapter = adapter

        contactApiService = ApiContact.retrofit.create(ContactApiService::class.java)

        searchIdEditText = findViewById(R.id.searchIdEditText)
        searchButton = findViewById(R.id.searchButton)

        searchButton.setOnClickListener {
            val id = searchIdEditText.text.toString().trim()
            if (id.isNotEmpty()) {
                try {
                    searchContactById(id.toLong())
                } catch (e: NumberFormatException) {
                    Toast.makeText(
                        this,
                        getString(R.string.invalid_id_format),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.enter_id_to_search),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        loadPeople()
    }

    private fun searchContactById(id: Long) {
        contactApiService.getContactById(id).enqueue(object : Callback<ApiResponse<List<Contact>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Contact>>>,
                response: Response<ApiResponse<List<Contact>>>
            ) {
                if (response.isSuccessful) {
                    val people = response.body()?.data
                    if (!people.isNullOrEmpty()) {
                        peopleList.clear()
                        peopleList.addAll(people)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(
                            this@ContactAPIActivity,
                            getString(R.string.contact_not_found),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e("API_ERROR", "Response error: ${response.code()} - ${response.message()}")
                    Toast.makeText(
                        this@ContactAPIActivity,
                        "Error fetching contact: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Contact>>>, t: Throwable) {
                Log.e("API_ERROR", "Failure: ${t.message}")
                Toast.makeText(
                    this@ContactAPIActivity,
                    "Connection error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun loadPeople() {
        contactApiService.getAllContacts().enqueue(object : Callback<ApiResponse<List<Contact>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Contact>>>,
                response: Response<ApiResponse<List<Contact>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        peopleList.clear()
                        peopleList.addAll(it)
                        adapter.notifyDataSetChanged()
                    } ?: run {
                        Toast.makeText(
                            this@ContactAPIActivity,
                            getString(R.string.no_people_found),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e("API_ERROR", "Response error: ${response.code()} - ${response.message()}")
                    Toast.makeText(
                        this@ContactAPIActivity,
                        getString(R.string.error_loading_people),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Contact>>>, t: Throwable) {
                Log.e("API_ERROR", "Failure: ${t.message}")
                Toast.makeText(
                    this@ContactAPIActivity,
                    "Connection error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onItemClick(contact: Contact) {
        val intent = Intent(this, ContactAPIDetail::class.java)
        intent.putExtra("contact", contact)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        loadPeople()
    }
}
