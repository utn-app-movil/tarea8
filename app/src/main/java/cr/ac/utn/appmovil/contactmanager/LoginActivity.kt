package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.contactmanager.databinding.ActivityLoginBinding
import cr.ac.utn.appmovil.data.DBHelper
import cr.ac.utn.appmovil.network.RetrofitClient
import cr.ac.utn.appmovil.util.util
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_CONTACTID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: DBHelper // Declarar DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this) // Inicializar DBHelper

        binding.btnLogin.setOnClickListener {
            val id = binding.etUserId.text?.toString()?.trim() ?: ""
            val password = binding.etPassword.text?.toString()?.trim() ?: ""

            if (id.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = RetrofitClient.apiService.validateAuth(
                            mapOf("id" to id, "password" to password)
                        )

                        runOnUiThread {
                            if (response.isSuccessful) {
                                val body = response.body()
                                if (body != null && body.responseCode == 0 && body.data?.isActive == true) {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        getString(R.string.login_successful),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // Guardar autenticación en la base de datos
                                    dbHelper.insertAuthentication(body.data.name)

                                    // Redirigir a MainActivity
                                    util.openActivity(
                                        this@LoginActivity,
                                        MainActivity::class.java,
                                        EXTRA_MESSAGE_CONTACTID,
                                        id
                                    )

                                    finish()
                                } else {
                                    val errorMessage =
                                        body?.message ?: getString(R.string.user_inactive)
                                    Toast.makeText(
                                        this@LoginActivity,
                                        errorMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    getString(R.string.invalid_credentials),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.error_message, e.localizedMessage),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}
