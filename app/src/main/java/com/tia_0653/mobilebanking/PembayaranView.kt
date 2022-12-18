package com.tia_0653.mobilebanking

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class PembayaranView : AppCompatActivity() {
    private var srpembayaran: SwipeRefreshLayout? = null
    private var adapter: PembayaranAdapter? = null
    private var svtransaksiBank: SearchView? = null

    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pembayaran_view)

        queue = Volley.newRequestQueue(this)

        srpembayaran = findViewById(R.id.sr_pembayaran)


        srpembayaran?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener { allMahasiswa() })


        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            val i = Intent(this@PembayaranView,  Pembayaran::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }



        val rvProduk = findViewById<RecyclerView>(R.id.rv_pembayaran)
        adapter = PembayaranAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allMahasiswa()
    }

    private fun allMahasiswa(){
        srpembayaran!!.isRefreshing = true
        val stringRequest : StringRequest = object: StringRequest(Method.GET, pembayaranApi.GET_ALL_URL,
            Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)

                var pembayaran : Array<metodePembayaran> = gson.fromJson(
                    jsonObject.getJSONArray("data").toString(),
                    Array<metodePembayaran>::class.java
                )


                adapter!!.setPembayaranList(pembayaran)

                srpembayaran!!.isRefreshing = false

                if(!pembayaran.isEmpty())
                    Toast.makeText(this@PembayaranView, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@PembayaranView, "Data Kosong!", Toast.LENGTH_SHORT).show()

            }, Response.ErrorListener { error ->
                srpembayaran!!.isRefreshing = false
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@PembayaranView, errors.getString("message"), Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    Toast.makeText(this@PembayaranView, e.message, Toast.LENGTH_SHORT).show()
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

    public fun deletePembayaran(id: Long){

        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, pembayaranApi.DELETE_URL+id, Response.Listener { response ->


                val gson = Gson()
                var mahasiswa = gson.fromJson(response, metodePembayaran::class.java)
                if(mahasiswa != null)
                    Toast.makeText(this@PembayaranView, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()

                allMahasiswa()
            }, Response.ErrorListener { error ->

                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@PembayaranView, errors.getString("message"), Toast.LENGTH_SHORT).show()
                } catch (e: java.lang.Exception){
                    Toast.makeText(this@PembayaranView, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == LAUNCH_ADD_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                allMahasiswa()
            }
        }
    }


}