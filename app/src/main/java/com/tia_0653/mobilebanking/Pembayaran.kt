package com.tia_0653.mobilebanking

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class Pembayaran : AppCompatActivity() {
    companion object {
        private val metode_LIST = arrayOf(
            "Gopay",
            "Dana",
            "Kartu kredit",
            "BPJS",
            "Pajak",
            "Asuransi",
        )

    }

    private var edmetode: AutoCompleteTextView? = null
    private var edkode: EditText? = null
    private var etjumlahUang: EditText? = null

    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.pembayaran)
        queue = Volley.newRequestQueue(this)
        edmetode = findViewById(R.id.et_metode)
        edkode = findViewById(R.id.et_kode)
        etjumlahUang = findViewById(R.id.et_jumlah_uang)
        setExposedDropdownMenu()

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener { finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id", -1)
        if (id == -1L) {
            tvTitle.setText("Tambah Pembayaran")
            btnSave.setOnClickListener { createPembayaran() }
        } else {
            tvTitle.setText("Edit Pembayaran")
            getPembayaranById(id)

            btnSave.setOnClickListener { updatePembayaran(id) }
        }


    }

    fun setExposedDropdownMenu() {
        val adapterMetode: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            R.layout.item_listpembayaran, metode_LIST
        )
        edmetode!!.setAdapter(adapterMetode)

    }

    private fun getPembayaranById(id: Long) {


        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, pembayaranApi.GET_BY_ID_URL + id,
                { response ->
                    val transaksi = Gson().fromJson(response, metodePembayaran::class.java)
                    edmetode!!.setText(transaksi.username)
                    edkode!!.setText(transaksi.kode)
                    etjumlahUang!!.setText(transaksi.jumlah_uang)
                    setExposedDropdownMenu()
                    Toast.makeText(
                        this@Pembayaran,
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
                            this,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@Pembayaran, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createPembayaran() {

        val pembayaran = metodePembayaran(
            0,
            edmetode!!.text.toString(),
            edkode!!.text.toString(),
            etjumlahUang!!.text.toString()
        )
        val stringRequest: StringRequest =
            object :
                StringRequest(Method.POST, pembayaranApi.ADD_URL, Response.Listener { response ->
                    val gson = Gson()
                    var mahasiswa = gson.fromJson(response, pembayaran::class.java)

                    if (mahasiswa != null)
                        FancyToast.makeText(
                            this,
                            "Data berhasil Ditambah",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.SUCCESS,true
                        ).show()


                    val returnIntent = Intent()
                    setResult(AppCompatActivity.RESULT_OK, returnIntent)
                    finish()


                }, Response.ErrorListener { error ->

                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@Pembayaran, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(pembayaran)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

        queue!!.add(stringRequest)
    }

    private fun updatePembayaran(id: Long) {

        val pembayaran = metodePembayaran(
            0,
            edmetode!!.text.toString(),
            edkode!!.text.toString(),
            etjumlahUang!!.text.toString()
        )

        val stringRequest: StringRequest =
            object : StringRequest(
                Method.PUT,
                pembayaranApi.UPDATE_URL + id,
                Response.Listener { response ->
                    val gson = Gson()
                    var mahasiswa = gson.fromJson(response, pembayaran::class.java)

                    if (mahasiswa != null)
                        FancyToast.makeText(
                            this@Pembayaran,
                            "Data berhasil Diubah",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.SUCCESS,true
                        ).show()


                    val returnIntent = Intent()
                    setResult(AppCompatActivity.RESULT_OK, returnIntent)
                    finish()


                },
                Response.ErrorListener { error ->

                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@Pembayaran, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(pembayaran)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }
}