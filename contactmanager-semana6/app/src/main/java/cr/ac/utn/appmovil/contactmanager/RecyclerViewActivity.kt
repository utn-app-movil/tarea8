package cr.ac.utn.appmovil.contactmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.data.DBManager
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_CONTACTID
import cr.ac.utn.appmovil.util.util
import cr.ac.utn.appmovil.interfaces.OnItemClickListener

class RecyclerViewActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var customAdapter: RecyclerCustomAdapter
    private lateinit var dbManager: DBManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        dbManager = DBManager(this)

        val recycler = findViewById<RecyclerView>(R.id.rcvContactList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager

        loadContacts(recycler)
    }

    private fun loadContacts(recycler: RecyclerView) {
        val contacts = dbManager.getAll()
        customAdapter = RecyclerCustomAdapter(contacts, this)
        recycler.adapter = customAdapter
        customAdapter.notifyDataSetChanged()
    }

    override fun onItemClicked(contact: Contact) {
        util.openActivity(this, ContactActivity::class.java, EXTRA_MESSAGE_CONTACTID, contact.Id)
    }
}
