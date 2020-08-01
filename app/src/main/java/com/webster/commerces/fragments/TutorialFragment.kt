package com.webster.commerces.fragments

import android.os.Bundle
import android.view.View
import com.webster.commerces.R
import com.webster.commerces.base.BaseFragment
import com.webster.commerces.ui.tutorial.TutorialItem
import kotlinx.android.synthetic.main.fragment_tutorial.*

class TutorialFragment : BaseFragment() {

    override fun resourceLayout() = R.layout.fragment_tutorial

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments?.getSerializable(ARGS_BUNDLE) as TutorialItem
        labelTitle.visibility = if (data.title.isEmpty()) View.GONE else View.VISIBLE
        labelTitle.text = data.title
        labelSubTitle.text = data.subTitle
        imageTutorial.setImageResource(getImage(data.position))
    }

    private fun getImage(position: Int): Int {
        val drawableName = "img_tutorial_$position"
        return resources.getIdentifier(drawableName, "drawable", context?.packageName)
    }

    companion object {

        private val ARGS_BUNDLE = TutorialFragment::class.java.name + ":Bundle"

        fun create(item: TutorialItem): TutorialFragment {
            val fragment = TutorialFragment()
            val extras = Bundle()
            extras.putSerializable(ARGS_BUNDLE, item)
            fragment.arguments = extras
            return fragment
        }
    }
}