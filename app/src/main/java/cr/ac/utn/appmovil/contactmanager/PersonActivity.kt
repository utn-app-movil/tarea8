package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.model.Person
import cr.ac.utn.appmovil.network.ApiClient
import cr.ac.utn.appmovil.network.ApiResponse
import cr.ac.utn.appmovil.network.PersonApiService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonActivity : AppCompatActivity(), PersonAdapter.OnItemClickListener {

    private lateinit var personApiService: PersonApiService
    private lateinit var adapter: PersonAdapter
    private var peopleList = mutableListOf<Person>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        val recyclerView = findViewById<RecyclerView>(R.id.peopleRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PersonAdapter(peopleList, this)
        recyclerView.adapter = adapter

        personApiService = ApiClient.retrofit.create(PersonApiService::class.java)

        loadPeople()

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val intent = Intent(this, PersonDetailActivity::class.java)
            startActivity(intent)
        }
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
                    } ?: run {
                        Toast.makeText(this@PersonActivity, getString(R.string.no_people_found), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@PersonActivity, getString(R.string.error_loading_people, response.code(), errorBody), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Person>>>, t: Throwable) {
                Toast.makeText(this@PersonActivity, getString(R.string.connection_error, t.message), Toast.LENGTH_LONG).show()
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
