package com.happy.wallpapersapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class HomeFragment : Fragment(), (WallpapersModel) -> Unit {

    private val firebaseRepositories: FirebaseRepositories = FirebaseRepositories()
    private var navController : NavController? = null

    private var wallpapersList : List<WallpapersModel> = ArrayList()
    private val wallpapersListAdapter : WallpapersListAdapter = WallpapersListAdapter(wallpapersList, this)

    private var isLoading : Boolean = true
    private val wallpapersViewModel : WallpapersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Init ActionBar
        (activity as AppCompatActivity).setSupportActionBar(main_toolbar)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar!!.title = "Free Wallpapers"

        //Setup Nav controller
        navController = Navigation.findNavController(view)

        //Check if user is logged in
        if (firebaseRepositories.getUser() == null){
            //User not logged in, go to register page
            navController!!.navigate(R.id.action_homeFragment_to_registerFragment)
        }

        //Init Wallpaper Recyclerview
        wallpapers_list_view.setHasFixedSize(true)
        wallpapers_list_view.layoutManager = GridLayoutManager(context, 2)
        wallpapers_list_view.adapter = wallpapersListAdapter

        //reached bottom of the page
        wallpapers_list_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE){
                    //reached at the bottom and not scrolling
                    if (!isLoading){
                        //Load next page
                        wallpapersViewModel.loadWallpapersData()
                        isLoading = true
                    }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        wallpapersViewModel.getWallpapersList().observe(viewLifecycleOwner, Observer {
            wallpapersList = it
            wallpapersListAdapter.wallpapersList = wallpapersList
            wallpapersListAdapter.notifyDataSetChanged()

            //loading complete
            isLoading = false
        })
    }


    override fun invoke(wallpaper: WallpapersModel) {
        //clicked on wallpaper item from the list, navigate to details fragment
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(wallpaper.image)
        navController!!.navigate(action)
    }
}