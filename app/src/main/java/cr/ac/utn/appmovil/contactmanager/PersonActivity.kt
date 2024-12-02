package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.EditText
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.model.Person
import cr.ac.utn.appmovil.network.ApiClient
import cr.ac.utn.appmovil.network.ApiResponse
import cr.ac.utn.appmovil.network.PersonApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonActivity : AppCompatActivity(), PersonAdapter.OnItemClickListener {

    private lateinit var personApiService: PersonApiService
    private lateinit var adapter: PersonAdapter
    private var peopleList = mutableListOf<Person>()

    private lateinit var searchIdEditText: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        val recyclerView = findViewById<RecyclerView>(R.id.peopleRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PersonAdapter(peopleList, this)
        recyclerView.adapter = adapter

        personApiService = ApiClient.retrofit.create(PersonApiService::class.java)

        searchIdEditText = findViewById(R.id.searchIdEditText)
        searchButton = findViewById(R.id.searchButton)

        searchButton.setOnClickListener {
            val id = searchIdEditText.text.toString().trim()
            if (id.isNotEmpty()) {
                try {
                    searchPersonById(id.toLong()) // Convertimos el ID a Long
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

    private fun searchPersonById(id: Long) {
        personApiService.getPersonById(id).enqueue(object : Callback<ApiResponse<List<Person>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Person>>>,
                response: Response<ApiResponse<List<Person>>>
            ) {
                if (response.isSuccessful) {
                    val people = response.body()?.data
                    if (people != null && people.isNotEmpty()) {
                        peopleList.clear()
                        peopleList.addAll(people)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(
                            this@PersonActivity,
                            getString(R.string.person_not_found),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@PersonActivity,
                        getString(R.string.error_loading_data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Person>>>, t: Throwable) {
                Toast.makeText(
                    this@PersonActivity,
                    getString(R.string.connection_error, t.message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun loadPeople() {
        personApiService.getAllPeople().enqueue(object : Callback<ApiResponse<List<Person>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Person>>>,
                response: Response<ApiResponse<List<Person>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        peopleList.clear()
                        peopleList.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(
                        this@PersonActivity,
                        getString(R.string.error_loading_people),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Person>>>, t: Throwable) {
                Toast.makeText(
                    this@PersonActivity,
                    getString(R.string.connection_error, t.message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onItemClick(person: Person) {
        val intent = Intent(this, PersonDetailActivity::class.java)
        intent.putExtra("person", person)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        loadPeople()
    }
}
