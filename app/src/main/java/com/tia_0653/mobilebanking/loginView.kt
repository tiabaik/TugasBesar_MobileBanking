package com.tia_0653.mobilebanking

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.tia_0653.mobilebanking.room.User
import com.tia_0653.mobilebanking.room.UserDB


class loginView: AppCompatActivity() {
    val db by lazy { UserDB(this) }

    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    private val CHANNEL_ID_1 = "channel_notification_01"

    private val notificationId1 = 101


    var sharedPreferences: SharedPreferences? = null

    var mBundle: Bundle?=null
    var Nama: String = ""
    lateinit var TanggalLahir: String
    lateinit var Email: String
    lateinit var Handphone: String
    var Password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_view)
        createNotificationChannel()
        setTitle("User Login")
        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.mainLayout)
        val btnClear: Button = findViewById(R.id.btnClear)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnRegister: Button = findViewById(R.id.btnRegister)


        btnClear.setOnClickListener {
            inputUsername.getEditText()?.setText("")
            inputPassword.getEditText()?.setText("")

            Snackbar.make(mainLayout, "Text Cleared Success", Snackbar.LENGTH_LONG).show()
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            val UserDB: User? = db.UserDao().getLogin(inputUsername.editText?.getText().toString(), inputPassword.editText?. getText(). toString())

            if(username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
                checkLogin = false
            }

            if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            val db by lazy { UserDB(this) }
            val userDao = db.UserDao()


//            if(username == "admin" && password == "0653") checkLogin = true

//            getBundle()
//            if(mBundle == null) {
//                checkLogin = false
//                Snackbar.make(mainLayout, "register first", Snackbar.LENGTH_LONG).show()
//            }else
            val user: User? = userDao.getLogin(username, password)
            if (user != null) {
                checkLogin = true
            }else{
                checkLogin = false
                Snackbar.make(mainLayout, "Periksa kembali username dan password", Snackbar.LENGTH_LONG).show()
            }


            if(!checkLogin)return@OnClickListener
            sharedPreferences = this.getSharedPreferences("userlog", Context.MODE_PRIVATE)
            var editor = sharedPreferences?.edit()
            editor?.putString("id", UserDB?.id.toString())
            editor?.commit()
            sendNotification(username,"")
            val moveHome = Intent( this@loginView, HomeActivity::class.java)
            startActivity(moveHome)
        })

        btnRegister.setOnClickListener{
            val moveRegister = Intent(this,RegisterView::class.java)
            startActivity(moveRegister)
        }
    }

    fun getBundle(){
        //Mengambil bundle dari activity sebelumnya dengan menggunakan key register mBundle = intent.getBundleExtra("signup")!!
        if(intent.getBundleExtra("register") != null) {
            mBundle = intent.getBundleExtra("register")

            // Mengambil data dari bundle
            Nama = mBundle?.getString("username")!!
            Email = mBundle?.getString("email")!!
            TanggalLahir = mBundle?.getString("Tanggallahir")!!
            Password = mBundle?.getString("password")!!
            Handphone = mBundle?.getString("NoHandphone")!!
        }


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
        broadcastIntent.putExtra("toastMessage", "Login berhasil1!")
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("hello, $username!")
            .setContentText("login berhasil selamat bergabung")
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
}