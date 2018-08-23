package com.tiny.synccontact.view

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.tiny.synccontact.R
import com.tiny.synccontact.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityIntroBinding? = null
    private val adapter: SimpleFragmentPagerAdapter? = null
    private var position: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)
        initialize()
    }

    fun initialize() {
        val adapter = SimpleFragmentPagerAdapter(this, supportFragmentManager)
        binding!!.viewPager.setPageTransformer(false, FadePageTransformer())
        binding!!.viewPager.adapter = adapter
        binding!!.tabDots.setupWithViewPager(binding!!.viewPager, true)
        position = binding!!.viewPager.currentItem
        when (position) {
            0 -> {
                binding!!.btnPrevious.visibility = View.GONE
                binding!!.btnNext.visibility = View.VISIBLE
                binding!!.btnSkip.visibility = View.GONE
            }
            1 -> {
                binding!!.btnPrevious.visibility = View.VISIBLE
                binding!!.btnNext.visibility = View.VISIBLE
                binding!!.btnSkip.visibility = View.GONE
            }
            2 -> {
                binding!!.btnPrevious.visibility = View.VISIBLE
                binding!!.btnNext.visibility = View.VISIBLE
                binding!!.btnSkip.visibility = View.GONE
            }
            3 -> {
                binding!!.btnPrevious.visibility = View.VISIBLE
                binding!!.btnNext.visibility = View.VISIBLE
                binding!!.btnSkip.visibility = View.GONE
            }
            4 -> {
                binding!!.btnPrevious.visibility = View.VISIBLE
                binding!!.btnNext.visibility = View.GONE
                binding!!.btnSkip.visibility = View.VISIBLE
            }
            else -> {
                binding!!.btnPrevious.visibility = View.VISIBLE
                binding!!.btnNext.visibility = View.VISIBLE
                binding!!.btnSkip.visibility = View.GONE
            }
        }
        binding!!.btnNext.setOnClickListener(this)
        binding!!.btnPrevious.setOnClickListener(this)
        binding!!.btnSkip.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_next -> nextFragment(binding!!.root)
            R.id.btn_previous -> previousFragment(binding!!.root)
            R.id.btn_skip -> skipActivity()
        }


    }

    inner class SimpleFragmentPagerAdapter(context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            if (position == 0) {
                val argumentFragment = IntroFragment()
                val data = Bundle()
                data.putInt("data", position)
                argumentFragment.arguments = data
                return argumentFragment
            } else if (position == 1) {
                val argumentFragment = IntroFragment()
                val data = Bundle()
                data.putInt("data", position)
                argumentFragment.arguments = data
                return argumentFragment
            } else if (position == 2) {
                val argumentFragment = IntroFragment()
                val data = Bundle()
                data.putInt("data", position)
                argumentFragment.arguments = data
                return argumentFragment
            } else if (position == 3) {
                val argumentFragment = IntroFragment()
                val data = Bundle()
                data.putInt("data", position)
                argumentFragment.arguments = data
                return argumentFragment
            } else if (position == 4) {
                val argumentFragment = IntroFragment()
                val data = Bundle()
                data.putInt("data", position)
                argumentFragment.arguments = data
                return argumentFragment
            } else {
                val argumentFragment = IntroFragment()
                val data = Bundle()
                data.putInt("data", position)
                argumentFragment.arguments = data
                return argumentFragment
            }

        }

        override fun getCount(): Int {
            return 5
        }

    }

    fun nextFragment(view: View) {
        binding!!.viewPager.currentItem = binding!!.viewPager.currentItem + 1
        position = binding!!.viewPager.currentItem
        when (position) {
            0 -> {
                binding!!.btnPrevious.visibility = View.GONE
                binding!!.btnNext.visibility = View.VISIBLE
                binding!!.btnSkip.visibility = View.GONE
            }
            1 -> {
                binding!!.btnPrevious.visibility = View.VISIBLE
                binding!!.btnNext.visibility = View.VISIBLE
                binding!!.btnSkip.visibility = View.GONE
            }
            2 -> {
                binding!!.btnPrevious.visibility = View.VISIBLE
                binding!!.btnNext.visibility = View.VISIBLE
                binding!!.btnSkip.visibility = View.GONE
            }
            3 -> {
                binding!!.btnPrevious.visibility = View.VISIBLE
                binding!!.btnNext.visibility = View.VISIBLE
                binding!!.btnSkip.visibility = View.GONE
            }
            4 -> {
                binding!!.btnPrevious.visibility = View.VISIBLE
                binding!!.btnNext.visibility = View.GONE
                binding!!.btnSkip.visibility = View.VISIBLE
            }
            else -> {
                binding!!.btnPrevious.visibility = View.VISIBLE
                binding!!.btnNext.visibility = View.VISIBLE
                binding!!.btnSkip.visibility = View.GONE
            }
        }
    }

    fun previousFragment(view: View) {
        binding!!.viewPager.currentItem = binding!!.viewPager.currentItem - 1
        position = binding!!.viewPager.currentItem
        when (position) {
            0 -> {
                binding!!.btnPrevious.visibility = View.GONE
                binding!!.btnNext.visibility = View.VISIBLE
                binding!!.btnSkip.visibility = View.GONE
            }
            1 -> {
                binding!!.btnPrevious.visibility = View.VISIBLE
                binding!!.btnNext.visibility = View.VISIBLE
                binding!!.btnSkip.visibility = View.GONE
            }
            2 -> {
                binding!!.btnPrevious.visibility = View.VISIBLE
                binding!!.btnNext.visibility = View.VISIBLE
                binding!!.btnSkip.visibility = View.GONE
            }
            3 -> {
                binding!!.btnPrevious.visibility = View.VISIBLE
                binding!!.btnNext.visibility = View.VISIBLE
                binding!!.btnSkip.visibility = View.GONE
            }
            4 -> {
                binding!!.btnPrevious.visibility = View.VISIBLE
                binding!!.btnNext.visibility = View.GONE
                binding!!.btnSkip.visibility = View.VISIBLE
            }
            else -> {
                binding!!.btnPrevious.visibility = View.VISIBLE
                binding!!.btnNext.visibility = View.VISIBLE
                binding!!.btnSkip.visibility = View.GONE
            }
        }
    }

    fun skipActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    inner class FadePageTransformer : ViewPager.PageTransformer {
        override fun transformPage(view: View, position: Float) {

            if (position < -1 || position > 1) {
                view.alpha = 0f
            } else if (position <= 0 || position <= 1) {
                val alpha = if (position <= 0) position + 1 else 1 - position
                view.alpha = alpha
            } else if (position == 0f) {
                view.alpha = 1f
            }
        }
    }

}
