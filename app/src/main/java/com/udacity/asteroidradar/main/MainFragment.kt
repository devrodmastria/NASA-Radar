package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_overflow_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.show_weekly_menu) {
                    return true
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val imageView: ImageView = binding.activityMainImageOfTheDay
        imageView.contentDescription = "Image name"

        val imageURL = "https://apod.nasa.gov/apod/image/2210/NGC1300_HST_1080.jpg"
        Glide.with(imageView.context).load(imageURL).into(imageView)

        return binding.root
    }

}
