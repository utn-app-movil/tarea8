package cr.ac.utn.appmovil.contactmanager

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

class ApiContactDetailActivity : AppCompatActivity() {

    private lateinit var txtPersonId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtProvinceCode: EditText
    private lateinit var txtBirthdate: EditText
    private lateinit var txtGender: EditText

    private var contact: ApiContact? = null
    private val fixedLat = -1.0
    private val fixedLong = 10.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_contact_detail)

        txtPersonId = findViewById(R.id.txtPersonId)
        txtName = findViewById(R.id.txtName)
        txtLastName = findViewById(R.id.txtLastName)
        txtProvinceCode = findViewById(R.id.txtProvinceCode)
        txtBirthdate = findViewById(R.id.txtBirthdate)
        txtGender = findViewById(R.id.txtGender)

        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        contact = intent.getParcelableExtra("contact")
        contact?.let { populateInputs(it) }

        btnUpdate.setOnClickListener { updateContact() }
        btnDelete.setOnClickListener { deleteContact() }
    }

    private fun populateInputs(contact: ApiContact) {
        txtPersonId.setText(contact.personId)
        txtName.setText(contact.name)
        txtLastName.setText(contact.lastName)
        txtProvinceCode.setText(contact.provinceCode)
        txtBirthdate.setText(contact.birthdate)
        txtGender.setText(contact.gender)
    }

    private fun updateContact() {
        val updatedContact = buildContactFromInputs()
        if (updatedContact != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitContactClient.apiService.updateContact(updatedContact)
                    runOnUiThread {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@ApiContactDetailActivity,
                                getString(R.string.contact_updated_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@ApiContactDetailActivity,
                                getString(R.string.error_updating_contact),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread { showError(e) }
                }
            }
        }
    }

    private fun deleteContact() {
        val personIdText = txtPersonId.text.toString()
        if (personIdText.isNotEmpty()) {
            val requestBody = mapOf("personId" to personIdText)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitContactClient.apiService.deleteContact(requestBody)
                    runOnUiThread {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@ApiContactDetailActivity,
                                getString(R.string.contact_deleted_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            val errorMessage = response.errorBody()?.string() ?: getString(R.string.unknown_error)
                            Toast.makeText(
                                this@ApiContactDetailActivity,
                                getString(R.string.error_deleting_contact, errorMessage),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread { showError(e) }
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.enter_valid_id), Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildContactFromInputs(): ApiContact? {
        val personId = txtPersonId.text.toString()
        val provinceCode = txtProvinceCode.text.toString()

        if (personId.isEmpty() || provinceCode.isEmpty()) {
            Toast.makeText(this, getString(R.string.complete_all_fields_valid_values), Toast.LENGTH_SHORT).show()
            return null
        }

        val name = txtName.text.toString()
        val lastName = txtLastName.text.toString()
        val birthdate = txtBirthdate.text.toString()
        val gender = txtGender.text.toString()

        if (name.isEmpty() || lastName.isEmpty() || birthdate.isEmpty() || gender.isEmpty()) {
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
            lat = fixedLat,
            long = fixedLong
        )
    }

    private fun showError(e: Exception) {
        Toast.makeText(this, getString(R.string.error_generic, e.localizedMessage), Toast.LENGTH_LONG).show()
    }
}
