package com.tia_0653.mobilebanking

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.tia_0653.mobilebanking.room.UserDB
import kotlinx.android.synthetic.main.fragment_profile.*

class FragmentProfile : Fragment() {
    val db by lazy{activity?.let { UserDB(it )}  }

    var sharedPreferences: SharedPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Proses menghubungkan layout fragment_mahasiswa.xml dengan fragment ini
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = this.getActivity()?.getSharedPreferences("userlog", Context.MODE_PRIVATE)
        val nameTxt :TextView =  view.findViewById(R.id.tvname)
        val emailTxt :TextView =  view.findViewById(R.id.tvemail)
        val TTlTxt :TextView =  view.findViewById(R.id.tvttl)
        val NoTxt :TextView =  view.findViewById(R.id.tvnohp)
        val id = sharedPreferences?.getString("id", "")
        val btnEdit : Button = view.findViewById(R.id.editBTN)

        nameTxt.setText(db?.UserDao()?.getUser(id!!.toInt())?.UserName.toString())
        emailTxt.setText(db?.UserDao()?.getUser(id!!.toInt())?.Email.toString())
        TTlTxt.setText(db?.UserDao()?.getUser(id!!.toInt())?.TanggalLahir.toString())
        NoTxt.setText(db?.UserDao()?.getUser(id!!.toInt())?.NoHandphone.toString())

        btnEdit.setOnClickListener(){
            (activity as HomeActivity).setActivity(EditProfile())
        }

        imagteView5.setOnClickListener{
            requireActivity().run {
                val intent = Intent(this, CameraMain::class.java)
                startActivity(intent)
            }

        }
    }


}