package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.network.RetrofitClient
import cr.ac.utn.appmovil.data.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                performLogin(username, password)
            } else {
                Toast.makeText(this, "Por favor ingresa tus datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performLogin(username: String, password: String) {
        val credentials = mapOf("id" to username, "password" to password)

        RetrofitClient.authInstance.validateAuth(credentials)
            .enqueue(object : Callback<LoginResponse> {

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.responseCode == 0) {
                            val technician = loginResponse.data
                            if (technician != null && technician.isActive) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Bienvenido, ${technician.name} ${technician.lastName}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navigateToMainActivity("${technician.name} ${technician.lastName}")
                            } else {
                                Toast.makeText(this@LoginActivity, loginResponse.message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val message = loginResponse?.message ?: "Error desconocido"
                            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Error en el servidor", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun navigateToMainActivity(userName: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("USER_NAME", userName)
        }
        startActivity(intent)
        finish()
    }
}
