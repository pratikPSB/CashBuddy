package com.psb.cashbuddy.ui.fragments.summary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.psb.cashbuddy.R
import com.psb.cashbuddy.data.db.models.Denomination
import com.psb.cashbuddy.databinding.ItemDenominationBinding

class SummaryAdapter : RecyclerView.Adapter<SummaryAdapter.ItemViewHolder>() {
    val list: ArrayList<Denomination?> = arrayListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.item_denomination, parent, false))
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int
    ) {
        val item = list[position]
        item?.let {
            holder.binding.tvDenomination.text = "${it.type.value} x ${it.count}"
        }
    }

    override fun getItemCount() = list.size

    fun setDenominations(list: List<Denomination?>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class ItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ItemDenominationBinding.bind(item)
    }
}