package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.network.ApiClient
import cr.ac.utn.appmovil.repository.AuthRepository
import cr.ac.utn.appmovil.util.util
import cr.ac.utn.appmovil.viewmodel.AuthViewModelFactory
import cr.ac.utn.appmovil.viewmodel.LoginViewModel


class LoginActivity : AppCompatActivity() {

    private lateinit var idField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar

    private val loginViewModel: LoginViewModel by viewModels {
        AuthViewModelFactory(AuthRepository(ApiClient.authApi))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        idField = findViewById(R.id.idField)
        passwordField = findViewById(R.id.passwordField)
        loginButton = findViewById(R.id.loginButton)
        progressBar = findViewById(R.id.progressBar)

        loginButton.setOnClickListener {
            val id = idField.text.toString()
            val password = passwordField.text.toString()

            if (id.isNotEmpty() && password.isNotEmpty()) {
                performLogin(id, password)
            } else {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        observeViewModel()
    }

    private fun performLogin(id: String, password: String) {
        progressBar.visibility = View.VISIBLE
        loginViewModel.login(id, password)
    }

    private fun observeViewModel() {
        loginViewModel.loginResult.observe(this) { response ->
            progressBar.visibility = View.GONE
            if (response.isSuccessful) {
                val userData = response.body()?.data
                if (userData != null && userData.isActive) {
                    Toast.makeText(
                        this,
                        "Bienvenido ${userData.name} ${userData.lastName}",
                        Toast.LENGTH_SHORT
                    ).show()
                    util.openActivity(this, MainActivity::class.java)
                } else {
                    Toast.makeText(
                        this,
                        response.body()?.message ?: "Usuario inactivo o no encontrado.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Error: ${response.body()?.message ?: "Credenciales incorrectas"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        loginViewModel.errorMessage.observe(this) { error ->
            progressBar.visibility = View.GONE
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    // Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.login_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnuLogin -> {
                util.openActivity(this, LoginActivity::class.java)
                true
            }
            R.id.mnuRegister -> {
                //Util.openActivity(this, RegisterActivity::class.java)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
