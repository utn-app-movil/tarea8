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

class ApiContactEditActivity : AppCompatActivity() {

    private lateinit var txtPersonId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtProvinceCode: EditText
    private lateinit var txtBirthdate: EditText
    private lateinit var txtGender: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_contact_edit)

        txtPersonId = findViewById(R.id.txtPersonId)
        txtName = findViewById(R.id.txtName)
        txtLastName = findViewById(R.id.txtLastName)
        txtProvinceCode = findViewById(R.id.txtProvinceCode)
        txtBirthdate = findViewById(R.id.txtBirthdate)
        txtGender = findViewById(R.id.txtGender)

        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        btnUpdate.setOnClickListener { updateContact() }
        btnDelete.setOnClickListener { deleteContact() }

        val contact = intent.getParcelableExtra<ApiContact>("contact")
        if (contact != null) {
            populateInputsFromContact(contact)
        } else {
            Toast.makeText(this, getString(R.string.error_no_contact_received), Toast.LENGTH_SHORT).show()
        }
    }

    private fun populateInputsFromContact(contact: ApiContact) {
        txtPersonId.setText(contact.personId)
        txtName.setText(contact.name)
        txtLastName.setText(contact.lastName)
        txtProvinceCode.setText(contact.provinceCode)
        txtBirthdate.setText(contact.birthdate)
        txtGender.setText(contact.gender)
    }

    private fun updateContact() {
        val contact = buildContactFromInputs()
        if (contact != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitContactClient.apiService.updateContact(contact)
                    runOnUiThread {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ApiContactEditActivity, getString(R.string.contact_updated_successfully), Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ApiContactEditActivity, ApiContactActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val errorMessage = response.errorBody()?.string() ?: getString(R.string.unknown_error)
                            Toast.makeText(this@ApiContactEditActivity, getString(R.string.error_updating_contact2, errorMessage), Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@ApiContactEditActivity, getString(R.string.error_generic, e.localizedMessage), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun deleteContact() {
        val personIdText = txtPersonId.text.toString()
        if (personIdText.isNotEmpty()) {
            val contactToDelete = mapOf("personId" to personIdText)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitContactClient.apiService.deleteContact(contactToDelete)
                    runOnUiThread {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@ApiContactEditActivity,
                                getString(R.string.contact_deleted_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@ApiContactEditActivity, ApiContactActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val errorMessage = response.errorBody()?.string() ?: getString(R.string.unknown_error)
                            Toast.makeText(
                                this@ApiContactEditActivity,
                                getString(R.string.error_deleting_contact, errorMessage),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(
                            this@ApiContactEditActivity,
                            getString(R.string.error_generic, e.localizedMessage),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.enter_valid_id), Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildContactFromInputs(): ApiContact? {
        val personId = txtPersonId.text.toString()
        val provinceCode = txtProvinceCode.text.toString()
        val name = txtName.text.toString()
        val lastName = txtLastName.text.toString()
        val birthdate = txtBirthdate.text.toString()
        val gender = txtGender.text.toString()

        if (personId.isEmpty() || provinceCode.isEmpty() || name.isEmpty() || lastName.isEmpty() || birthdate.isEmpty() || gender.isEmpty()) {
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
}
