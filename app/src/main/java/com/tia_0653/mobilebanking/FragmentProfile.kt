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
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.tia_0653.mobilebanking.models.userBank
import com.tia_0653.mobilebanking.room.UserDB
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class FragmentProfile : Fragment() {
    val db by lazy{activity?.let { UserDB(it )}  }
    private var queue: RequestQueue? = null

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
        queue = Volley.newRequestQueue(requireActivity())
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = this.getActivity()?.getSharedPreferences("userlog", Context.MODE_PRIVATE)
//        val nameTxt :TextView =  view.findViewById(R.id.tvname)
//        val emailTxt :TextView =  view.findViewById(R.id.tvemail)
//        val TTlTxt :TextView =  view.findViewById(R.id.tvttl)
//        val NoTxt :TextView =  view.findViewById(R.id.tvnohp)
        val btnEdit : Button = view.findViewById(R.id.editBTN)

//        nameTxt.setText()
//        emailTxt.setText(db?.UserDao()?.getUser(id!!.toInt())?.Email.toString())
//        TTlTxt.setText(db?.UserDao()?.getUser(id!!.toInt())?.TanggalLahir.toString())
//        NoTxt.setText(db?.UserDao()?.getUser(id!!.toInt())?.NoHandphone.toString())

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

    private fun getuserBankById(id: Long) {

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, userBankAPi.GET_BY_ID_URL + id,
                { response ->
                    val gson = Gson()
                    val jsonObject = JSONObject(response)
                    var userBank : userBank = gson.fromJson(
                        jsonObject.getJSONObject("data").toString(),
                        userBank::class.java
                    )

                    val nameTxt :TextView =  requireView().findViewById(R.id.tvname)
                    val emailTxt :TextView =  requireView().findViewById(R.id.tvemail)
                    val TTlTxt :TextView =  requireView().findViewById(R.id.tvttl)
                    val NoTxt :TextView =  requireView().findViewById(R.id.tvnohp)


                    nameTxt.setText(userBank.username)
                    emailTxt.setText(userBank.alamat_email)
                    TTlTxt.setText(userBank.tanggal_lahir)
                    NoTxt.setText(userBank.nomor_handphone)

                    Toast.makeText(
                        requireActivity(),
                        "Data berhasil diambil",
                        Toast.LENGTH_SHORT
                    ).show()

                },
                Response.ErrorListener { error ->

                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            requireActivity(),
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)

    }

    override fun onStart() {
        super.onStart()

        val id = sharedPreferences?.getString("id", "")

        getuserBankById(id!!.toLong())
    }
}