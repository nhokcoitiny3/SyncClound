package com.tiny.synccontact.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tiny.synccontact.R
import com.tiny.synccontact.databinding.FragmentContactBinding
import com.tiny.synccontact.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {

    internal lateinit var binding: FragmentHistoryBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(inflater.context), R.layout.fragment_history, container, false)
        initialize()
        return binding.root
    }

    fun initialize() {}
}
