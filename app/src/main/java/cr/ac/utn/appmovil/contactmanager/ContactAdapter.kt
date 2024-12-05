package cr.ac.utn.appmovil.contactmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import cr.ac.utn.appmovil.interfaces.ContactRequest








class ContactAdapter(
    context: Context,
    private val resource: Int,
    private val dataSource: MutableList<ContactRequest>
) : ArrayAdapter<ContactRequest>(context, resource, dataSource) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private val provinces = listOf(
        "San José", "Alajuela", "Cartago", "Heredia",
        "Guanacaste", "Puntarenas", "Limón"
    )

    override fun getCount(): Int = dataSource.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = convertView ?: inflater.inflate(resource, parent, false)

        val txtContactNameItem = rowView.findViewById<TextView>(R.id.txtContactNameItem)
        val txtBirthdateItem = rowView.findViewById<TextView>(R.id.imgPhotoItem)
        val txtProvinceItem = rowView.findViewById<TextView>(R.id.imgPhotoItem)
        val imgGenderItem = rowView.findViewById<ImageView>(R.id.imgPhotoItem)

        val contact = dataSource[position]

        txtContactNameItem.text = "${contact.name} ${contact.lastName}"
        txtBirthdateItem.text = contact.birthdate

        val provinceCodeAsInt = when (contact.provinceCode) {
            is Int -> contact.provinceCode
            is String -> contact.provinceCode.toIntOrNull() ?: 0
            else -> 0
        }

        txtProvinceItem.text = provinces.getOrNull(provinceCodeAsInt - 1) ?: "Desconocida"




        return rowView
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun clear() {
        dataSource.clear()
        notifyDataSetChanged()
    }

    fun addAll(newContacts: List<ContactRequest>) {
        dataSource.clear()
        dataSource.addAll(newContacts)
        notifyDataSetChanged()
    }

    fun getContactAt(position: Int): ContactRequest = dataSource[position]
}