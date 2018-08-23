package com.tiny.synccontact.view

import android.content.Intent
import android.content.SharedPreferences
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tiny.synccontact.R
import com.tiny.synccontact.databinding.ActivityLoginBinding
import com.tiny.synccontact.models.Account

import java.util.regex.Matcher
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityLoginBinding? = null
    private var email: String? = null
    private var password: String? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var sharedPreferences: SharedPreferences? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        email = intent.getStringExtra("EMAIL")
        password = intent.getStringExtra("PASSWORD")
        sharedPreferences = getSharedPreferences("com.tiny.synccontact", Context.MODE_PRIVATE)
        token = intent.getStringExtra("TOKEN")
        checkLogin()
    }

    override fun onResume() {
        super.onResume()
        if (sharedPreferences!!.getBoolean("firstrun", true)) {
            sharedPreferences!!.edit().putBoolean("firstrun", false).commit()
            startActivity(Intent(this, IntroActivity::class.java))
            finish()
        } else
            initialize()
    }


    fun checkLogin() {
        token = sharedPreferences!!.getString("token", null)
        if (token != null) {
            val intent = Intent(application, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else
            initialize()
    }

    fun initialize() {

        firebaseAuth = FirebaseAuth.getInstance()
        val account = Account(email, password)
        binding!!.account = account
        binding!!.btnLogin.setOnClickListener(this)
        binding!!.btnSignUp.setOnClickListener(this)
        binding!!.btnForgetPass.setOnClickListener(this)
        binding!!.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding!!.txtError.visibility = View.GONE
                email = binding!!.edtEmail.text.toString()
                if (!validateEmailAddress(email)) {
                    binding!!.edtEmail.error = "Invalid email!"
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

        binding!!.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding!!.txtError.visibility = View.GONE
                password = binding!!.edtPassword.text.toString()
                if (password!!.length < 6) {
                    binding!!.edtPassword.error = "Password must more than 6 characters!"
                }

            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

    }

    override fun onClick(view: View) {
        val intent: Intent
        val userName: String
        val password: String
        when (view.id) {
            R.id.btn_login -> {
                email = binding!!.edtEmail.text.toString()
                password = binding!!.edtPassword.text.toString()
                userLogin()
            }
            R.id.btn_sign_up -> {
                userName = binding!!.edtEmail.text.toString()
                password = binding!!.edtPassword.text.toString()
                intent = Intent(this, SignUpActivity::class.java)
                intent.putExtra("EMAIL", userName)
                intent.putExtra("PASSWORD", password)
                startActivity(intent)
            }
            R.id.btn_forget_pass -> {
                userName = binding!!.edtEmail.text.toString()
                intent = Intent(this, ForgetPassActivity::class.java)
                intent.putExtra("EMAIL", userName)
                startActivity(intent)
            }
            else -> {
            }
        }
    }

    private fun userLogin() {
        if (email!!.isEmpty() || password!!.isEmpty()) {
            return
        }
        binding!!.progressBar.visibility = View.VISIBLE
        firebaseAuth!!.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener(OnCompleteListener { task ->
            binding!!.progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                val token = firebaseAuth!!.uid
                val editor = sharedPreferences!!.edit()
                editor.putString("token", token)
                editor.apply()
                val intent = Intent(application, MainActivity::class.java)
                intent.putExtra("TOKEN", token)
                startActivity(intent)
                finish()
                return@OnCompleteListener
            } else {
                binding!!.txtError.visibility = View.VISIBLE
                return@OnCompleteListener
            }
        })
    }

    fun validateEmailAddress(emailAddress: String?): Boolean {

        val regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$")
        val regMatcher = regexPattern.matcher(emailAddress!!)
        return regMatcher.matches()
    }


}
