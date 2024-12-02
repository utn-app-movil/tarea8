package cr.ac.utn.appmovil.contactmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.model.Contact

class ContactAPIAdapter(
    private val people: List<Contact>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ContactAPIAdapter.ContactViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(contact: Contact)
    }

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val contactIdTextView: TextView = itemView.findViewById(R.id.ContactIdTextView)

        fun bind(contact: Contact) {

            val name = contact.name ?: "N/A"
            val lastName = contact.lastName ?: "N/A"
            val personId = contact.personId ?: "N/A"

            nameTextView.text = itemView.context.getString(R.string.full_name, name, lastName)
            contactIdTextView.text = itemView.context.getString(R.string.contact_id, personId)

            itemView.setOnClickListener {
                listener.onItemClick(contact)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return people.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(people[position])
    }
}
