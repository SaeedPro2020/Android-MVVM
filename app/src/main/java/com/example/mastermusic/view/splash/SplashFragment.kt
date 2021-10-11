package com.example.mastermusic.view.splash

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.mastermusic.R

class SplashFragment : Fragment() {

    private lateinit var topAnimation: Animation
    private lateinit var flowerImageView: ImageView
    private lateinit var covereImage: ImageView
    private lateinit var myRelative: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        topAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.togo)
        flowerImageView = view.findViewById(R.id.musicImageView)
        covereImage = view.findViewById(R.id.coverImage)
        myRelative = view.findViewById(R.id.container)

        Handler(Looper.getMainLooper()).postDelayed({
            covereImage.isVisible = false
        }, 2000)

        view.post {
            covereImage.startAnimation(topAnimation)
        }

        if (networkAvailable()) {
            Handler(Looper.getMainLooper()).postDelayed({
                displayMainFragment()
            }, 3000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                showMessageBox("There is a problem with your internet connection")
            }, 3000)
        }

        return view
    }

    private fun displayMainFragment() {
        val navController = Navigation.findNavController(
            requireActivity(),
            R.id.fragment
        )
        navController.navigate(
            R.id.nav_to_mainFragment, null,
            NavOptions.Builder()
                .setPopUpTo(R.id.splashFragment, true)
                .build()
        )
    }

    //Network Checking
    private fun networkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        } else {
            return false
        }
        capabilities.also {
            if (it != null){
                if (it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                    return true
                else if (it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                    return true
                }
            }
        }
        return false
    }

    // Alert for connectivity checking
    private fun showMessageBox(text: String) {

        val builder1 = AlertDialog.Builder(
            requireContext()
        )
        builder1.setMessage(text)
        builder1.setCancelable(true)

        builder1.setPositiveButton("OK"
        ) { _, _ ->

        }

        val alert11 = builder1.create()
        alert11.show()

        alert11.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            var wantToCloseDialog = false

            if (!networkAvailable()) {
                Toast.makeText(
                    requireContext(), "Your internet connection still has a problem",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                wantToCloseDialog = true
            }
            if (wantToCloseDialog){
                alert11.dismiss()
                displayMainFragment()
            }
        }
    }

}