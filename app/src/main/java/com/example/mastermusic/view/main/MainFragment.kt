package com.example.mastermusic.view.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.mastermusic.R
import com.example.mastermusic.model.data.MusicEntityDb
import com.example.mastermusic.viewModel.MyViewModel
import com.google.android.material.appbar.AppBarLayout
import java.util.*

class MainFragment : Fragment(),
    MasterRecyclerViewAdapter.MusicItemListener {

    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var navController: NavController
    private lateinit var recyclerView: RecyclerView
    private lateinit var myViewModel: MyViewModel
    private lateinit var adapter: MasterRecyclerViewAdapter

    private val STANDARD_APPBAR = 0
    private val SEARCH_APPBAR = 1
    private var mAppBarState = 0

    private lateinit var viewBar: AppBarLayout
    private lateinit var searchBar: AppBarLayout

    private lateinit var mSearchArtis: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity() as AppCompatActivity).run {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val view = inflater.inflate(R.layout.main_fragment, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)

        navController = Navigation.findNavController(
            requireActivity(), R.id.fragment
        )

        viewBar = view.findViewById(R.id.viewSerToolbar)
        searchBar = view.findViewById(R.id.searchToolbar)
        mSearchArtis = view.findViewById(R.id.etSearchArt)

        setAppBaeState(STANDARD_APPBAR)

        val ivSearchArti: ImageView = view.findViewById(R.id.ivSearchIcon)
        ivSearchArti.setOnClickListener {
            toggleToolBarState()
        }

        val ivBackArrow: ImageView = view.findViewById(R.id.ivBackArrow)
        ivBackArrow.setOnClickListener {
            toggleToolBarState()
        }

        myViewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        myViewModel.refreshData()
        getDatafromDb()

        swipeLayout = view.findViewById(R.id.swipeLayout)
        swipeLayout.setOnRefreshListener {
            myViewModel.refreshData()
        }

        return view
    }

    private fun getDatafromDb() {
        myViewModel.musicData.observe(viewLifecycleOwner, {
            adapter = MasterRecyclerViewAdapter(
                requireContext(),
                it, this
            )
            recyclerView.adapter = adapter
            swipeLayout.isRefreshing = false
        })
    }

    //Click on each item
    override fun onMusicItemClick(music: MusicEntityDb) {
        myViewModel.selectedMusic.value = music
        navController.navigate(R.id.nav_master_to_details)
    }
//endregion

    // Initiate toggle (it means when you click the search icon it pops up the editText and clicking the back button goes to the search icon again)
    private fun toggleToolBarState() {
        if (mAppBarState == STANDARD_APPBAR) {
            setAppBaeState(SEARCH_APPBAR)
            searchEngine()
        } else {
            setAppBaeState(STANDARD_APPBAR)
            myViewModel.refreshData()
        }
    }

    // Sets the appbar state for either search mode or standard mode.
    private fun setAppBaeState(state: Int) {
        mAppBarState = state
        if (mAppBarState == STANDARD_APPBAR) {
            searchBar.visibility = View.GONE
            viewBar.visibility = View.VISIBLE
            val view = view
            val im: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            try {
                im.hideSoftInputFromWindow(view!!.windowToken, 0) // make keyboard hide
            } catch (e: NullPointerException) {
            }
        } else if (mAppBarState == SEARCH_APPBAR) {
            viewBar.visibility = View.GONE
            searchBar.visibility = View.VISIBLE
            val im: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0) // make keyboard popup
        }
    }

    override fun onResume() {
        super.onResume()
        setAppBaeState(STANDARD_APPBAR)
    }

    private fun searchEngine() {
        mSearchArtis.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count == 0) {
                    myViewModel.refreshData()
                }
            }

            override fun afterTextChanged(s: Editable) {
                val text: String =
                    mSearchArtis.text.toString().lowercase(Locale.getDefault())
                        .trim { it <= ' ' }
                adapter.filter.filter(text)
                recyclerView.adapter = adapter
            }
        })
    }
}