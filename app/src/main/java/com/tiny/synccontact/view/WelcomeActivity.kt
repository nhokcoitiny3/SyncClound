package com.tiny.synccontact.view

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils

import com.tiny.synccontact.R
import com.tiny.synccontact.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private var binding: ActivityWelcomeBinding? = null
    private var mThread: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        startAnimation()
    }

    private fun startAnimation() {
        val translate = AnimationUtils.loadAnimation(this, R.anim.translate)
        val translateTop = AnimationUtils.loadAnimation(this, R.anim.translate_top)

        translate.reset()

        binding!!.text.animation = translate
        binding!!.image.animation = translateTop
        mThread = object : Thread() {
            override fun run() {
                super.run()
                var waited = 0
                while (waited < 3500) {
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    waited += 100
                }
                this@WelcomeActivity.finish()
                val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
            }
        }
        mThread!!.start()
    }
}