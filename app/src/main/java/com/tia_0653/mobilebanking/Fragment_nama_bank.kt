package com.tia_0653.mobilebanking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.tia_0653.mobilebanking.Fragment_nama_bank
import com.tia_0653.mobilebanking.entity.daftarNamaBank

class Fragment_nama_bank : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nama_bank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVDaftarnamaBankAdapter = RVDaftarnamaBankAdapter(daftarNamaBank.listOfdaftarNamaBank)
        val rvnamabank : RecyclerView = view.findViewById(R.id.rv_namabank)
        rvnamabank.layoutManager = layoutManager
        rvnamabank.setHasFixedSize(true)
        rvnamabank.adapter = adapter
    }
}