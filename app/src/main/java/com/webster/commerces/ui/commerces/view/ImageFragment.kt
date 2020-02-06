package com.webster.commerces.ui.commerces.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.webster.commerces.R
import com.yanzhenjie.album.AlbumFile
import kotlinx.android.synthetic.main.fragment_image.*
import java.io.File

const val EXTRA_FILE_IMAGE = "extra_file_image"

class ImageFragment : Fragment() {

    private var fileImage: AlbumFile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileImage = arguments?.getParcelable(EXTRA_FILE_IMAGE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fileImage?.let {
            val file = File(it.path)
            Picasso.get().load(file).into(imageFile)
        }
    }

    companion object {
        fun newInstance(file: AlbumFile): ImageFragment {
            val fragment = ImageFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_FILE_IMAGE, file)
            fragment.arguments = bundle
            return fragment
        }
    }
}