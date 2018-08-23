package com.tiny.synccontact.view

import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tiny.synccontact.R
import com.tiny.synccontact.databinding.FragmentHistoryBinding
import com.tiny.synccontact.databinding.FragmentMenuBinding
import com.tiny.synccontact.models.User
import com.tiny.synccontact.presenter.AdapterRCVMenu
import com.tiny.synccontact.presenter.IOnRecyclerViewItemClickListener

import java.util.ArrayList

class MenuFragment : Fragment(), AdapterRCVMenu.IList, IOnRecyclerViewItemClickListener {
    private var binding: FragmentMenuBinding? = null
    private var menuList: MutableList<String>? = null
    private val firebaseAuth: FirebaseAuth? = null
    private val user: User? = null
    private var uId: String? = null
    private var databaseReference: DatabaseReference? = null
    private var firebaseDatabase: FirebaseDatabase? = null

    override val count: Int
        get() = menuList!!.size

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(inflater.context), R.layout.fragment_menu, container, false)
        uId = (activity as MainActivity).uiD
        initialize()
        return binding!!.root
    }

    fun initialize() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.reference
        menuList = ArrayList()
        menuList!!.add("Call")
        menuList!!.add("Message")
        menuList!!.add("Sync Contact")
        menuList!!.add("About")
        menuList!!.add("Help")
        menuList!!.add("Setting")
        menuList!!.add("Sign Out")
        val adapterRCVMenu = AdapterRCVMenu()
        adapterRCVMenu.setData(this)
        val linearLayoutManager = LinearLayoutManager(context)
        binding!!.rcvMenu.layoutManager = linearLayoutManager
        binding!!.rcvMenu.adapter = adapterRCVMenu
        adapterRCVMenu.setOnItemClickListener(this)
        try {

            databaseReference!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    setUser(dataSnapshot)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding!!.user.text = user!!.name


    }

    override fun getItem(position: Int): String {
        return menuList!![position]
    }

    override fun onRecyclerViewItemClicked(position: Int, id: Int) {
        alertsignout()
    }

    fun alertsignout() {
        val alertDialog2 = AlertDialog.Builder(
                activity!!)

        alertDialog2.setTitle("Confirm SignOut")

        alertDialog2.setMessage("Are you sure you want to Signout?")

        alertDialog2.setPositiveButton("YES"
        ) { dialog, which -> (activity as MainActivity).signOut() }
        alertDialog2.setNegativeButton("NO"
        ) { dialog, which -> dialog.cancel() }
        alertDialog2.show()
    }

    fun setUser(dataSnapshot: DataSnapshot) {
        for (snapshot in dataSnapshot.children) {
            user!!.id = snapshot.child(uId!!).getValue<User>(User::class.java!!)!!.id
            user.name = snapshot.child(uId!!).getValue<User>(User::class.java!!)!!.name
            user.email = snapshot.child(uId!!).getValue<User>(User::class.java!!)!!.email
            user.phone = snapshot.child(uId!!).getValue<User>(User::class.java!!)!!.phone


        }
    }
}
