package com.example.cloudestorage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cloudestorage.databinding.ActivityMain2Binding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity2 : AppCompatActivity() {
    lateinit var data: ArrayList<pdf>
    lateinit var pdfAdapter: pdfAdapter
    lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        data = ArrayList<pdf>()

        data.add(pdf(1, "samy.pdf"))
        data.add(pdf(1, "samy.pdf"))
        data.add(pdf(1, "samy.pdf"))
        data.add(pdf(1, "samy.pdf"))
        data.add(pdf(1, "samy.pdf"))

        data.clear()

        db = Firebase.firestore


        pdfAdapter = pdfAdapter(this, data)
        binding.listV.adapter = pdfAdapter
        fetchData()

        binding.imageView2.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }
    fun fetchData() {
        var id = 0
        db.collection("pdfNames").get().addOnSuccessListener { result ->
            for (document in result) {
                val name = document.data.get("name").toString()

                val contactObj = pdf(id, name)
                data.add(contactObj)
                id++

                Log.d(
                    "samy", "$id $name .... ${data.size}"
                )
                pdfAdapter.notifyDataSetChanged()

            }
        }.addOnFailureListener { exception ->
            Log.w("TAG", "Error getting documents.", exception)
        }
    }

}
