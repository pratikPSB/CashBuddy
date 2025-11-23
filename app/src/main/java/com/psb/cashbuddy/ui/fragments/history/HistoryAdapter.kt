package com.psb.cashbuddy.ui.fragments.history

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.psb.cashbuddy.R
import com.psb.cashbuddy.data.db.models.TransactionHistory
import com.psb.cashbuddy.data.db.models.TransactionType
import com.psb.cashbuddy.databinding.ItemTransactionBinding
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.TransactionViewHolder>() {
    val list: ArrayList<TransactionHistory?> = arrayListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TransactionViewHolder(inflater.inflate(R.layout.item_transaction, parent, false))
    }

    override fun onBindViewHolder(
        holder: TransactionViewHolder,
        position: Int
    ) {
        val item = list[position]
        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault())
        val formattedDate = simpleDateFormat.format(item?.date)
        holder.binding.tvTransactionType.text = "Type: ${item?.type?.name}"
        holder.binding.tvTransactionDate.text = "Date: $formattedDate"
        holder.binding.tvTransactionAmount.text = "Amount: ₹${item?.amount}"
        holder.binding.tvTransactionBreakdown.text = item?.denominations?.joinToString { "${it.type.value} x ${it.count}" }

        holder.binding.root.setCardBackgroundColor(
            ColorStateList.valueOf(
                holder.binding.root.context.getColor(
                    if (item?.type == TransactionType.CREDIT) R.color.txn_credit else R.color.txn_debit
                )
            )
        )
    }

    override fun getItemCount() = list.size

    fun setTransactions(list: List<TransactionHistory?>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class TransactionViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ItemTransactionBinding.bind(item)
    }
}