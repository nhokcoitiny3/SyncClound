package com.tiny.synccontact.presenter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tiny.synccontact.R
import com.tiny.synccontact.databinding.ItemMenuBinding

class AdapterRCVMenu : RecyclerView.Adapter<AdapterRCVMenu.ViewHolder>() {
    private var binding: ItemMenuBinding? = null
    private var listener: IOnRecyclerViewItemClickListener? = null
    private var ilist: IList? = null

    fun setData(ilist: IList) {
        this.ilist = ilist
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_menu, parent, false)
        return ViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = ilist!!.getItem(position)
        holder.binding.txtItem.text = item
        holder.itemView.setOnClickListener { listener!!.onRecyclerViewItemClicked(position, 1) }
    }

    override fun getItemCount(): Int {
        return this.ilist!!.count
    }

    inner class ViewHolder(private val binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickListener(listener: IOnRecyclerViewItemClickListener) {
        this.listener = listener
    }

    interface IList {
        val count: Int

        fun getItem(position: Int): String
    }
}
