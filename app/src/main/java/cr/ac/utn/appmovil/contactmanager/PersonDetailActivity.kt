package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.model.Person
import cr.ac.utn.appmovil.network.ApiClient
import cr.ac.utn.appmovil.network.ApiResponse
import cr.ac.utn.appmovil.network.PersonApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonDetailActivity : AppCompatActivity() {

    private lateinit var personApiService: PersonApiService
    private var person: Person? = null

    private lateinit var personIdEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var provinceCodeEditText: EditText
    private lateinit var birthdateEditText: EditText
    private lateinit var genderEditText: EditText
    private lateinit var submitButton: Button
    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_detail)

        personApiService = ApiClient.retrofit.create(PersonApiService::class.java)

        personIdEditText = findViewById(R.id.personIdEditText)
        nameEditText = findViewById(R.id.nameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        provinceCodeEditText = findViewById(R.id.provinceCodeEditText)
        birthdateEditText = findViewById(R.id.birthdateEditText)
        genderEditText = findViewById(R.id.genderEditText)
        submitButton = findViewById(R.id.submitButton)

        if (intent.hasExtra("person")) {
            isUpdate = true
            person = intent.getSerializableExtra("person") as Person
            populateFields(person!!)
        }

        submitButton.setOnClickListener {
            if (isUpdate) {
                updatePerson()
            } else {
                createPerson()
            }
        }
    }

    private fun populateFields(person: Person) {
        personIdEditText.setText(person.personId.toString())
        personIdEditText.isEnabled = false
        nameEditText.setText(person.name)
        lastNameEditText.setText(person.lastName)
        provinceCodeEditText.setText(person.provinceCode.toString())
        birthdateEditText.setText(person.birthdate)
        genderEditText.setText(person.gender)
    }

    private fun createPerson() {
        val newPerson = collectPersonData() ?: return
        personApiService.createPerson(newPerson).enqueue(object : Callback<ApiResponse<Person>> {
            override fun onResponse(
                call: Call<ApiResponse<Person>>,
                response: Response<ApiResponse<Person>>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@PersonDetailActivity,
                        getString(R.string.person_created_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(
                        this@PersonDetailActivity,
                        getString(R.string.error_creating_person, response.code(), errorBody),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Person>>, t: Throwable) {
                Toast.makeText(
                    this@PersonDetailActivity,
                    getString(R.string.connection_error, t.message),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun updatePerson() {
        val updatedPerson = collectPersonData() ?: return
        personApiService.updatePerson(updatedPerson)
            .enqueue(object : Callback<ApiResponse<Person>> {
                override fun onResponse(
                    call: Call<ApiResponse<Person>>,
                    response: Response<ApiResponse<Person>>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@PersonDetailActivity,
                            getString(R.string.person_updated_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(
                            this@PersonDetailActivity,
                            getString(R.string.error_updating_person, response.code(), errorBody),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Person>>, t: Throwable) {
                    Toast.makeText(
                        this@PersonDetailActivity,
                        getString(R.string.connection_error, t.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun collectPersonData(): Person? {
        return try {
            val personId = personIdEditText.text.toString().trim()
            val name = nameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val provinceCode = provinceCodeEditText.text.toString().trim()
            val birthdate = birthdateEditText.text.toString().trim()
            val gender = genderEditText.text.toString().trim()

            if (personId.isEmpty() || name.isEmpty() || lastName.isEmpty() || gender.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_required_fields), Toast.LENGTH_SHORT).show()
                null
            } else {
                Person(
                    personId = personId,
                    name = name,
                    lastName = lastName,
                    provinceCode = provinceCode,
                    birthdate = birthdate,
                    gender = gender
                )
            }
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.data_processing_error, e.message), Toast.LENGTH_SHORT).show()
            null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isUpdate) {
            menuInflater.inflate(R.menu.person_detail_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deletePerson) {
            deletePerson()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deletePerson() {
        person?.let {
            val personToDelete = mapOf("personId" to it.personId.toString())
            personApiService.deletePerson(personToDelete)
                .enqueue(object : Callback<ApiResponse<Void>> {
                    override fun onResponse(
                        call: Call<ApiResponse<Void>>,
                        response: Response<ApiResponse<Void>>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@PersonDetailActivity,
                                getString(R.string.person_deleted_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@PersonDetailActivity,
                                getString(R.string.error_deleting_person),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                        Toast.makeText(
                            this@PersonDetailActivity,
                            getString(R.string.connection_error, t.message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}
