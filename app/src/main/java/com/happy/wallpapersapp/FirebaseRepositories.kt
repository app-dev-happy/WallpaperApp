package com.happy.wallpapersapp

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class FirebaseRepositories {
    private var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseFirestore : FirebaseFirestore = FirebaseFirestore.getInstance()

    var lastVisisble : DocumentSnapshot? = null
    private var pageSize : Long = 6

    fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun queryWallpapers(): Task<QuerySnapshot> {

        if (lastVisisble == null){
            //load first page
            return firebaseFirestore
                .collection("Wallpapers")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(9)
                .get()
        } else{
            //load next page
            return firebaseFirestore
                .collection("Wallpapers")
                .orderBy("date", Query.Direction.DESCENDING)
                .startAfter(lastVisisble!!)
                .limit(pageSize)
                .get()
        }


    }






}