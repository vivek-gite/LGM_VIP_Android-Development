package com.lgm.covidTracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lgm.covidTracker.databinding.TestingBinding
import kotlinx.android.synthetic.main.testing.view.*


class covidAdapter(
    var covidData: List<Model>,
): RecyclerView.Adapter<covidAdapter.covidAdapter_ViewHolder>() {

    inner class covidAdapter_ViewHolder(val binding: TestingBinding):RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): covidAdapter_ViewHolder {
        //LayoutInflater is used to create a new View (or Layout) object from one of your xml layouts.
        val context = parent.context
        // Inflate the custom layout
        val view = covidAdapter_ViewHolder(TestingBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        // Return a new holder instance
        return view
    }

    //The purpose of this function is to bind the data to our items so it takes data from 'shedule' list
    //and sets the view for the layout
    override fun onBindViewHolder(holder: covidAdapter_ViewHolder, position: Int) {
        holder.itemView.apply {
            val currentItem = covidData[position]
            statename.text = currentItem.state
            cityname.text = currentItem.city
            active.text = currentItem.active
            deceased.text = currentItem.deceased
            confirm.text = currentItem.confirmed
            recovered.text = currentItem.recovered
            ddeceased.text = currentItem.ddeceased
            drecovered.text = currentItem.drecovered
            dconfirm.text = currentItem.dconfirmed
        }
    }

    override fun getItemCount(): Int {
        return covidData.size
    }


}