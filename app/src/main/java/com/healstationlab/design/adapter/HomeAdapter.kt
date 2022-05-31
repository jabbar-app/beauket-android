package com.healstationlab.design.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.healstationlab.design.databinding.HomeViewapgerItemBinding
import com.healstationlab.design.model.Banner
import com.healstationlab.design.ui.ProductDetailActivity

class HomeAdapter(private val imgList : ArrayList<Banner>) : RecyclerView.Adapter<HomeAdapter.Holder>() {

    private lateinit var itemClickListener: ItemClickListener

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    fun setItemClickListner(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = HomeViewapgerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(imgList[position])

        holder.itemView.setOnClickListener {
            if (imgList[position].linkUrl != null && imgList[position].linkUrl != "") {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(imgList[position].linkUrl)
                it.context.startActivity(intent)
            } else if (imgList[position].product != null) {
                val intent = Intent(it.context, ProductDetailActivity::class.java)
                intent.putExtra("id", imgList[position].product!!.id)
                it.context.startActivity(intent)
            }
        }
    }

    inner class Holder(binding : HomeViewapgerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val item = binding.homeItem

        fun bind(img : Banner) {
            Glide.with(itemView).load(img.imageUrl).into(item)
        }
    }
}