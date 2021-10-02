package com.android.check_in_listener.visitorList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.check_in_listener.databinding.ItemVisitorListBinding
import com.android.check_in_listener.listenDb.ListenRoomData

class VisitorListRvAdapter : RecyclerView.Adapter<VisitorListRvAdapter.VisitorListViewHolder>() {

    private var visitorList = ArrayList<ListenRoomData>()

    inner class VisitorListViewHolder(private val binding: ItemVisitorListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindWithView(listenRoomData: ListenRoomData) {
            binding.tvDate.text = listenRoomData.time
            binding.tvPersonalNumber.text = listenRoomData.personalNumber
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitorListViewHolder =
        VisitorListViewHolder(
            ItemVisitorListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VisitorListViewHolder, position: Int) {
        holder.bindWithView(visitorList[position])
    }

    override fun getItemCount(): Int = visitorList.size

    fun submitList(visitorList: ArrayList<ListenRoomData>) {
        this.visitorList = visitorList
        notifyDataSetChanged()
    }
}