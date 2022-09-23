package com.tia_0653.mobilebanking

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.tia_0653.mobilebanking.room.UserDB
import com.tia_0653.mobilebanking.databinding.FragmentEditBinding
import com.tia_0653.mobilebanking.room.User
import java.util.*


class EditProfile : AppCompatActivity() {
    val db by lazy {UserDB(this) }
    var itemBinding: FragmentEditBinding? = null
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemBinding = FragmentEditBinding.inflate(layoutInflater)

        setContentView(itemBinding?.root)

        sharedPreferences = this.getSharedPreferences("userlog", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "")
        itemBinding?.etName?.setText(db?.UserDao()?.getUser(id!!.toInt())?.UserName)
        itemBinding?.etEmail?.setText(db?.UserDao()?.getUser(id!!.toInt())?.Email)
        itemBinding?.etTTL?.setText(db?.UserDao()?.getUser(id!!.toInt())?.TanggalLahir)
        itemBinding?.etPhoneNumber?.setText(db?.UserDao()?.getUser(id!!.toInt())?.NoHandphone)

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

        db.UserDao().updateUser(
            User(
                id!!.toInt(),
                itemBinding?.etName?.getText().toString(),
                itemBinding?.etPhoneNumber?.text.toString(),
                itemBinding?.etEmail?.text.toString(),
                itemBinding?.etTTL?.text.toString(),
                db?.UserDao()?.getUser(id!!.toInt())?.Password.toString()
            )
        )
        finish()
    }

}