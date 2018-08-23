package com.tiny.synccontact.view

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.tiny.synccontact.R
import com.tiny.synccontact.databinding.FragmentIntroBinding

import com.firebase.ui.auth.AuthUI.TAG

class IntroFragment : Fragment() {
    private var binding: FragmentIntroBinding? = null
    private var position: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(inflater.context), R.layout.fragment_intro, container, false)
        position = arguments!!.getInt("data")
        initialize()
        return binding!!.root
    }

    fun initialize() {
        when (position) {
            0 -> {
                binding!!.imgInto.setImageResource(R.drawable.img_broke_phone)
                binding!!.txtIntro.text = "Your phone was damaged!"
            }
            1 -> {
                binding!!.imgInto.setImageResource(R.drawable.img_broke_sim)
                binding!!.txtIntro.text = "Your sim card was damaged!"
            }
            2 -> {
                binding!!.imgInto.setImageResource(R.drawable.img_dont_worry)
                binding!!.txtIntro.text = "Don't worry! we will help you."
            }
            3 -> {
                binding!!.imgInto.setImageResource(R.drawable.img_sync)
                binding!!.txtIntro.text = "Synchronize contacts!"
            }
            4 -> {
                binding!!.imgInto.setImageResource(R.drawable.img_do)
                binding!!.txtIntro.text = "Let's do this!"
            }
            else -> {
            }
        }
    }


}
