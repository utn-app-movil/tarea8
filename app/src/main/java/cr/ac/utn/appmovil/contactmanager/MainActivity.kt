package cr.ac.utn.appmovil.contactmanager

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.util.util

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnContactList: Button = findViewById(R.id.main_btnContactList)
        btnContactList.setOnClickListener {
            openActivity(ContactListActivity::class.java)
        }

        val btnContactListCustom: Button = findViewById(R.id.main_btnContactListCustom)
        btnContactListCustom.setOnClickListener {
            openActivity(ContactListCustomActivity::class.java)
        }

        val btnRecyclerView: Button = findViewById(R.id.btnRecycleView)
        btnRecyclerView.setOnClickListener {
            openActivity(RecyclerViewActivity::class.java)
        }

        val btnDisplayDialog: Button = findViewById(R.id.btngetDialog)
        btnDisplayDialog.setOnClickListener {
            DisplayDialog()
        }

        val btnCustomDisplayDialog: Button = findViewById(R.id.btnDisplayCustomDialog)
        btnCustomDisplayDialog.setOnClickListener {
            DisplayCustomeDialog()
        }

        val btnViewLogins: Button = findViewById(R.id.btnViewLogins)
        btnViewLogins.setOnClickListener {
            viewLoginEvents()
        }

        val btnApiGetAllContacts: Button = findViewById(R.id.btnApiGetAllContacts)
        btnApiGetAllContacts.setOnClickListener {
            val intent = Intent(this, PersonActivity::class.java)
            startActivity(intent)
        }

        val btnApiCreateContact: Button = findViewById(R.id.btnApiCreateContact)
        btnApiCreateContact.setOnClickListener {
            val intent = Intent(this, PersonDetailActivity::class.java)
            startActivity(intent)
        }

        val btnApiUpdateContact: Button = findViewById(R.id.btnApiUpdateContact)
        btnApiUpdateContact.setOnClickListener {
            Toast.makeText(this, getString(R.string.select_contact_to_update), Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PersonActivity::class.java)
            startActivity(intent)
        }

        val btnApiDeleteContact: Button = findViewById(R.id.btnApiDeleteContact)
        btnApiDeleteContact.setOnClickListener {
            Toast.makeText(this, getString(R.string.select_contact_to_delete), Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PersonActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnuAddContact -> {
                openActivity(ContactActivity::class.java)
                true
            }
            R.id.mnuViewContacts -> {
                openActivity(ContactListActivity::class.java)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun addContactButon(view: View) {
        openActivity(ContactActivity::class.java)
    }

    private fun viewLoginEvents() {
        val intent = Intent(this, LoginEventsActivity::class.java)
        startActivity(intent)
    }

    private fun openActivity(objclass: Class<*>) {
        util.openActivity(this, objclass, "", "")
    }

    private fun DisplayDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(getString(R.string.QuestionCloseApp))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.Ok)) { _, _ -> finish() }
            .setNegativeButton(getString(R.string.Cancel)) { dialog, _ -> dialog.cancel() }
            .setNeutralButton(getString(R.string.neutral_button)) { _, _ ->
                Toast.makeText(this, getString(R.string.neutral_button_clicked), Toast.LENGTH_LONG).show()
            }

        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.TitleDialogQuestion))
        alert.show()
    }

    private fun DisplayCustomeDialog() {
        val inflater: LayoutInflater =
            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogBinding = inflater.inflate(R.layout.custom_dialog, null, false)

        val dialogBuilder = AlertDialog.Builder(this, 0).create()
        dialogBuilder.apply {
            setView(dialogBinding)
            setCancelable(false)
        }.show()

        val btnOk: Button = dialogBinding.findViewById(R.id.btnOk_CustomDialog)
        btnOk.setOnClickListener {
            dialogBuilder.dismiss()
        }
    }
}
