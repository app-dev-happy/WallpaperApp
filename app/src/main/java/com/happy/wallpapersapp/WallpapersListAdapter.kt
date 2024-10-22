package com.happy.wallpapersapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_single_item.view.*
import kotlinx.coroutines.withContext

class WallpapersListAdapter(var wallpapersList : List<WallpapersModel>, private val clickListener: (WallpapersModel) -> Unit) : RecyclerView.Adapter<WallpapersListAdapter.WallpapersViewHolder>() {

    class WallpapersViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(wallpapers : WallpapersModel, clickListener: (WallpapersModel) -> Unit){

            //Load image
            /*Glide.with(itemView.context).load(wallpapers.thumbnail).listener(
                object : RequestListener<Drawable>(
                    override fun onloa
                )
            ).into(itemView.list_single_image)*/

            itemView.artist_name.text = wallpapers.name
            itemView.gradient.visibility = View.GONE



            //try {
                Picasso.get()
                    .load(wallpapers.thumbnail)
                    .into(itemView.list_single_image, object : Callback {
                        override fun onSuccess() {
                            itemView.list_single_progress.visibility = View.GONE
                            itemView.gradient.visibility = View.VISIBLE
                        }
                        override fun onError(e: Exception?) {

                        }
                    })
           /* } catch (e: Exception) {
                e.printStackTrace()
            }*/

            //click listemner
            itemView.setOnClickListener {
                clickListener(wallpapers)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpapersViewHolder {

        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.list_single_item,parent,false)
        return WallpapersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return wallpapersList.size
    }

    override fun onBindViewHolder(holder: WallpapersViewHolder, position: Int) {
        (holder as WallpapersViewHolder).bind(wallpapersList[position], clickListener)
    }
}