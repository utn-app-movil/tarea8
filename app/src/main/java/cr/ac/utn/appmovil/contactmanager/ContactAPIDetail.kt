package cr.ac.utn.appmovil.contactmanager

import ContactApiService
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.model.Contact
import cr.ac.utn.appmovil.network.ApiContact
import cr.ac.utn.appmovil.network.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactAPIDetail : AppCompatActivity() {

    private lateinit var ContactApiService: ContactApiService
    private var contact: Contact? = null

    private lateinit var contactIdEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var provinceCodeEditText: EditText
    private lateinit var birthdateEditText: EditText
    private lateinit var genderEditText: EditText
    private lateinit var submitButton: Button
    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        ContactApiService = ApiContact.retrofit.create(ContactApiService::class.java)

        contactIdEditText = findViewById(R.id.contactIdEditText)
        nameEditText = findViewById(R.id.nameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        provinceCodeEditText = findViewById(R.id.provinceCodeEditText)
        birthdateEditText = findViewById(R.id.birthdateEditText)
        genderEditText = findViewById(R.id.genderEditText)
        submitButton = findViewById(R.id.submitButton)

        if (intent.hasExtra("contact")) {
            isUpdate = true
            contact = intent.getSerializableExtra("contact") as Contact
            populateFields(contact!!)
        }

        submitButton.setOnClickListener {
            if (isUpdate) {
                updatePerson()
            } else {
                createPerson()
            }
        }
    }

    private fun populateFields(contact: Contact) {
        contactIdEditText.setText(contact.personId.toString())
        contactIdEditText.isEnabled = false
        nameEditText.setText(contact.name)
        lastNameEditText.setText(contact.lastName)
        provinceCodeEditText.setText(contact.provinceCode.toString())
        birthdateEditText.setText(contact.birthdate)
        genderEditText.setText(contact.gender)
    }

    private fun createPerson() {
        val newPerson = collectContactData() ?: return
        ContactApiService.createContact(newPerson).enqueue(object : Callback<ApiResponse<Contact>> {
            override fun onResponse(
                call: Call<ApiResponse<Contact>>,
                response: Response<ApiResponse<Contact>>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@ContactAPIDetail,
                        getString(R.string.contact_created_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(
                        this@ContactAPIDetail,
                        getString(R.string.error_creating_contact, response.code(), errorBody),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Contact>>, t: Throwable) {
                Toast.makeText(
                    this@ContactAPIDetail,
                    getString(R.string.connection_error, t.message),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun updatePerson() {
        val updatedPerson = collectContactData() ?: return
        ContactApiService.updateContact(updatedPerson)
            .enqueue(object : Callback<ApiResponse<Contact>> {
                override fun onResponse(
                    call: Call<ApiResponse<Contact>>,
                    response: Response<ApiResponse<Contact>>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ContactAPIDetail,
                            getString(R.string.contact_updated_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(
                            this@ContactAPIDetail,
                            getString(R.string.error_updating_contact, response.code(), errorBody),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Contact>>, t: Throwable) {
                    Toast.makeText(
                        this@ContactAPIDetail,
                        getString(R.string.connection_error, t.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun collectContactData(): Contact? {
        return try {
            val contactId = contactIdEditText.text.toString().trim()
            val name = nameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val provinceCode = provinceCodeEditText.text.toString().trim()
            val birthdate = birthdateEditText.text.toString().trim()
            val gender = genderEditText.text.toString().trim()
            val address = findViewById<EditText>(R.id.txtAddressItem).text.toString().trim()
            val email = findViewById<EditText>(R.id.txtContactEmail).text.toString().trim()
            val phone = findViewById<EditText>(R.id.txtContactPhone).text.toString().trim()

            if (contactId.isEmpty() || name.isEmpty() || lastName.isEmpty() || gender.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_required_fields), Toast.LENGTH_SHORT)
                    .show()
                null
            } else {
                Contact(
                    personId = contactId.toLong(),
                    name = name,
                    lastName = lastName,
                    provinceCode = provinceCode.toInt(),
                    birthdate = birthdate,
                    gender = gender,
                    address = if (address.isNotEmpty()) address else null,
                    email = if (email.isNotEmpty()) email else null,
                    phone = if (phone.isNotEmpty()) phone.toLong() else null
                )
            }
        } catch (e: Exception) {
            Toast.makeText(
                this,
                getString(R.string.data_processing_error, e.message),
                Toast.LENGTH_SHORT
            ).show()
            null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isUpdate) {
            menuInflater.inflate(R.menu.contact_detail_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteContact) {
            deleteContact()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteContact() {
        contact?.let {
            val personToDelete =
                mapOf("contactID" to it.personId.toString())
            ContactApiService.deleteContact(personToDelete)
                .enqueue(object : Callback<ApiResponse<Void>> {
                    override fun onResponse(
                        call: Call<ApiResponse<Void>>,
                        response: Response<ApiResponse<Void>>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@ContactAPIDetail,
                                getString(R.string.contact_deleted_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@ContactAPIDetail,
                                getString(R.string.error_deleting_contact),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                        Toast.makeText(
                            this@ContactAPIDetail,
                            getString(R.string.connection_error, t.message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}
