package com.happy.wallpapersapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot

class WallpapersViewModel : ViewModel() {

    private val firebaseRepositories : FirebaseRepositories = FirebaseRepositories()

    private val wallpapersList : MutableLiveData<List<WallpapersModel>> by lazy {
        MutableLiveData<List<WallpapersModel>>().also {
            loadWallpapersData()
        }
    }

    fun getWallpapersList(): LiveData<List<WallpapersModel>> {
        return wallpapersList
    }

    fun loadWallpapersData() {
        //call for wallpapers query
        firebaseRepositories.queryWallpapers().addOnCompleteListener {
            if (it.isSuccessful){

                val result = it.result

                if (result!!.isEmpty){
                    //no more results to load, reached at the bottom
                }else{
                    //results are ready to be loaded
                    if (wallpapersList.value == null) {
                        //loading first page
                        wallpapersList.value = result.toObjects(WallpapersModel::class.java)
                    }else{
                        //loading next page
                        wallpapersList.value = wallpapersList.value!!.plus(result.toObjects(WallpapersModel::class.java))
                    }

                    //get the last document
                    val lastItem : DocumentSnapshot = result.documents[result.size() - 1]
                    firebaseRepositories.lastVisisble = lastItem
                }

            }else {
                Log.d("WallpapersViewModelErr", "Error ${it.exception!!.message} ")
            }
        }
    }
}