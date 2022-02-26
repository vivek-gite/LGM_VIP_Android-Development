package com.lgm.covidTracker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.lgm.covidTracker.databinding.ActivityMainBinding
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    lateinit var stateData: List<Model>
    lateinit var model:Model
    lateinit var adapter: covidAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        stateData = ArrayList<Model>()
        getStateData()
    }

    private fun getStateData() {
        val url =
            "https://data.covid19india.org/state_district_wise.json"
        val queue = Volley.newRequestQueue(this@MainActivity)
        val request =
            JsonObjectRequest(Request.Method.GET, url, null, { response ->
                try {
                    val dataKeys = response.keys()
                    while(dataKeys.hasNext()){
                        val stateName = dataKeys.next()
                        val obj1 = response.getJSONObject(stateName)
                        val districtObject = obj1.getJSONObject("districtData")
                        val districtKeys = districtObject.keys()

                        while(districtKeys.hasNext()){
                            val districtName = districtKeys.next()
                            val districtData = districtObject.getJSONObject(districtName)
                            val obj2 = districtData.getJSONObject("delta")

                            val active: String = districtData.getString("active")
                            val confirmed: String = districtData.getString("confirmed")
                            val deceased: String = districtData.getString("deceased")
                            val recovered: String = districtData.getString("recovered")

                            val dconfirmed: String = obj2.getString("confirmed")
                            val ddeceased: String = obj2.getString("deceased")
                            val drecovered: String = obj2.getString("recovered")

                            model = Model(districtName,stateName,active,confirmed,deceased,recovered,dconfirmed,ddeceased,drecovered)
                            stateData = stateData+model
                        }
                    }

                    adapter = covidAdapter(stateData)
                    binding.rcv.adapter = adapter
                    binding.rcv.layoutManager = LinearLayoutManager(this)


                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }, { error ->
                Toast.makeText(this, "Fail to get response", Toast.LENGTH_SHORT).show()
            })
        queue.add(request)
    }
}