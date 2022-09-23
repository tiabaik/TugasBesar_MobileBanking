package com.tia_0653.mobilebanking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

class HomeActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        loadFragment(Fragment_nama_bank())
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.namaBank-> {
                    loadFragment(Fragment_nama_bank())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.profile -> {
                    loadFragment(FragmentProfile())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.exit -> {
                    val intent = Intent(this, loginView::class.java)
                    startActivity(intent)
                }
            }
        }
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    fun setActivity(activity: AppCompatActivity){
        val moveActivity = Intent(this, activity::class.java)
        startActivity(moveActivity)
    }
}