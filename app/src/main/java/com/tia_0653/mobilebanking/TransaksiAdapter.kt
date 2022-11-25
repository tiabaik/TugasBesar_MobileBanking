package com.tia_0653.mobilebanking

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import java.util.*

class TransaksiAdapter(private var transaksiList: List<transaksiBank>, context: Context):
    RecyclerView.Adapter<TransaksiAdapter.ViewHolder>(), Filterable {

    private var filteredTransaksiList: MutableList<transaksiBank>
    private val context: Context


    init {
        filteredTransaksiList = ArrayList(transaksiList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_transaksi, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredTransaksiList.size
    }

    fun setTransaksiList(transaksiList: Array<transaksiBank>){
        this.transaksiList = transaksiList.toList()
        filteredTransaksiList = transaksiList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val transaksiBank = filteredTransaksiList[position]
        holder.tvNama.text = transaksiBank.username
        holder.tvTanggalTransaksi.text = transaksiBank.tanggal_transaksi
        holder.tvKodeBank.text = transaksiBank.kode_bank.toString()
        holder.tvJumlahUang.text = transaksiBank.jumlah_uang.toString()

        holder.btnDelete.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus mahasiswa ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){_,_ ->
                    if (context is TransaksiView) transaksiBank.id?.let { it1 ->
                        context.deleteTransaksi(
                            it1
                        )
                    }
                }
                .show()

        }

        holder.cvTransaksiBank.setOnClickListener {
            val i = Intent(context, BuktiTransaksi::class.java)
            i.putExtra("id", transaksiBank.id)
            if(context is TransaksiView)
                context.startActivityForResult(i, TransaksiView.LAUNCH_ADD_ACTIVITY)
        }


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<transaksiBank> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(transaksiList)
                }else{
                    for (transaksiBank in transaksiList){
                        if(transaksiBank.username.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))

                        )filtered.add(transaksiBank)

                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults

            }

            override fun publishResults( CharSequence: CharSequence, filterResults: FilterResults) {
                filteredTransaksiList.clear()
                filteredTransaksiList.addAll(filterResults.values as List<transaksiBank>)
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvNama: TextView
        var tvTanggalTransaksi: TextView
        var tvKodeBank: TextView
        var tvJumlahUang: TextView
        var btnDelete: ImageButton
        var cvTransaksiBank: CardView

        init {
            tvNama = itemView.findViewById(R.id.tv_nama)
            tvTanggalTransaksi = itemView.findViewById(R.id.tv_tanggal_transaksi)
            tvKodeBank = itemView.findViewById(R.id.tv_kodeBank)
            tvJumlahUang = itemView.findViewById(R.id.tv_jumlahUang)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvTransaksiBank = itemView.findViewById(R.id.cv_transaksiBank)
        }

    }
}