package com.tia_0653.mobilebanking


import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.tia_0653.mobilebanking.databinding.RegisterViewBinding
import com.tia_0653.mobilebanking.room.User
import com.tia_0653.mobilebanking.room.UserDB
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class RegisterView : AppCompatActivity() {
    private lateinit var itemBinding: RegisterViewBinding

    private lateinit var signUpLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        itemBinding = RegisterViewBinding.inflate(layoutInflater)
        val view = itemBinding.root
        setContentView(view)

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
                val db by lazy { UserDB(this) }
                val userDao = db.UserDao()

                val user = User(0, Name, Email, Password, TanggalLahir, NoTelp)
                userDao.addUser(user)

                val movetoLogin = Intent(this, loginView::class.java)
                val bundle: Bundle = Bundle()
                bundle.putString("username", Name)
                bundle.putString("email", Email)
                bundle.putString("Tanggallahir", TanggalLahir)
                bundle.putString("password", Password)
                bundle.putString("NoHandphone", NoTelp)
                movetoLogin.putExtra("register", bundle)
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

}