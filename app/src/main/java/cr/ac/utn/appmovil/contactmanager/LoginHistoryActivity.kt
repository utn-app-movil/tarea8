package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.data.ContactDbHelper

class LoginHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LoginHistoryAdapter
    private lateinit var loginHistory: List<LoginHistory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_history)

        recyclerView = findViewById(R.id.recyclerViewLoginHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Obtener el historial de inicios de sesión desde la base de datos
        val dbHelper = ContactDbHelper(this)
        loginHistory = dbHelper.getLoginHistory()

        // Mostrar los logs en el RecyclerView
        adapter = LoginHistoryAdapter(loginHistory)
        recyclerView.adapter = adapter
    }
}
