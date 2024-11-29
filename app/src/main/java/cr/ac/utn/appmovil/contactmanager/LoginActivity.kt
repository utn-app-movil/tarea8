package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.data.LoginResponse
import cr.ac.utn.appmovil.network.RetrofitClient
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
            performLogin()
        }
    }

    private fun performLogin() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_credentials), Toast.LENGTH_SHORT).show()
            return
        }

        val credentials = mapOf("id" to username, "password" to password)

        RetrofitClient.authApi.validateAuth(credentials)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.responseCode == 0) {
                            val technician = loginResponse.data
                            val userName = "${technician?.name} ${technician?.lastName}"

                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.welcome_user, technician?.name, technician?.lastName),
                                Toast.LENGTH_SHORT
                            ).show()

                            navigateToMainActivity(userName)
                        } else {
                            val message = loginResponse?.message ?: getString(R.string.invalid_credentials)
                            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.connection_error, t.message ?: "Unknown error"),
                        Toast.LENGTH_SHORT
                    ).show()
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
