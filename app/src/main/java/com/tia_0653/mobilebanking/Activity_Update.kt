package com.tia_0653.mobilebanking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tia_0653.mobilebanking.room.User
import com.tia_0653.mobilebanking.room.UserDB
import com.tia_0653.mobilebanking.room.constant
import kotlinx.android.synthetic.main.activity_update.*
import kotlinx.coroutines.*

class Activity_Update : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    private var User : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        setupView()
        setupListener()
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            constant.TYPE_CREATE ->{
                button_update.visibility= View.GONE
            }
            constant.TYPE_READ ->{
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
            }
            constant.TYPE_UPDATE ->{
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
            }

        }
    }

    private fun setupListener(){
        button_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.UserDao().addUser(
                    User(0,
                        Edit_username.text.toString(),
                        Edit_password.text,toString(),
                        Edit_email.text.toString(),
                        Edit_Tanggal.text.toString(),
                        Edit_NoHP.text.toString())
                )
                finish()
            }
        }
        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch{
                db.UserDao().updateUser(
                    User(User,
                        Edit_username.text.toString(),
                        Edit_password.text,toString(),
                        Edit_email.text.toString(),
                        Edit_Tanggal.text.toString(),
                        Edit_NoHP.text.toString())
                )
                    finish()
            }
        }
    }
    fun getUser(){
        User = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val  users = db.UserDao().getUsers(User)[0]
            Edit_username.setText(users.UserName)
            Edit_password.setText(users.Password)
            Edit_email.setText(users.Email)
            Edit_Tanggal.setText(users.TanggalLahir)
            Edit_NoHP.setText(users.TanggalLahir)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}