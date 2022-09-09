package com.tia_0653.mobilebanking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterView : AppCompatActivity() {
    private lateinit var email: TextInputLayout
    private lateinit var username: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var passwordconfirm: TextInputLayout
    private lateinit var TTL: TextInputLayout
    private lateinit var hp: TextInputLayout
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_view)
        val  btnLogin: Button = findViewById(R.id.btnLogin)

        username = findViewById(R.id.tilUserName)
        email = findViewById(R.id.tilEmail)
        password = findViewById(R.id.tilRegisPassword)
        passwordconfirm = findViewById(R.id.tilRegisPasswordConfirm)
        TTL = findViewById(R.id.tiltanggalLahir)
        hp = findViewById(R.id.tilNomorhp)

        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener (View.OnClickListener{
            val intent = Intent(this, loginView::class.java)
            val mBundle = Bundle()
            var error = true
            //memasukkann data ke dalam bundle
            mBundle.putString("username", username.editText?.text.toString())
            mBundle.putString("email", email.editText?.text.toString())
            mBundle.putString("password", password.editText?.text.toString())
            mBundle.putString("passwordconfirm", passwordconfirm.editText?.text.toString())
            mBundle.putString("Tanggallahir", TTL.editText?.text.toString())
            mBundle.putString("NoHandphone", hp.editText?.text.toString())

            //melakukan intent dengan memanggil bundle
            intent.putExtra("register", mBundle)



            if (password.editText?.text.toString() != passwordconfirm.editText?.text.toString()) {
                password.setError("Password tidak sama")
                passwordconfirm.setError("Password tidak sama")
                error = false
            }
            if(!error)return@OnClickListener
            startActivity(intent)
        })

            btnLogin.setOnClickListener{
            val moveloginView = Intent(this,loginView::class.java)
            startActivity(moveloginView)
        }
    }

}
