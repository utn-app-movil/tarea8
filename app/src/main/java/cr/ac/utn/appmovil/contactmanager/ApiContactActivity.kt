package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.model.ApiContact
import cr.ac.utn.appmovil.network.RetrofitContactClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiContactActivity : AppCompatActivity() {

    private lateinit var txtPersonId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtProvinceCode: EditText
    private lateinit var txtBirthdate: EditText
    private lateinit var txtGender: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_contact)

        txtPersonId = findViewById(R.id.txtPersonId)
        txtName = findViewById(R.id.txtName)
        txtLastName = findViewById(R.id.txtLastName)
        txtProvinceCode = findViewById(R.id.txtProvinceCode)
        txtBirthdate = findViewById(R.id.txtBirthdate)
        txtGender = findViewById(R.id.txtGender)

        val btnCreate = findViewById<Button>(R.id.btnCreate)
        val btnGetById = findViewById<Button>(R.id.btnGetById)
        val btnGetAll = findViewById<Button>(R.id.btnGetAll)

        btnCreate.setOnClickListener { createContact() }
        btnGetById.setOnClickListener { getContactById() }
        btnGetAll.setOnClickListener { getAllContacts() }
    }

    private fun createContact() {
        val contact = buildContactFromInputs()
        if (contact != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitContactClient.apiService.createContact(contact)
                    runOnUiThread {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@ApiContactActivity,
                                getString(R.string.contact_created_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            clearInputs()
                        } else {
                            Toast.makeText(
                                this@ApiContactActivity,
                                getString(R.string.error_creating_contact, response.errorBody()?.string()),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread { showError(e) }
                }
            }
        }
    }

    private fun getContactById() {
        val personIdText = txtPersonId.text.toString()
        if (personIdText.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_valid_id), Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitContactClient.apiService.getContactById(personIdText)
                runOnUiThread {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val contactList = responseBody?.data
                        if (!contactList.isNullOrEmpty()) {
                            val contact = contactList[0]
                            val intent = Intent(this@ApiContactActivity, ApiContactEditActivity::class.java)
                            intent.putExtra("contact", contact)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@ApiContactActivity, getString(R.string.contact_not_found), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            this@ApiContactActivity,
                            getString(R.string.error_fetching_contact, response.errorBody()?.string()),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread { showError(e) }
            }
        }
    }

    private fun getAllContacts() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitContactClient.apiService.getAllContacts()
                runOnUiThread {
                    if (response.isSuccessful) {
                        val contacts = response.body()?.data
                        if (!contacts.isNullOrEmpty()) {
                            val intent = Intent(this@ApiContactActivity, ApiContactListActivity::class.java)
                            intent.putParcelableArrayListExtra("contactList", ArrayList(contacts))
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@ApiContactActivity, getString(R.string.no_contacts_available), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            this@ApiContactActivity,
                            getString(R.string.error_fetching_contacts, response.errorBody()?.string()),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread { showError(e) }
            }
        }
    }

    private fun showError(e: Exception) {
        Toast.makeText(this, getString(R.string.error_generic, e.localizedMessage), Toast.LENGTH_LONG).show()
    }

    private fun buildContactFromInputs(): ApiContact? {
        val personId = txtPersonId.text.toString()
        val name = txtName.text.toString()
        val lastName = txtLastName.text.toString()
        val provinceCode = txtProvinceCode.text.toString()
        val birthdate = txtBirthdate.text.toString()
        val gender = txtGender.text.toString()

        if (personId.isEmpty() || name.isEmpty() || lastName.isEmpty() || provinceCode.isEmpty() || birthdate.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, getString(R.string.complete_all_fields), Toast.LENGTH_SHORT).show()
            return null
        }

        return ApiContact(
            personId = personId,
            name = name,
            lastName = lastName,
            provinceCode = provinceCode,
            birthdate = birthdate,
            gender = gender,
            lat = -1.0,
            long = 10.0
        )
    }

    private fun clearInputs() {
        txtPersonId.text.clear()
        txtName.text.clear()
        txtLastName.text.clear()
        txtProvinceCode.text.clear()
        txtBirthdate.text.clear()
        txtGender.text.clear()
    }
}
