package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.model.ApiContact
import cr.ac.utn.appmovil.network.ApiContactClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactActivity : AppCompatActivity() {

    private lateinit var txtPersonId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtBirthdate: EditText
    private lateinit var spGender: Spinner
    private lateinit var spProvince: Spinner

    private var isEditionMode: Boolean = false
    private var currentContactId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        // Inicializar vistas
        txtPersonId = findViewById(R.id.txtPersonId)
        txtName = findViewById(R.id.txtContactName)
        txtLastName = findViewById(R.id.txtContactLastName)
        txtBirthdate = findViewById(R.id.txtBirthdate)
        spGender = findViewById(R.id.spGender)
        spProvince = findViewById(R.id.spProvince)

        // Validar formato de fecha (YYYYMMDD)
        txtBirthdate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val cleanString = s.toString().replace(Regex("[^\\d]"), "").take(8)
                txtBirthdate.removeTextChangedListener(this)
                txtBirthdate.setText(cleanString)
                txtBirthdate.setSelection(cleanString.length)
                txtBirthdate.addTextChangedListener(this)
            }
        })

        // Configurar Spinner de género
        val genders = listOf("M", "F")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spGender.adapter = genderAdapter

        // Configurar Spinner de provincias
        val provinces = listOf(
            "1 - San Jose", "2 - Alajuela", "3 - Cartago", "4 - Heredia",
            "5 - Guanacaste", "6 - Puntarenas", "7 - Limon"
        )
        val provinceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spProvince.adapter = provinceAdapter

        // Verificar si estamos editando un contacto
        val contactId = intent.getIntExtra("EXTRA_CONTACT_ID", -1)
        if (contactId != -1) {
            isEditionMode = true
            currentContactId = contactId
            loadEditContact(contactId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.contact_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.mnuDelete)?.isVisible = isEditionMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnuSave -> {
                saveContact()
                true
            }
            R.id.mnuDelete -> {
                confirmDelete()
                true
            }
            R.id.mnuCancel -> {
                cleanScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveContact() {
        val personId = txtPersonId.text.toString().toLongOrNull()
        val name = txtName.text.toString().trim()
        val lastName = txtLastName.text.toString().trim()
        val birthdateInput = txtBirthdate.text.toString().trim()
        val gender = spGender.selectedItem.toString()
        val provinceCode = spProvince.selectedItem.toString().split(" - ")[0].toIntOrNull() ?: 0

        // Validar el formato de la fecha
        val birthdate = if (birthdateInput.length == 8) {
            "${birthdateInput.substring(0, 4)}-${birthdateInput.substring(4, 6)}-${birthdateInput.substring(6, 8)}"
        } else {
            ""
        }

        if (personId == null || name.isEmpty() || lastName.isEmpty() || birthdate.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
            return
        }

        val contact = ApiContact(personId, name, lastName, provinceCode, birthdate, gender)

        if (isEditionMode) {
            // Usamos el ID del contacto para hacer la actualización, si es necesario
            currentContactId?.let { contactId ->
                ApiContactClient.api.updateContact(contactId, contact).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ContactActivity, "Contacto actualizado", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@ContactActivity, "Error al actualizar contacto", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@ContactActivity, t.message, Toast.LENGTH_LONG).show()
                    }
                })
            }
        } else {
            ApiContactClient.api.createContact(contact).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ContactActivity, "Contacto creado", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@ContactActivity, "Error al crear contacto", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@ContactActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun confirmDelete() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Eliminar contacto")
            .setMessage("¿Está seguro de que desea eliminar este contacto?")
            .setPositiveButton("Sí") { _, _ ->
                currentContactId?.let { id ->
                    // Ahora solo pasamos el ID para eliminarlo
                    ApiContactClient.api.deleteContact(id).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@ContactActivity, "Contacto eliminado", Toast.LENGTH_SHORT).show()
                                finish()  // Cerrar la actividad después de eliminar el contacto
                            } else {
                                Toast.makeText(this@ContactActivity, "Error al eliminar contacto", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(this@ContactActivity, t.message, Toast.LENGTH_LONG).show()
                        }
                    })
                }
            }
            .setNegativeButton("No", null)
            .create()

        dialog.show()
    }
    private fun cleanScreen() {
        txtPersonId.text.clear()
        txtName.text.clear()
        txtLastName.text.clear()
        txtBirthdate.text.clear()
        spGender.setSelection(0)
        spProvince.setSelection(0)
        isEditionMode = false
        currentContactId = null
        invalidateOptionsMenu()
    }

    private fun loadEditContact(id: Int) {
        ApiContactClient.api.getContactById(id).enqueue(object : Callback<ApiContact> {
            override fun onResponse(call: Call<ApiContact>, response: Response<ApiContact>) {
                if (response.isSuccessful) {
                    response.body()?.let { contact ->
                        txtPersonId.setText(contact.personId.toString())
                        txtName.setText(contact.name)
                        txtLastName.setText(contact.lastName)
                        txtBirthdate.setText(contact.birthdate.replace("-", ""))
                        spGender.setSelection(if (contact.gender == "M") 0 else 1)
                        spProvince.setSelection(contact.provinceCode - 1)
                    }
                } else {
                    Toast.makeText(this@ContactActivity, "Error al cargar contacto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiContact>, t: Throwable) {
                Toast.makeText(this@ContactActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}
