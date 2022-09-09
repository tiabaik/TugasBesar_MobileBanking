package com.tia_0653.mobilebanking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.tia_0653.mobilebanking.entity.daftarNamaBank

class RVDaftarnamaBankAdapter(private val data: Array<daftarNamaBank>) : RecyclerView.Adapter<RVDaftarnamaBankAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_nama_bank, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int){
        val currentItem = data[position]
        holder.tvNamaBank.text = "ID ${currentItem.kodebank.toString()}"
        holder.tvDetailsBank.text = currentItem.namebank
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView : View) :  RecyclerView.ViewHolder(itemView){
        val tvNamaBank: TextView = itemView.findViewById(R.id.tv_nama_bank)
        val tvDetailsBank : TextView = itemView.findViewById(R.id.tv_details_bank)
    }

}
