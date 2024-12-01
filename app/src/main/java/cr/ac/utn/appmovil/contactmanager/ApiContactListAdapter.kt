package cr.ac.utn.appmovil.contactmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.BaseAdapter
import cr.ac.utn.appmovil.model.ApiContact

class ApiContactListAdapter(private val context: Context, private val contacts: List<ApiContact>) : BaseAdapter() {

    override fun getCount(): Int = contacts.size

    override fun getItem(position: Int): Any = contacts[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.api_contact_list_item, parent, false)

        val contact = getItem(position) as ApiContact
        val txtName = view.findViewById<TextView>(R.id.txtContactName)
        val txtDetails = view.findViewById<TextView>(R.id.txtContactDetails)

        txtName.text = "${contact.name} ${contact.lastName}"
        txtDetails.text = "ID: ${contact.personId}, Province: ${contact.provinceCode}, Gender: ${contact.gender}"

        return view
    }
}
