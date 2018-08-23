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
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tiny.synccontact.R
import com.tiny.synccontact.models.Account
import com.tiny.synccontact.databinding.ActivitySignUpBinding
import com.tiny.synccontact.models.User

import java.util.ArrayList
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivitySignUpBinding? = null
    private var email: String? = null
    private var password: String? = null
    private var cfPassword: String? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        email = intent.getStringExtra("EMAIL")
        password = intent.getStringExtra("PASSWORD")
        initialize()
    }

    fun initialize() {
        firebaseAuth = FirebaseAuth.getInstance()
        val account = Account(email, password)
        binding!!.account = account
        binding!!.btnSignUp.setOnClickListener(this)
        binding!!.btnLogin.setOnClickListener(this)

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

        binding!!.edtCfPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding!!.txtError.visibility = View.GONE
                password = binding!!.edtPassword.text.toString()
                cfPassword = binding!!.edtCfPassword.text.toString()
                if (cfPassword != password) {
                    binding!!.edtCfPassword.error = "Does not match password!"
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_login -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("EMAIL", email)
                intent.putExtra("PASSWORD", password)
                startActivity(intent)
                finish()
            }
            R.id.btn_sign_up -> {
                if (binding!!.edtEmail.text == null) {
                    return
                } else if (binding!!.edtPassword.text.toString() == null) {
                    return
                } else if (binding!!.edtCfPassword.text.toString() == null) {
                    return
                } else {
                    email = binding!!.edtEmail.text.toString()
                    password = binding!!.edtPassword.text.toString()
                    cfPassword = binding!!.edtCfPassword.text.toString()
                }
                if (binding!!.edtEmail.error == null &&
                        binding!!.edtPassword.error == null &&
                        binding!!.edtCfPassword.error == null) {
                    register()

                } else if (!binding!!.edtEmail.error.toString().isEmpty()) {
                    binding!!.txtError.text = binding!!.edtEmail.error.toString()
                    return
                } else if (!binding!!.edtPassword.error.toString().isEmpty()) {
                    binding!!.txtError.text = binding!!.edtPassword.error.toString()
                    return
                } else if (!binding!!.edtCfPassword.error.toString().isEmpty()) {
                    binding!!.txtError.text = binding!!.edtCfPassword.error.toString()
                    return
                }
            }
            else -> {
            }
        }
    }

    private fun register() {

        if (email!!.isEmpty()) {
            binding!!.txtError.text = "Not allowed to empty email !"
            binding!!.txtError.visibility = View.VISIBLE
            return
        }
        if (password!!.isEmpty()) {
            binding!!.txtError.text = "Not allowed to empty password!"
            binding!!.txtError.visibility = View.VISIBLE
            return
        }

        binding!!.progressBar.visibility = View.VISIBLE
        if (password == cfPassword) {
            firebaseAuth!!.createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this, OnCompleteListener { task ->
                        binding!!.progressBar.visibility = View.GONE
                        if (task.isSuccessful) {
                            val uid = firebaseAuth!!.uid
                            val user = User(uid, email, email, "")
                            addNewUser(user)
                            val intent = Intent(application, LoginActivity::class.java)
                            intent.putExtra("EMAIL", email)
                            intent.putExtra("PASSWORD", password)
                            startActivity(intent)
                            finish()
                            return@OnCompleteListener
                        } else {

                            if (task.exception is FirebaseAuthUserCollisionException) {
                                binding!!.txtError.text = "Account already exists!"
                                binding!!.txtError.visibility = View.VISIBLE
                                return@OnCompleteListener
                            } else {
                                binding!!.txtError.text = "Error!"
                                binding!!.txtError.visibility = View.VISIBLE
                            }
                        }
                    })
            /*    final FirebaseUser user = firebaseAuth.getCurrentUser();
            user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                showAlertDialog();
                            } else {
                                Log.e(TAG, "sendEmailVerification", task.getException());
                                Toast.makeText(getApplicationContext(),
                                        "Failed to send verification email.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/
        }
    }

    fun validateEmailAddress(emailAddress: String?): Boolean {

        val regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$")
        val regMatcher = regexPattern.matcher(emailAddress!!)
        return regMatcher.matches()
    }

    fun addNewUser(user: User) {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference!!.child("users").child(user.id!!).setValue(user)
    }

    companion object {

        private val TAG = "100"
    }
    /* public void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Finish!");
        builder.setMessage("We have sent a link verification to your email. Please check your email !");
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }*/
}
