package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tarea7.network.RetrofitClient
import cr.ac.utn.appmovil.data.SQLiteManager
import cr.ac.utn.appmovil.identities.LoginEvent
import data.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    private lateinit var dbManager: SQLiteManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        dbManager = SQLiteManager(this)

        loginButton.setOnClickListener {
            realizarLogin()
        }
    }

    private fun realizarLogin() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_credentials), Toast.LENGTH_SHORT).show()
            return
        }

        val credentials = mapOf("id" to username, "password" to password)

        RetrofitClient.authInstance.validateAuth(credentials)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.responseCode == 0) {
                            Toast.makeText(
                                this@LoginActivity,
                                getString(
                                    R.string.welcome_user,
                                    loginResponse.data.name,
                                    loginResponse.data.lastName
                                ),
                                Toast.LENGTH_SHORT
                            ).show()

                            // Registrar el evento de inicio de sesión
                            val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                            val loginEvent = LoginEvent(
                                userId = username,
                                loginTime = currentTime
                            )
                            dbManager.addLoginEvent(loginEvent)

                            // Navegar al MainActivity después de un login exitoso
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Cierra el LoginActivity
                        } else {
                            Toast.makeText(this@LoginActivity, loginResponse?.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, getString(R.string.connection_error, t.message), Toast.LENGTH_SHORT).show()
                }
            })
    }
}
