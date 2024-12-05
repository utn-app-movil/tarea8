package cr.ac.utn.appmovil.contactmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.ContactRequest
import cr.ac.utn.appmovil.interfaces.OnItemClickListener

interface OnContactClickListener {
    fun onContactClick(contact: ContactRequest)
}

class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val txtFullName: TextView = view.findViewById(R.id.txtContactNameItem_recycler)
    private val txtBirthdate: TextView = view.findViewById(R.id.imgPhotoItem)
    private val txtProvince: TextView = view.findViewById(R.id.imgPhotoItem)
    private val imgGender: ImageView = view.findViewById(R.id.imgPhotoItem)

    private val provinces = listOf(
        "San José", "Alajuela", "Cartago", "Heredia",
        "Guanacaste", "Puntarenas", "Limón"
    )

    fun bind(item: ContactRequest, listener: RecyclerViewActivity) {
        txtFullName.text = "${item.name} ${item.lastName}"
        txtBirthdate.text = "Nacimiento: ${item.birthdate}"

        val provinceCode = item.getProvinceCodeAsInt()
        val provinceName = provinces.getOrNull(provinceCode - 1) ?: "Desconocida"
        txtProvince.text = "Provincia: $provinceName"

        imgGender.setImageResource(
            when (item.gender) {
                "M" -> R.drawable.default_image
                "F" -> R.drawable.female_icon
                else -> R.drawable.unknown_icon
            }
        )

        itemView.setOnClickListener { listener.onContactClick(item) }
    }

}

class RecyclerCustomAdapter(
    private var itemList: List<ContactRequest> = mutableListOf(),
    private val listener: RecyclerViewActivity
) : RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_contact, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item, this.listener)
    }

    override fun getItemCount(): Int = itemList.size

    fun updateContacts(newContacts: List<ContactRequest>) {
        itemList = newContacts
        notifyDataSetChanged()
    }

    fun getContactAt(position: Int): ContactRequest = itemList[position]
}