package com.tia_0653.mobilebanking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import timber.log.Timber

class HomeActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    private lateinit var mAnimatedDialog: MaterialDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        loadFragment(Fragment_nama_bank())
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView


        mAnimatedDialog = MaterialDialog.Builder(this)
            .setTitle("Exit")
            .setMessage("Apakah anda yakin ingin keluar dari aplikasi ini?")
            .setCancelable(false)
            .setPositiveButton(
                "Exit", R.drawable.ic_baseline_exit_to_app_24
            ) { dialogInterface, i ->
                val intent = Intent(this,loginView::class.java)
                startActivity(intent)
                dialogInterface.dismiss()
            }
            .setNegativeButton(
                "Cancel", R.drawable.ic_baseline_close
            ) { dialogInterface, which ->
                Toast.makeText(applicationContext, "Cancelled!", Toast.LENGTH_SHORT)
                    .show()
                dialogInterface.dismiss()
            }
            .setAnimation("delete_anim.json")
            .build()


        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
//                R.id.namaBank-> {
//                    loadFragment(Fragment_nama_bank())
//                    return@setOnNavigationItemReselectedListener
//                }

                R.id.profile -> {
                    loadFragment(FragmentProfile())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.Transaksi ->{
                    val intent = Intent(this,TransaksiView::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.Qrcode->{
                    val intent = Intent(this,QRcode::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }


                R.id.Pembayaran->{
                    val intent = Intent(this,PembayaranView::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.exit -> {


                    mAnimatedDialog.show()

                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
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