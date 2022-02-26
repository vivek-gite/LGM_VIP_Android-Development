package com.lgm.facedetection

import android.app.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.lgm.facedetection.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding

    companion object{
        //This is for checking if the user has provided the permission for the camera or not
        private val CAMERA_PERMISSION_CODE = 1
        //The requestCode helps you to identify from which Intent you came back. For example,
        // imagine your Activity A (Main Activity) could call Activity B (Camera Request),
        // Activity C (Audio Recording), Activity D (Select a Contact).
        //
        //Whenever the subsequently called activities B, C or D finish and need to pass data back
        // to A, now you need to identify in your onActivityResult from which Activity you are
        // returning from and put your handling logic accordingly.
        private val CAMERA_REQUEST_CODE = 2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCamera.setOnClickListener {
            if(ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED
                ){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }
            else{
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== CAMERA_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }
            else{
                Toast.makeText(
                    this,
                    ":(( Camera permission was denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Checking requestCode if it matches with our app's requestCode or not, coz there might be other apps which may use
        // camera too.
        //Also checking resultCode coz it might be possible that the user just opens and closes the app without
        // clicking the open camera button
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA_REQUEST_CODE){
                //Bitmap- A bitmap is an array of bits that specify the color of each pixel in a
                // rectangular array of pixels.

                //Retrieving the Bitmap from 'extras' captured by the camera
                val bitMap = data!!.extras!!.get("data") as Bitmap
                detectPeople(bitMap)
            }
        }
    }

    private fun detectPeople(bitmap: Bitmap){

        //configuring the detector to High-accuracy landmark detection and face classification
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
            .enableTracking()
            .build()

        // Preparing the input image
        val image = InputImage.fromBitmap(bitmap,0)

        //Get an instance of FaceDetector
        val detector = FaceDetection.getClient(highAccuracyOpts)

        detector.process(image)
            .addOnSuccessListener { faces ->
                // Task completed successfully
                var i = if (faces.size>0) faces.size else 1

                if(faces.size==0){
                    Toast.makeText(this@MainActivity, "No Face Detected", Toast.LENGTH_SHORT).show()
                }
                else{
                    val bundle:Bundle = Bundle()
                    bundle.putString("RESULT", i.toString())

                    val dialog:DialogFragment = ResultDialog()
                    dialog.arguments = bundle
                    dialog.show(supportFragmentManager,"RESULT_DIALOG_BOX")
                }
            }

    }
}