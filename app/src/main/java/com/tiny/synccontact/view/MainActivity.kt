package com.tiny.synccontact.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tiny.synccontact.R
import com.tiny.synccontact.databinding.ActivityMainBinding
import com.tiny.synccontact.models.User


class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    var uiD: String? = null
        private set
    private var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        sharedPreferences = getSharedPreferences("com.tiny.synccontact", Context.MODE_PRIVATE)
        uiD = sharedPreferences!!.getString("token", null)
        initialize()
    }


    fun initialize() {
        val adapter = SimpleFragmentPagerAdapter(this, supportFragmentManager)
        binding!!.viewPager.adapter = adapter
        binding!!.tabLayout.setupWithViewPager(binding!!.viewPager)
        binding!!.tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_tab_contacts)
        binding!!.tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_tab_history)
        binding!!.tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_tab_message)
        binding!!.tabLayout.getTabAt(3)!!.setIcon(R.drawable.ic_tab_menu)


    }


    override fun onResume() {
        super.onResume()

    }

    override fun onBackPressed() {
        showAlertDialog()
    }

    fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit")
        builder.setMessage("Are you sure exit?")
        builder.setCancelable(false)
        builder.setPositiveButton("Cancel") { dialogInterface, i -> }
        builder.setNegativeButton("Yes") { dialogInterface, i ->
            dialogInterface.dismiss()
            finish()
        }
        val alertDialog = builder.create()
        alertDialog.show()

    }


    inner class SimpleFragmentPagerAdapter(context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return if (position == 0) {
                ContactsFragment()
            } else if (position == 1) {
                HistoryFragment()
            } else if (position == 2) {
                MessageFragment()
            } else {
                MenuFragment()
            }
        }


        override fun getCount(): Int {
            return 4
        }
    }

    fun signOut() {
        val preferences = getSharedPreferences("com.tiny.synccontact", 0)
        preferences.edit().remove("token").commit()
        val intent = Intent(application, LoginActivity::class.java)
        intent.putExtra("TOKEN", "")
        startActivity(intent)
        finish()
    }
}
