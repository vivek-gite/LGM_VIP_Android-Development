package com.lgm.facedetection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.lgm.facedetection.databinding.FragmentResultDialogBinding


class ResultDialog : DialogFragment() {

    lateinit var binding:FragmentResultDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentResultDialogBinding.inflate(inflater,container,false)

        //Using Bundle coz we aren't getting just a single string but a lot more info which we need to bundle together
        val bundle = arguments
        binding.resultTextView.text = "Number of faces detected: "+bundle?.get("RESULT").toString()
        binding.resultOkButton.setOnClickListener{
            dismiss()
        }
        return binding.root
    }
}