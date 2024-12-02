package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tarea7.network.RegisterResponse
import com.example.tarea7.network.RetrofitClient
import com.example.tarea7.network.TechnicianData

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var idEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        idEditText = findViewById(R.id.idEditText)
        nameEditText = findViewById(R.id.nameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            performRegistration()
        }
    }

    private fun performRegistration() {
        val id = idEditText.text.toString()
        val name = nameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (id.isEmpty() || name.isEmpty() || lastName.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.all_fields_required), Toast.LENGTH_SHORT).show()
            return
        }

        val technicianData = TechnicianData(
            id = id,
            name = name,
            lastName = lastName,
            isActive = true,
            password = password,
            isTemporary = false
        )

        Log.d("RegisterData", technicianData.toString())

        RetrofitClient.authInstance.registerTechnician(technicianData)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        if (registerResponse?.responseCode == 0) {
                            Toast.makeText(
                                this@RegisterActivity,
                                getString(R.string.register_success),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                registerResponse?.message
                                    ?: getString(R.string.register_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Unknown error"
                        Log.e("RegisterError", errorBody)
                        Toast.makeText(
                            this@RegisterActivity,
                            getString(R.string.server_error_with_body, errorBody),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(
                        this@RegisterActivity,
                        getString(R.string.connection_error, t.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
