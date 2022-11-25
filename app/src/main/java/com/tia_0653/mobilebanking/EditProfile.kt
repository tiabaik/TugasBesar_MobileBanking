package com.tia_0653.mobilebanking

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.shashank.sony.fancytoastlib.FancyToast
import com.tia_0653.mobilebanking.room.UserDB
import com.tia_0653.mobilebanking.databinding.FragmentEditBinding
import com.tia_0653.mobilebanking.models.userBank
import com.tia_0653.mobilebanking.room.User
import kotlinx.android.synthetic.main.fragment_edit.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.*
import com.tia_0653.mobilebanking.EditProfile as EditProfile1


class EditProfile : AppCompatActivity() {
//    val db by lazy {UserDB(this) }
    var itemBinding: FragmentEditBinding? = null
    var sharedPreferences: SharedPreferences? = null
    private var queue: RequestQueue? = null

    private val CHANNEL_ID_1 = "channel_notification_01"

    private val notificationId1 = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        queue = Volley.newRequestQueue(this)
        super.onCreate(savedInstanceState)
        itemBinding = FragmentEditBinding.inflate(layoutInflater)
        createNotificationChannel()

        setContentView(itemBinding?.root)

        sharedPreferences = this.getSharedPreferences("userlog", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")

        getuserBankById(id!!.toLong())

        itemBinding?.etTTL?.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    itemBinding?.ilBirthDate?.editText?.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)

                },
                year,
                month,
                day
            )

            dpd.show()
        }


        itemBinding?.btnSave?.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, loginView::class.java)

            val Name: String = itemBinding?. ilName?.editText?.getText().toString()
            val Email: String = itemBinding?. ilEmail?.editText?.getText().toString()
            val TanggalLahir: String = itemBinding?. etTTL?.getText().toString()
            val NoTelp: String = itemBinding?.ilPhoneNumber?.editText?.getText().toString()

            var checkSave = true

            if (Name.isEmpty()) {
                itemBinding?.ilName?.setError("Name must be filled with text")
                checkSave = false
            }

            if (NoTelp.isEmpty()) {
                itemBinding?.ilPhoneNumber?.setError("Phone Number must be filled with text")
                checkSave = false
            }

            if (Email.isEmpty()) {
                itemBinding?.ilEmail?.setError("E-mail must be filled with text")
                checkSave = false
            }

            if (!Email.matches(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"))) {
                itemBinding?.ilEmail?.setError("Email tidak valid")
                checkSave = false
            }

            if (TanggalLahir.isEmpty()) {
                itemBinding?.etEmail?.setError("Birth Date must be filled with text")
                checkSave = false
            }

            if (checkSave == true) {
                setupListener()
                Toast.makeText(
                    applicationContext,
                    "Your Profile Changed",
                    Toast.LENGTH_SHORT
                ).show()
                sendNotification(Name, "")
                val moveMenu = Intent(this, HomeActivity::class.java)
                startActivity(moveMenu)
            } else {
                return@OnClickListener
            }
        })
    }


    private fun setupListener() {
        sharedPreferences = this.getSharedPreferences("userlog", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")

        updateuserBank(id!!.toLong())
//        db.UserDao().updateUser(
//            User(
//                id!!.toInt(),
//                itemBinding?.etName?.getText().toString(),
//                itemBinding?.etPhoneNumber?.text.toString(),
//                itemBinding?.etEmail?.text.toString(),
//                itemBinding?.etTTL?.text.toString(),
//                db?.UserDao()?.getUser(id!!.toInt())?.Password.toString()
//            )
//        )
        finish()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(
                CHANNEL_ID_1,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotification(username: String, password: String) {

        val intent: Intent = Intent(this, RegisterView::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val broadcastIntent: Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", "Data Berhasil di edit dan diubah!")
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("hello, $username!")
            .setContentText("selamat datang")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Selamat data anda berhasil teredit dan terupdate, sekarang anda dapat mengakese aplikasi ini dengan profil yang terlah berhsail diedit"))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        with(NotificationManagerCompat.from(this)) {
            notify(notificationId1, builder.build())
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

                    val nameTxt :TextInputLayout= findViewById(R.id.ilName)
                    val emailTxt :TextInputLayout = findViewById(R.id.ilEmail)
                    val TTlTxt :TextInputLayout =  findViewById(R.id.ilBirthDate)
                    val NoTxt :TextInputLayout =  findViewById(R.id.ilPhoneNumber)


                    nameTxt.editText?.setText(userBank.username)
                    emailTxt.editText?.setText(userBank.alamat_email)
                    TTlTxt.editText?.setText(userBank.tanggal_lahir)
                    NoTxt.editText?.setText(userBank.nomor_handphone)

                    FancyToast.makeText(
                        this,
                        "Data berhasil diambil",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.INFO,true
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
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
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

    private fun updateuserBank(id: Long) {
        val nameTxt :TextInputLayout= findViewById(R.id.ilName)
        val emailTxt :TextInputLayout = findViewById(R.id.ilEmail)
        val TTlTxt :TextInputLayout =  findViewById(R.id.ilBirthDate)
        val NoTxt :TextInputLayout =  findViewById(R.id.ilPhoneNumber)

        val userBank = userBank(
            id= null,
            username = nameTxt.editText?.text.toString(),
            alamat_email = emailTxt.editText?.text.toString(),
            tanggal_lahir = TTlTxt.editText?.text.toString(),
            nomor_handphone = NoTxt.editText?.text.toString(),
            password = null,
            konfirmasi_password = null
        )
        val stringRequest: StringRequest =
            object : StringRequest(
                Method.PUT,
                userBankAPi.UPDATE_URL + id,
                Response.Listener { response ->
                    val gson = Gson()
                    val jsonObject = JSONObject(response)
                    var userBank : userBank = gson.fromJson(
                        jsonObject.getJSONObject("data").toString(),
                        com.tia_0653.mobilebanking.models.userBank::class.java
                    )

                    if (userBank != null)
                        FancyToast.makeText(
                            this@EditProfile,
                            "Data berhasil diubah",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.SUCCESS, true
                        ).show()


//                    val returnIntent = Intent()
//                    setResult(RESULT_OK, returnIntent)
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
                        Toast.makeText(this@EditProfile, e.message, Toast.LENGTH_SHORT).show()
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
                    val requestBody = gson.toJson(userBank)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }

}