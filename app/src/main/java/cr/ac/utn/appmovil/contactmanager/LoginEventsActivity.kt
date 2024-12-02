package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.data.SQLiteManager
import cr.ac.utn.appmovil.identities.LoginEvent

class LoginEventsActivity : AppCompatActivity() {

    private lateinit var dbManager: SQLiteManager
    private lateinit var lstLoginEvents: ListView
    private lateinit var loginEvents: List<LoginEvent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_event)

        lstLoginEvents = findViewById(R.id.lstLoginEvents)
        dbManager = SQLiteManager(this)

        loginEvents = dbManager.getAllLoginEvents()

        val adapter = LoginEventAdapter(this, R.layout.list_item_login_event, loginEvents)
        lstLoginEvents.adapter = adapter
    }
}