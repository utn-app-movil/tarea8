package cr.ac.utn.appmovil.contactmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.model.Person

class PersonAdapter(
    private val people: List<Person>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(person: Person)
    }

    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val personIdTextView: TextView = itemView.findViewById(R.id.personIdTextView)

        fun bind(person: Person) {
            nameTextView.text = itemView.context.getString(R.string.full_name, person.name, person.lastName)
            personIdTextView.text = itemView.context.getString(R.string.person_id, person.personId)

            itemView.setOnClickListener {
                listener.onItemClick(person)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_person, parent, false)
        return PersonViewHolder(view)
    }

    override fun getItemCount() = people.size

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(people[position])
    }
}
