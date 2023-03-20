package com.example.cloudestorage

import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cloudestorage.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {
    val reqCode: Int = 100
    lateinit var name: String
    lateinit var progress: ProgressDialog
    lateinit var pdfPath: Uri
    lateinit var storage: FirebaseStorage
    lateinit var binding: ActivityMainBinding
    lateinit var db: FirebaseFirestore

    companion object {
        lateinit var storageRef: StorageReference
        lateinit var uriFrom: String
        lateinit var ref: StorageReference
        lateinit var dm: DownloadManager
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dm = getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager


        progress = ProgressDialog(this)
        progress.setCancelable(false)
        progress.setMessage("Uploading PDF..")
        val i = Intent(this, MainActivity2::class.java)



        storageRef = FirebaseStorage.getInstance().reference
        db = Firebase.firestore
        ref = storageRef
        storage = Firebase.storage


        binding.choosePdf.setOnClickListener {
            name = "2023" + ((10000..99999).random()).toString()
            val intent = Intent()
            intent.type = "application/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "SELECT PDF FILE"), reqCode)
        }


        binding.show.setOnClickListener {
            startActivity(i)
        }

        binding.UploadePdf.setOnClickListener {
            name = "2023" + ((1000..999).random()).toString()
            progress.show()
            ref.child("$name.pdf/").putFile(pdfPath).addOnSuccessListener {
                Toast.makeText(this, "Sucsses", Toast.LENGTH_SHORT).show()
                if (progress.isShowing) {
                    progress.dismiss()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                if (progress.isShowing) {
                    progress.dismiss()
                }
            }
            addToDB("$name.pdf")
        }

        binding.download.setOnClickListener {
            ref = storageRef.child("202377364.pdf/")
            ref.downloadUrl.addOnSuccessListener {
                uriFrom = it.toString()
                Log.e("zizo", it.toString())
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == reqCode) {
            pdfPath = data!!.data!!
        }
    }


    fun addToDB(name: String) {
        val pdf = hashMapOf("name" to name)

        db.collection("pdfNames").add(pdf).addOnSuccessListener {
            Toast.makeText(this, "sucsess ${it.id}", Toast.LENGTH_SHORT).show()
            if (progress.isShowing) {
                progress.dismiss()
            }
            Log.e("samy", "sucsess")
        }.addOnFailureListener {
            Toast.makeText(this, "faild ${it.message}", Toast.LENGTH_SHORT).show()
            Log.e("samy", "faild ${it.message}")
        }
    }
}
