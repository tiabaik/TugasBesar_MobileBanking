package com.tia_0653.mobilebanking

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.tia_0653.mobilebanking.room.Transaksi
import kotlinx.android.synthetic.main.fragment_bukti_transaksi.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class BuktiTransaksi : AppCompatActivity() {
    companion object {
        private val KodeBank_LIST = arrayOf("014", "008", "002", "022", "011", "426", "019", "123", "125", "117")
        private val NamaBank_LIST = arrayOf(
            "BCA",
            "Mandiri",
            "BRI",
            "CIMB Niaga",
            "Danamon",
            "Panin",
            "Kalbar",
            "kalteng",
             "sumut",
        )

    }

    private var edNamabank: AutoCompleteTextView? = null
    private var etTanggaltransaksi: EditText? = null
    private var edkodeBank: AutoCompleteTextView? = null
    private var etjumlahUang: EditText? = null

    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_bukti_transaksi)

        queue = Volley.newRequestQueue(this)
        edNamabank = findViewById(R.id.et_namaBank)
        etTanggaltransaksi = findViewById(R.id.et_tanggal_transaksi)
        edkodeBank= findViewById(R.id.et_kode_bank)
        etjumlahUang = findViewById(R.id.et_jumlah_uang)


        setExposedDropdownMenu()

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener { finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id", -1)
        if (id == -1L) {
            tvTitle.setText("Tambah Transaksi")
            btnSave.setOnClickListener { createTransaksi() }
        } else {
            tvTitle.setText("Edit Transaksi")
            getTransaksiById(id)

            btnSave.setOnClickListener { updateTransaksi(id) }
        }
    }

    fun setExposedDropdownMenu() {
        val adapterNamaBank: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            R.layout.item_listtransaksi, NamaBank_LIST
        )
        edNamabank!!.setAdapter(adapterNamaBank)
        val adapterKodeBank: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            R.layout.item_listtransaksi, KodeBank_LIST
        )
        edkodeBank!!.setAdapter(adapterKodeBank)
    }

    private fun getTransaksiById(id: Long) {


        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, transaksiBankApi.GET_BY_ID_URL + id,
                { response ->
                    val transaksi = Gson().fromJson(response, transaksiBank::class.java)
                    edNamabank!!.setText(transaksi.username)
                    etTanggaltransaksi!!.setText(transaksi.tanggal_transaksi)
                    edkodeBank!!.setText(transaksi.kode_bank)
                    etjumlahUang!!.setText(transaksi.jumlah_uang)
                    setExposedDropdownMenu()
                    Toast.makeText(
                        this@BuktiTransaksi,
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
                        Toast.makeText(this@BuktiTransaksi, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createTransaksi() {

        val transaksi = transaksiBank(
            0,
            edNamabank!!.text.toString(),
            etTanggaltransaksi!!.text.toString(),
            edkodeBank!!.text.toString(),
            etjumlahUang!!.text.toString()
        )
        val stringRequest: StringRequest =
            object :
                StringRequest(Method.POST, transaksiBankApi.ADD_URL, Response.Listener { response ->
                    val gson = Gson()
                    var mahasiswa = gson.fromJson(response, transaksi::class.java)

                    if (mahasiswa != null)
                        Toast.makeText(
                            this@BuktiTransaksi,
                            "Data berhasil ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()


                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
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
                        Toast.makeText(this@BuktiTransaksi, e.message, Toast.LENGTH_SHORT).show()
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
                    val requestBody = gson.toJson(transaksi)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

        queue!!.add(stringRequest)
    }

    private fun updateTransaksi(id: Long) {

        val transaksi = transaksiBank(
            0,
            edNamabank!!.text.toString(),
            etTanggaltransaksi!!.text.toString(),
            edkodeBank!!.text.toString(),
            etjumlahUang!!.text.toString()
        )

        val stringRequest: StringRequest =
            object : StringRequest(
                Method.PUT,
                transaksiBankApi.UPDATE_URL + id,
                Response.Listener { response ->
                    val gson = Gson()
                    var mahasiswa = gson.fromJson(response, transaksi::class.java)

                    if (mahasiswa != null)
                        Toast.makeText(
                            this@BuktiTransaksi,
                            "Data berhasil diubah",
                            Toast.LENGTH_SHORT
                        ).show()

                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
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
                        Toast.makeText(this@BuktiTransaksi, e.message, Toast.LENGTH_SHORT).show()
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
                    val requestBody = gson.toJson(transaksi)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }


}
