package com.example.finalproject.chat



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.HomeItem
import com.example.finalproject.Message
import com.example.finalproject.R

class MessageAdapter(private var message: List<Message> ) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {


    fun addMessages(newMessages: List<Message>) {
        message = newMessages//messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderTextView: TextView = itemView.findViewById(R.id.item_email)
        val msgTextView: TextView = itemView.findViewById(R.id.item_msg)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = message[position]
        holder.senderTextView.text = message.sender
        holder.msgTextView.text = message.text
    }

    override fun getItemCount(): Int {
        return message.size
    }
}
