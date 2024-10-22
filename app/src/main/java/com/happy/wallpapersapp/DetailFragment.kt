package com.happy.wallpapersapp

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.happy.wallpapersapp.databinding.FragmentDetailBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*


class DetailFragment : Fragment(), View.OnClickListener {

    lateinit var fragmentDetailBinding: FragmentDetailBinding
    internal lateinit var view: View
    private var image : String? = null
    private var wallpaper: WallpapersModel = WallpapersModel()
    private var isOptionVisible : Boolean? = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        fragmentDetailBinding.executePendingBindings()
        view = fragmentDetailBinding.root
        initListners()
        return view
    }

    fun initListners(){
        //set on btn click
        fragmentDetailBinding.wallpaperBtn.setOnClickListener(this)
        fragmentDetailBinding.lockscreenBtn.setOnClickListener(this)
        fragmentDetailBinding.downloadBtn.setOnClickListener(this)
        fragmentDetailBinding.setAsText.setOnClickListener(this)
        fragmentDetailBinding.successText.setOnClickListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_single_progress.visibility = View.VISIBLE
        fragmentDetailBinding.successDialog.visibility = View.GONE
        //set_options.visibility = View.VISIBLE
        var h = fragmentDetailBinding.setOptions.layoutParams
        h.height = 1
        fragmentDetailBinding.setOptions.layoutParams = h
        fragmentDetailBinding.lockscreenIcon.visibility = View.GONE
        fragmentDetailBinding.downloadIcon.visibility = View.GONE
        image = DetailFragmentArgs.fromBundle(requireArguments()).wallpaperImage
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

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.download_btn -> {
                downloadWallpaper()
            }
            R.id.lockscreen_btn -> {
                setLockscreen()
            }
            R.id.wallpaper_btn -> {
                setWallPaper()
            }
            R.id.set_as_text -> {
                toggleOptions()
            }
            R.id.success_text -> {
                fragmentDetailBinding.successDialog.visibility = View.GONE
                fragmentDetailBinding.setOptions.visibility = View.VISIBLE
            }
        }
    }

    fun toggleOptions(){
        if (!isOptionVisible!!){
            var h = fragmentDetailBinding.setOptions.layoutParams
            h.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT_SPREAD
            fragmentDetailBinding.setOptions.layoutParams = h
            fragmentDetailBinding.lockscreenIcon.visibility = View.VISIBLE
            fragmentDetailBinding.downloadIcon.visibility = View.VISIBLE
            fragmentDetailBinding.dividerLine.visibility = View.VISIBLE
            fragmentDetailBinding.setOptions.setPadding(0,45,0,45)
            isOptionVisible = true
        } else {
            var h = fragmentDetailBinding.setOptions.layoutParams
            h.height = 1
            fragmentDetailBinding.setOptions.layoutParams = h
            fragmentDetailBinding.lockscreenIcon.visibility = View.GONE
            fragmentDetailBinding.downloadIcon.visibility = View.GONE
            fragmentDetailBinding.dividerLine.visibility = View.GONE
            isOptionVisible = false
        }
    }

    fun setWallPaper(){
        val bitmap : Bitmap = detail_image.drawable.toBitmap()
        val task :SetWallpaperTask = SetWallpaperTask(requireContext(), bitmap)
        task.execute(true)
        successScreen()
    }

    fun setLockscreen(){
        val bitmap : Bitmap = detail_image.drawable.toBitmap()
        val task :SetLockscreenTask = SetLockscreenTask(requireContext(), bitmap)
        task.execute(true)
        successScreen()

    }

    fun downloadWallpaper(){
        val bitmap : Bitmap = detail_image.drawable.toBitmap()
        val task :DownloadWallpaperTask = DownloadWallpaperTask(requireContext(), bitmap, wallpaper.name + "_" + wallpaper.date)
        task.execute(true)
        successScreen()
    }

    fun successScreen(){

        fragmentDetailBinding.successDialog.visibility = View.VISIBLE
        fragmentDetailBinding.setOptions.visibility = View.GONE
        Handler().postDelayed({
            fragmentDetailBinding.successAnimation.playAnimation()
        },300)
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
        class DownloadWallpaperTask internal constructor(private val context : Context, private val bitmap : Bitmap, private val picName : String) :
            AsyncTask<Boolean, String, String>() {
            override fun doInBackground(vararg p0: Boolean?): String {
                return "Download Wallpaper"
            }
        }
        class SetLockscreenTask internal constructor(private val context : Context, private val bitmap : Bitmap) :
            AsyncTask<Boolean, String, String>() {
            override fun doInBackground(vararg p0: Boolean?): String {
                val wallpaperManager : WallpaperManager = WallpaperManager.getInstance(context)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                    //Toast.makeText(context, "Lockscreen set.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Your Android Version does not support . \n Please Download the image and then set the lockscreen.", Toast.LENGTH_LONG).show()
                }
                return "Lockscreen Set"
            }
        }
    }
}


