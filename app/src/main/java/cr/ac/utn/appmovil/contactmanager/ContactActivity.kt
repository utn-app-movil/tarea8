package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.model.ContactModel
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_CONTACTID
import java.io.File
import java.lang.Exception

private const val FILE_NAME = "photo.jpg"
private const val PROVIDER = "cr.ac.utn.appmovil.contactmanager.fileprovider"

class ContactActivity : AppCompatActivity() {

    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var imgPhoto: ImageView
    private lateinit var filePhoto: File
    private lateinit var spCountries: Spinner
    private lateinit var contactMod: ContactModel
    private var isEditionMode: Boolean = false
    private lateinit var countries: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        txtId = findViewById(R.id.txtContact_Id)
        txtName = findViewById(R.id.txtContactName)
        txtLastName = findViewById(R.id.txtContactLastName)
        txtPhone = findViewById(R.id.txtContactPhone)
        txtEmail = findViewById(R.id.txtContactEmail)
        txtAddress = findViewById(R.id.txtContactAddress)
        imgPhoto = findViewById(R.id.imgPhoto_Contact)
        spCountries = findViewById(R.id.spCountries_contact)

        contactMod = ContactModel(this)
        loadCountries()

        spCountries.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(this@ContactActivity, countries[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        findViewById<Button>(R.id.btnTakePicture).setOnClickListener {
            TakePhoto()
        }

        findViewById<Button>(R.id.btnSelectPhoto).setOnClickListener {
            selectPhoto()
        }

        val contactId = intent.getStringExtra(EXTRA_MESSAGE_CONTACTID)
        if (!contactId.isNullOrEmpty()) {
            isEditionMode = loadEditContact(contactId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.contact_menu, menu)
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
        try {

            val photo = (imgPhoto.drawable as? BitmapDrawable)?.bitmap

            val contact = Contact().apply {
                Id = txtId.text.toString()
                Name = txtName.text.toString()
                LastName = txtLastName.text.toString()
                Phone = txtPhone.text.toString().toInt()
                Email = txtEmail.text.toString()
                Address = txtAddress.text.toString()
                Photo = photo
                Country = spCountries.selectedItem.toString()
            }

            if (dataValidation(contact)) {
                if (!isEditionMode) contactMod.addContact(contact)
                else contactMod.updateContact(contact)

                cleanScreen()
                Toast.makeText(this, getString(R.string.msgSave), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.msgInvalidData), Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun dataValidation(contact: Contact): Boolean {
        return contact.Id.isNotEmpty() && contact.Name.isNotEmpty() &&
                contact.LastName.isNotEmpty() && contact.Address.isNotEmpty() &&
                contact.Email.isNotEmpty() && contact.Phone > 0
    }

    private fun cleanScreen() {
        txtId.text.clear()
        txtName.text.clear()
        txtLastName.text.clear()
        txtPhone.text.clear()
        txtEmail.text.clear()
        txtAddress.text.clear()
        txtId.isEnabled = true
        isEditionMode = false
        invalidateOptionsMenu()
    }

    private fun loadEditContact(id: String): Boolean {
        return try {
            val contact = contactMod.getContact(id)
            txtId.setText(contact.Id)
            txtName.setText(contact.Name)
            txtLastName.setText(contact.LastName)
            txtPhone.setText(contact.Phone.toString())
            txtEmail.setText(contact.Email)
            txtAddress.setText(contact.Address)
            spCountries.setSelection(countries.indexOf(contact.Country.trim()))
            imgPhoto.setImageBitmap(contact.Photo)
            txtId.isEnabled = false
            true
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.ConfirmDelete))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.Ok)) { _, _ ->
                contactMod.removeContact(txtId.text.toString())
                cleanScreen()
                Toast.makeText(this, getString(R.string.msgDelete), Toast.LENGTH_LONG).show()
            }
            .setNegativeButton(getString(R.string.Cancel)) { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, 101)
    }

    private fun TakePhoto() {
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        filePhoto = getPhotoFile(FILE_NAME)
        val providerFile = FileProvider.getUriForFile(this, PROVIDER, filePhoto)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)
        startActivityForResult(takePhotoIntent, 100)
    }

    private fun getPhotoFile(fileName: String): File {
        val directoryStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", directoryStorage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                101 -> {
                    val imageUri = data?.data
                    imgPhoto.setImageURI(imageUri)
                }

                100 -> {
                    val takenPhoto = BitmapFactory.decodeFile(filePhoto.absolutePath)
                    imgPhoto.setImageBitmap(takenPhoto)
                }
            }
        }
    }

    private fun loadCountries() {
        countries = resources.getStringArray(R.array.Countries).toList()
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            countries
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCountries.adapter = adapter
    }
}
