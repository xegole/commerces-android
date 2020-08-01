package com.webster.commerces.ui.tutorial

import android.os.Bundle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.webster.commerces.AppCore
import com.webster.commerces.R
import com.webster.commerces.base.BaseActivity
import com.webster.commerces.extensions.goToActivity
import com.webster.commerces.fragments.TutorialFragment
import com.webster.commerces.ui.login.view.LoginActivity
import kotlinx.android.synthetic.main.activity_tutorial.*
import java.io.Serializable

class TutorialActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        pagerTutorial.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 3
            override fun createFragment(position: Int) =
                TutorialFragment.create(getTutorialList()[position])
        }

        pagerTutorial.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                pageIndicatorView.setSelected(position)
            }
        })

        fabDone.setOnClickListener {
            AppCore.prefs.firstTime = false
            goToActivity(LoginActivity::class.java)
        }
    }

    private fun getTutorialList(): List<TutorialItem> {
        val list = ArrayList<TutorialItem>()
        list.add(
            TutorialItem(
                "\"Vinculate\"",
                "Muestra tu linea de productos desde tu Smartphone",
                0
            )
        )
        list.add(
            TutorialItem(
                "\"Comprobado\"",
                "Las ventas por internet aumentan tus ingresos",
                1
            )
        )
        list.add(
            TutorialItem(
                "",
                "Aumente sus ventas desde casa",
                2
            )
        )
        return list
    }
}

data class TutorialItem(val title: String, val subTitle: String, val position: Int) : Serializable