package com.example.chatbotapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbotapp.databinding.ItemMessageReceiveBinding
import com.example.chatbotapp.databinding.ItemMessageSendBinding

class MessagesAdapter(
    private var context: Context, var listMessData: ArrayList<MessagesData>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemMessReceiveVH(
        val bindingReceive: ItemMessageReceiveBinding
    ) : RecyclerView.ViewHolder(bindingReceive.root)

    class ItemMessSendVH(
        val bindingSend: ItemMessageSendBinding
    ) : RecyclerView.ViewHolder(bindingSend.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 0) return ItemMessReceiveVH(
            ItemMessageReceiveBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
        return ItemMessSendVH(
            ItemMessageSendBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        if (holder.itemViewType == 0) {
            var userFeatureViewHolder: ItemMessReceiveVH = holder as ItemMessReceiveVH
            userFeatureViewHolder.bindingReceive.tvItemMessMessReceive.text =
                listMessData[position].messages
            userFeatureViewHolder.bindingReceive.tvItemMessHeadingReceive.text =
                listMessData[position].heading
        } else {
            val userFeatureViewHolder: ItemMessSendVH = holder as ItemMessSendVH
            userFeatureViewHolder.bindingSend.tvItemMessMessSend.text =
                listMessData[position].messages
            userFeatureViewHolder.bindingSend.tvItemMessHeadingSend.text =
                listMessData[position].heading
        }

    override fun getItemCount(): Int {
        return listMessData.size
    }

    override fun getItemViewType(position: Int): Int {
        if (listMessData[position].isSend == 0) {
            return 0
        }
        return 1
    }
}