package com.demo.cl.movieguidekotlin.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.demo.cl.movieguidekotlin.BR
import com.demo.cl.movieguidekotlin.R
import com.demo.cl.movieguidekotlin.data.Movie

class MoviePageListAdapter:PagedListAdapter<Movie,ViewBindingHolder>(object : DiffUtil.ItemCallback<Movie?>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return true
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewBindingHolder {
        return ViewBindingHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_movie,parent,false))
    }

    override fun onBindViewHolder(holder: ViewBindingHolder, position: Int) {
        holder.setData(BR.movie,getItem(position))
    }
}

class ViewBindingHolder(val dataBinding: ViewDataBinding):RecyclerView.ViewHolder(dataBinding.root){
    fun <T> setData(id:Int,data:T){
        dataBinding.setVariable(id,data)
        dataBinding.executePendingBindings()
    }
}