package com.tia_0653.mobilebanking


import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
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
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.tia_0653.mobilebanking.databinding.RegisterViewBinding
import com.tia_0653.mobilebanking.models.userBank
import com.tia_0653.mobilebanking.room.User
import com.tia_0653.mobilebanking.room.UserDB
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class RegisterView : AppCompatActivity() {
    private lateinit var itemBinding: RegisterViewBinding

    private lateinit var signUpLayout: ConstraintLayout

    private val CHANNEL_ID_1 = "channel_notification_01"

    private val notificationId1 = 101

    private var queue: RequestQueue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        itemBinding = RegisterViewBinding.inflate(layoutInflater)
        val view = itemBinding.root
        setContentView(view)

        createNotificationChannel()

        queue = Volley.newRequestQueue(this)

        itemBinding.etTTL.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    itemBinding.etTTL.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)

                }, year, month, day)

            dpd.show()
        }

        itemBinding.btnRegister.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, loginView::class.java)
            val mbundle = Bundle()

            val Name: String = itemBinding.tilUserName.editText?.getText().toString()
            val Email: String = itemBinding.tilEmail.editText?.getText().toString()
            val Password: String = itemBinding.tilRegisPassword.editText?.getText().toString()
            val PasswordConfirm: String = itemBinding.tilRegisPasswordConfirm.editText?.getText().toString()
            val TanggalLahir: String = itemBinding.etTTL.getText().toString()
            val NoTelp: String = itemBinding.tilNomorhp.editText?.getText().toString()


            var checkSignUp = true

            if (Name.isEmpty()) {
                itemBinding.tilUserName.setError("Name must be filled with text")
                checkSignUp = false
            }

            if (NoTelp.isEmpty()) {
                itemBinding.tilNomorhp.setError("Phone Number must be filled with text")
                checkSignUp = false
            }

            if (Email.isEmpty()) {
                itemBinding.tilEmail.setError("E-mail must be filled with text")
                checkSignUp = false
            }

            if (!Email.matches(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"))) {
                itemBinding.tilEmail.setError("Email tidak valid")
                checkSignUp = false
            }

            if (TanggalLahir.isEmpty()) {
                itemBinding.etEmail.setError("Birth Date must be filled with text")
                checkSignUp = false
            }

            if (Password.isEmpty()) {
                itemBinding.etPassword.setError("Password must be filled with text")
                checkSignUp = false
            }

            if (PasswordConfirm.isEmpty()) {
                itemBinding.etPasswordConfirm.setError("Password Confirmation must be filled with text")
                checkSignUp = false
            }

            if (Password != PasswordConfirm) {
                itemBinding.etPasswordConfirm.setError("Password Confirmation doesn't match with password")
                checkSignUp = false
            }
            if (NoTelp.length != 12) {
                itemBinding.etPasswordConfirm.setError("Phone Number length must be 12 digit")
                checkSignUp = false
            }


            if (checkSignUp == true) {
                // simpan data ke database
//                val db by lazy { UserDB(this) }
//                val userDao = db.UserDao()

//                val user = User(0, Name, Email, Password, TanggalLahir, NoTelp)
//                userDao.addUser(user)

                // cek register
                cekRegister()

                val movetoLogin = Intent(this, loginView::class.java)
                val bundle: Bundle = Bundle()
                bundle.putString("username", Name)
                bundle.putString("email", Email)
                bundle.putString("Tanggallahir", TanggalLahir)
                bundle.putString("password", Password)
                bundle.putString("NoHandphone", NoTelp)
                movetoLogin.putExtra("register", bundle)

//                sendNotification(user.UserName, user.Password)
                startActivity(movetoLogin)
            } else {
                return@OnClickListener
            }


        })

        itemBinding.btnLogin.setOnClickListener(View.OnClickListener {
            val movetoLogin = Intent(this, loginView::class.java)
            startActivity(movetoLogin)
        })

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
        broadcastIntent.putExtra("toastMessage", "Register berhasil1!")
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val gambar = BitmapFactory.decodeResource(resources, R.drawable.mobilebanking)
        val bigPicture = NotificationCompat.BigPictureStyle()
            .bigPicture(gambar)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("hello, $username!")
            .setContentText("Register berhasil selamat bergabung")
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.BLUE)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "log in", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(bigPicture)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId1, builder.build())
        }
    }

    fun cekRegister() {
        val userBank = userBank(
            username = itemBinding.tilUserName.editText?.text.toString(),
            alamat_email = itemBinding.tilEmail.editText?.text.toString(),
            password = itemBinding.tilRegisPassword.editText?.text.toString(),
            konfirmasi_password = itemBinding.tilRegisPasswordConfirm.editText?.text.toString(),
            tanggal_lahir = itemBinding.etTTL.text.toString(),
            nomor_handphone = itemBinding.tilNomorhp.editText?.text.toString(),
            id = null
        )
        val stringRequest: StringRequest =
            object :
                StringRequest(Method.POST, userBankAPi.ADD_URL, Response.Listener { response ->
                    val gson = Gson()
                    var userBank = gson.fromJson(response, userBank::class.java)

                    if (userBank != null)
                        Toast.makeText(
                            this@RegisterView,
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
                        Toast.makeText(this@RegisterView, e.message, Toast.LENGTH_SHORT).show()
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