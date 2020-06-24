package com.happy.wallpapersapp

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.list_single_item.view.*
import java.sql.Time

class DetailFragment : Fragment(), View.OnClickListener {

    private var image : String? = null
    private var wallpaper: WallpapersModel = WallpapersModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_single_progress.visibility = View.VISIBLE
        image = DetailFragmentArgs.fromBundle(requireArguments()).wallpaperImage


        //set on btn click
        detail_set_btn.setOnClickListener(this)

    }

    override fun onStart() {
        super.onStart()
        if (image != null){
            //set Image
            Picasso.get()
                .load(image)
                .into(detail_image, object : Callback {
                    override fun onSuccess() {
                        list_single_progress.visibility = View.GONE
                    }
                    override fun onError(e: Exception?) {
                        list_single_progress.visibility = View.GONE
                    }
                })
        }
    }

    override fun onClick(p0: View?) {
        setWallPaper()
    }

    fun setWallPaper(){

        detail_set_btn.isEnabled = false
        detail_set_btn.text = "Wallpaper Set"
        detail_set_btn.setTextColor(resources.getColor(R.color.black))

        val bitmap : Bitmap = detail_image.drawable.toBitmap()
        val task :SetWallpaperTask = SetWallpaperTask(requireContext(), bitmap)
        task.execute(true)
    }

    companion object{
        class SetWallpaperTask internal constructor(private val context : Context, private val bitmap : Bitmap) :
            AsyncTask<Boolean, String, String>() {
            override fun doInBackground(vararg p0: Boolean?): String {
                val wallpaperManager : WallpaperManager = WallpaperManager.getInstance(context)
                wallpaperManager.setBitmap(bitmap)
                return "Wallpaper Set"
            }
        }
    }
}


