package com.tiny.synccontact.view

import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.tiny.synccontact.R
import com.tiny.synccontact.databinding.ActivityForgetPasswordBinding

import java.util.regex.Matcher
import java.util.regex.Pattern

class ForgetPassActivity : AppCompatActivity(), OnClickListener {
    private var binding: ActivityForgetPasswordBinding? = null
    private var email: String? = null
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password)
        email = intent.getStringExtra("EMAIL")
        initialize()
    }

    private fun initialize() {
        firebaseAuth = FirebaseAuth.getInstance()
        binding!!.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding!!.txtError.visibility = View.GONE
                email = binding!!.edtEmail.text.toString()
                if (!validateEmailAddress(email)) {
                    binding!!.edtEmail.error = "Invalid email!"
                    binding!!.txtError.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

        binding!!.btnContinue.setOnClickListener(this)
        binding!!.btnBack.setOnClickListener(this)
    }

    fun validateEmailAddress(emailAddress: String?): Boolean {

        val regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$")
        val regMatcher = regexPattern.matcher(emailAddress!!)
        return regMatcher.matches()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_continue -> {
                email = binding!!.edtEmail.text.toString()
                if (email!!.isEmpty()) {

                } else if (binding!!.edtEmail.error != null) {
                    binding!!.edtEmail.error = "Invalid email!"
                    binding!!.txtError.visibility = View.VISIBLE
                } else {
                    sendPasswordReset(email)
                }
            }
            R.id.btn_back -> finish()
            else -> {
            }
        }
    }

    fun sendPasswordReset(email: String) {
        binding!!.progressBar.visibility = View.VISIBLE
        firebaseAuth!!.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    binding!!.progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        showAlertDialog()
                    } else {
                        try {
                            throw task.getException()
                        } catch (e: Exception) {
                            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                        }

                    }
                }
    }

    fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Finish!")
        builder.setMessage("We have sent a link to your email. Please check your email and change your password!")
        builder.setCancelable(false)
        builder.setNegativeButton("OK") { dialogInterface, i ->
            dialogInterface.dismiss()
            finish()
        }
        val alertDialog = builder.create()
        alertDialog.show()

    }
}
