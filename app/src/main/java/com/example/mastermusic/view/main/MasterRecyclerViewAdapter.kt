package com.example.mastermusic.view.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mastermusic.R
import com.example.mastermusic.model.data.MusicEntityDb

class MasterRecyclerViewAdapter(
    private val context: Context,
    private val musics: List<MusicEntityDb>,
    private val itemListener: MusicItemListener
) :

    RecyclerView.Adapter<MasterRecyclerViewAdapter.ViewHolder>(), Filterable {

    var exampleList = ArrayList<MusicEntityDb>()

    init {
        exampleList = musics as ArrayList<MusicEntityDb>
    }

    override fun getItemCount(): Int = exampleList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = exampleList[position]
        with(holder) {
            trackName.text = music.trackName
            artistName.text = music.artistName

            Glide.with(context)
                .load(music.thImageFile)
                .into(thArtWork)

            holder.itemView.setOnClickListener{
                itemListener.onMusicItemClick(music)
            }
        }
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val trackName = itemView.findViewById<TextView>(R.id.track_name)!!
        val artistName = itemView.findViewById<TextView>(R.id.artis_name)!!
        val thArtWork = itemView.findViewById<ImageView>(R.id.imageForList)!!
    }

    //Interface on each item click
    interface MusicItemListener {
        fun onMusicItemClick(music: MusicEntityDb)
    }

//For filtering
    override fun getFilter(): Filter {
        return exampleFilter
    }

    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<MusicEntityDb> = ArrayList()
            if (constraint.isEmpty()) {
                exampleList.let { filteredList.addAll(it) }
            } else {
                val filterPattern = constraint.toString().lowercase().trim { it <= ' ' }
                for (item in exampleList) {
                    if (item.artistName.lowercase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            exampleList.clear()
            exampleList.addAll(results.values as Collection<MusicEntityDb>)
            notifyDataSetChanged()
        }
    }

}