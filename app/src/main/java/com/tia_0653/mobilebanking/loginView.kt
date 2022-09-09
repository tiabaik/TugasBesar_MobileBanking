package com.tia_0653.mobilebanking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout



class loginView: AppCompatActivity() {

    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout

    var mBundle: Bundle?=null
    var Nama: String = ""
    lateinit var TanggalLahir: String
    lateinit var Email: String
    lateinit var Handphone: String
    var Password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_view)

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

            if(username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
                checkLogin = false
            }

            if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            if(username == "admin" && password == "0653") checkLogin = true

            getBundle()
            if(mBundle == null) {
                checkLogin = false
                Snackbar.make(mainLayout, "register first", Snackbar.LENGTH_LONG).show()
            }else if (username == Nama && password == Password ) {
                checkLogin = true
            }else{
                checkLogin = false
                Snackbar.make(mainLayout, "Periksa kembali username dan password", Snackbar.LENGTH_LONG).show()
            }


            if(!checkLogin)return@OnClickListener
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
}