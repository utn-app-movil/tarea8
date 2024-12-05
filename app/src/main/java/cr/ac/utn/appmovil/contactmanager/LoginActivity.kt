package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.data.ContactDatabaseHelper


class LoginActivity : AppCompatActivity() {
    private lateinit var dbManager: ContactDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbManager = ContactDatabaseHelper(this)

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val username = findViewById<EditText>(R.id.ID_User).text.toString()
            val password = findViewById<EditText>(R.id.User_Password).text.toString()
            authenticate(username, password)
        }
    }

    private fun authenticate(username: String, password: String) {
        val user = dbManager.getUserByUsername(username)

        if (user != null && user.password == password) {
            dbManager.saveAuthentication(username, System.currentTimeMillis().toString())
            Toast.makeText(this, "Autenticación exitosa", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_LONG).show()
        }
    }
}
