package cr.ac.utn.appmovil.contactmanager


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import cr.ac.utn.appmovil.identities.LoginEvent


class LoginEventAdapter(context: Context, resource: Int, objects: List<LoginEvent>) :
    ArrayAdapter<LoginEvent>(context, resource, objects) {

    private val inflater = LayoutInflater.from(context)
    private val resourceLayout = resource

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: inflater.inflate(resourceLayout, parent, false)

        val loginEvent = getItem(position)

        val txtUserId = view.findViewById<TextView>(R.id.txtUserId)
        val txtLoginTime = view.findViewById<TextView>(R.id.txtLoginTime)

        txtUserId.text = "Usuario: ${loginEvent?.userId}"
        txtLoginTime.text = "Fecha y hora: ${loginEvent?.loginTime}"

        return view
    }
}