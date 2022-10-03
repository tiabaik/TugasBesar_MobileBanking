package com.tia_0653.mobilebanking

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.tia_0653.mobilebanking.databinding.FragmentBuktiTransaksiBinding
import com.tia_0653.mobilebanking.room.User
import com.tia_0653.mobilebanking.room.UserDB
import androidx.databinding.DataBindingUtil
import com.tia_0653.mobilebanking.room.Transaksi

class FragmentBuktiTransaksi(buktiTransaksi: Transaksi) : Fragment()
{
    val db by lazy{activity?.let { UserDB(it )}  }
    var sharedPreferences: SharedPreferences? = null
    lateinit var binding: FragmentBuktiTransaksiBinding
    var bukti : Transaksi = buktiTransaksi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Proses menghubungkan layout fragment_mahasiswa.xml dengan fragment ini
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_bukti_transaksi,container,false)

        binding.viewmodel= Transaksi(bukti.id, bukti.UserName, bukti.TanggalTransaksi, bukti.JumlahUang)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        sharedPreferences = this.getActivity()?.getSharedPreferences("userlog", Context.MODE_PRIVATE)
//        val nameTxt :TextView =  view.findViewById(R.id.tvname)
//        val TanggalTransaksiTxt :TextView =  view.findViewById(R.id.tvtanggalTransaksi)
//        val jumlahUangTxt :TextView =  view.findViewById(R.id.tvjumlahUang)
//        val id = sharedPreferences?.getString("id", "")
//
//
//        nameTxt.setText(db?.TransaksiDao()?.getUser(id!!.toInt())?.UserName.toString())
//        TanggalTransaksiTxt.setText(db?.TransaksiDao()?.getUser(id!!.toInt())?.TanggalTransaksi.toString())
//        jumlahUangTxt.setText(db?.TransaksiDao()?.getUser(id!!.toInt())?.JumlahUang.toString())



    }


}