package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.data.DBHelper
import cr.ac.utn.appmovil.contactmanager.databinding.ActivityLoginHistoryBinding

class LoginHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper(this)

        val loginHistory = dbHelper.getAllAuthentications()

        val recyclerView: RecyclerView = binding.recyclerViewLoginHistory
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LoginHistoryAdapter(loginHistory)
    }
}
