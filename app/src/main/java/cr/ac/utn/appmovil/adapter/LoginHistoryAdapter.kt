package cr.ac.utn.appmovil.contactmanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LoginHistoryAdapter(private val historyList: List<LoginHistory>) :
    RecyclerView.Adapter<LoginHistoryAdapter.LoginHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoginHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_login_history, parent, false)
        return LoginHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoginHistoryViewHolder, position: Int) {
        val loginHistory = historyList[position]
        holder.userIdTextView.text = loginHistory.userId
        holder.loginTimeTextView.text = loginHistory.loginTime
    }

    override fun getItemCount(): Int = historyList.size

    inner class LoginHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userIdTextView: TextView = itemView.findViewById(R.id.tvUserId)
        val loginTimeTextView: TextView = itemView.findViewById(R.id.tvLoginTime)
    }
}
