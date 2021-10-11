package com.example.mastermusic.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.mastermusic.R
import com.example.mastermusic.databinding.FragmentDetailsBinding
import com.example.mastermusic.viewModel.MyViewModel

class DetailFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Inflate the layout for this fragment
        (requireActivity() as AppCompatActivity).run {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        setHasOptionsMenu(true)
        navController = Navigation.findNavController(
            requireActivity(), R.id.fragment
        )

        viewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)

        // This is a part that our class will bind to the layout "fragment_details"
        val binding = FragmentDetailsBinding.inflate(
            inflater, container, false
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            navController.navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

}