package com.tia_0653.mobilebanking

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class PembayaranAdapter(private var pembayaranList: List<metodePembayaran>, context: Context):
    RecyclerView.Adapter<PembayaranAdapter.ViewHolder>(), Filterable {

    private var filteredPembayaranList: MutableList<metodePembayaran>
    private val context: Context


    init {
        filteredPembayaranList = ArrayList(pembayaranList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_pembayaran, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredPembayaranList.size
    }

    fun setPembayaranList(pembayaranList: Array<metodePembayaran>){
        this.pembayaranList = pembayaranList.toList()
        filteredPembayaranList = pembayaranList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val metodePembayaran = filteredPembayaranList[position]
        holder.tvNama.text = metodePembayaran.username
        holder.tvKode.text = metodePembayaran.kode.toString()
        holder.tvJumlahUang.text = metodePembayaran.jumlah_uang.toString()

        holder.btnDelete.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus Pembayaran  ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){_,_ ->
                    if (context is PembayaranView) metodePembayaran.id?.let { it1 ->
                        context.deletePembayaran(
                            it1
                        )
                    }
                }
                .show()

        }

        holder.cvPembayaran.setOnClickListener {
            val i = Intent(context, Pembayaran::class.java)
            i.putExtra("id", metodePembayaran.id)
            if(context is PembayaranView)
                context.startActivityForResult(i, PembayaranView.LAUNCH_ADD_ACTIVITY)
        }


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<metodePembayaran> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(pembayaranList)
                }else{
                    for (metodePembayaran in pembayaranList){
                        if(metodePembayaran.username.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))

                        )filtered.add(metodePembayaran)

                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults

            }

            override fun publishResults( CharSequence: CharSequence, filterResults: FilterResults) {
                filteredPembayaranList.clear()
                filteredPembayaranList.addAll(filterResults.values as List<metodePembayaran>)
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvNama: TextView
        var tvKode: TextView
        var tvJumlahUang: TextView
        var btnDelete: ImageButton
        var cvPembayaran: CardView

        init {
            tvNama = itemView.findViewById(R.id.tv_metode)
            tvKode = itemView.findViewById(R.id.tv_kode)
            tvJumlahUang = itemView.findViewById(R.id.tv_jumlahUang)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvPembayaran = itemView.findViewById(R.id.cv_transaksiBank)
        }

    }
}