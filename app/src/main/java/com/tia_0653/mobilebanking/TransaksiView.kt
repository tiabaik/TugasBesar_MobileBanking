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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.tia_0653.mobilebanking.databinding.TransaksiViewBinding
import com.tia_0653.mobilebanking.room.Transaksi
import com.tia_0653.mobilebanking.room.User
import com.tia_0653.mobilebanking.room.UserDB
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class TransaksiView : AppCompatActivity() {
    private lateinit var itemBinding: TransaksiViewBinding

    private lateinit var signUpLayout: ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        itemBinding = TransaksiViewBinding.inflate(layoutInflater)
        val view = itemBinding.root
        setContentView(view)


        itemBinding.etTanggalTransaksi.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    itemBinding.etTanggalTransaksi.setText("" + dayOfMonth + "/" + monthOfYear + "/" + year)

                }, year, month, day
            )

            dpd.show()
        }

        itemBinding.btnTransaksi.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, FragmentBuktiTransaksi::class.java)

            val Name: String = itemBinding.tilUserName.editText?.getText().toString()
            val TanggalTransaksi: String = itemBinding.etTanggalTransaksi.getText().toString()
            val jumlahUang: String = itemBinding.tiljumlahUang.editText?.getText().toString()


            var checkSignUp = true

            if (Name.isEmpty()) {
                itemBinding.tilUserName.setError("Name must be filled with text")
                checkSignUp = false
            }


            if (TanggalTransaksi.isEmpty()) {
                itemBinding.etTanggalTransaksi.setError("Tanggal Transaksi must be filled with text")
                checkSignUp = false
            }


            if (jumlahUang.isEmpty()) {
                itemBinding.tiljumlahUang.setError("Phone Number must be filled with text")
                checkSignUp = false
            }


            if (checkSignUp == true) {
                // simpan data ke database
                val db by lazy { UserDB(this) }
                val userDao = db.TransaksiDao()

                val user = Transaksi(0, Name, TanggalTransaksi, jumlahUang)
                userDao.addTransaksi(user)

                val movetoHomeActivity= Intent(this, HomeActivity::class.java)
                val bundle: Bundle = Bundle()
                bundle.putString("username", Name)
                bundle.putString("TanggalTransaksi", TanggalTransaksi)
                bundle.putString("Jumlah Uang", jumlahUang)

                startActivity(movetoHomeActivity)
            } else {
                return@OnClickListener
            }


        })


    }
}