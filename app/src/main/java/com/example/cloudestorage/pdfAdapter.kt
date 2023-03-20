package com.example.cloudestorage

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.example.cloudestorage.MainActivity.Companion.storageRef
import com.example.cloudestorage.MainActivity.Companion.uriFrom
import com.example.cloudestorage.databinding.DesignListBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class pdfAdapter(var activity: Activity, var data: ArrayList<pdf>) : BaseAdapter() {
    lateinit var db: FirebaseFirestore

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): Any {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
        return data[p0].id.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val binding = DesignListBinding.inflate(LayoutInflater.from(p2!!.context), p2, false)

        db = Firebase.firestore

        val root = p1
        if (root == null) binding.txtName.text = data[p0].pdfName

        MainActivity.ref = storageRef.child("${data[p0].pdfName}/")
        MainActivity.ref.downloadUrl.addOnSuccessListener {
            uriFrom = it.toString()
            Log.e("samy", it.toString())
//            Toast.makeText(activity, "zizo"+it.toString(), Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
//            Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
        }

        binding.cardView2.setOnClickListener {
            val Bulder = AlertDialog.Builder(activity)
            Bulder.setTitle("Download !!")
            Bulder.setMessage("Are You Want To Download this pdf file ?")
            Bulder.setPositiveButton("Yes") { _, _ ->
                Toast.makeText(activity, "${data[p0].pdfName}", Toast.LENGTH_SHORT).show()
                download(uriFrom, "${data[p0].pdfName}")
            }
            Bulder.setNegativeButton("No") { d, _ ->
                d.dismiss()
            }
            Bulder.create().show()
        }

        return binding.root
    }


}

fun download(url: String, fileName: String) {
    val request = DownloadManager.Request(Uri.parse(url + ""))
    request.setTitle(fileName)
    request.setMimeType("application/pdf")
    request.allowScanningByMediaScanner()
    request.setAllowedOverMetered(true)
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
    val dm = MainActivity.dm
    dm.enqueue(request)
}