package cr.ac.utn.appmovil.auth

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.data.DBManager

class AuthListActivity : AppCompatActivity() {
    private lateinit var dbManager: DBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_list)

        dbManager = DBManager(this)
        val listView = findViewById<ListView>(R.id.listViewAuthentications)

        val authentications = dbManager.getAllAuthentications()
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            authentications.map { "Usuario: ${it.first}, Timestamp: ${it.second}" }
        )
        listView.adapter = adapter
    }
}
