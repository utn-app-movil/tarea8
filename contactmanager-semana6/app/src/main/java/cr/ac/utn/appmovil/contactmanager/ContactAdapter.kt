package cr.ac.utn.appmovil.contactmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import cr.ac.utn.appmovil.data.DBManager
import cr.ac.utn.appmovil.identities.Contact

class ContactAdapter(
    private val mCtx: Context,
    private val resource: Int,
    private var dataSource: List<Contact>
) : ArrayAdapter<Contact>(mCtx, resource, dataSource) {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val dbManager = DBManager(mCtx)

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.list_item_contact, parent, false)
        val txtContactNameItem = rowView.findViewById<TextView>(R.id.txtContactNameItem)
        val txtAddressItem = rowView.findViewById<TextView>(R.id.txtAddressItem)
        val txtPhoneItem = rowView.findViewById<TextView>(R.id.txtPhoneItem)
        val imgPhotoItem = rowView.findViewById<ImageView>(R.id.imgPhotoItem)

        val contact = dataSource[position]

        txtContactNameItem.text = contact.FullName
        txtAddressItem.text = contact.Address
        txtPhoneItem.text = contact.Phone.toString()
        imgPhotoItem.setImageBitmap(contact.Photo)

        return rowView
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun reloadData() {
        dataSource = dbManager.getAll()
        notifyDataSetChanged()
    }
}
