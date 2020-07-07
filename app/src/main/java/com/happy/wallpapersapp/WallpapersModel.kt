package com.happy.wallpapersapp

import com.google.firebase.Timestamp

data class WallpapersModel(

    //for wallpapers
    val name: String = "",
    val image: String = "",
    val thumbnail: String = "",
    val date: Timestamp? = null,

    //for categories
    val categoryName: String = "",
    val categoryRank: Int? = null
)
