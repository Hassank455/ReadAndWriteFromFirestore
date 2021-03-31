package com.example.ass_firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class MainActivity : AppCompatActivity() {

    lateinit var name: EditText
    lateinit var number: EditText
    lateinit var address: EditText

    lateinit var progressBar: ProgressBar

    lateinit var datatext: TextView
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        name = findViewById(R.id.username)
        number = findViewById(R.id.usernumber)
        address = findViewById(R.id.useradress)
        progressBar = findViewById(R.id.progress)
        datatext = findViewById(R.id.productsData)

        progressBar.visibility = View.GONE

        fetchData()

    }

    private fun fetchData() {

        db.collection("contact")
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    val eventList: MutableList<String?> = ArrayList()
                    for (doc in task.result!!) {
                        eventList.add(doc.data.toString())
                    }
                    datatext.text = "$eventList"
                } else {
                    Log.d("TAG", "Error ", task.exception)
                }
            })

    }


    fun saveToFirebase(v: View) {

        val username = name.text.toString()
        val unumber = number.text.toString()
        val uaddress = address.text.toString()
        val hashMap: HashMap<String, Any> = HashMap<String, Any>()
        hashMap["name"] = username
        hashMap["number"] = unumber
        hashMap["address"] = uaddress
        db.collection("contact").add(hashMap)
            .addOnSuccessListener {

                Thread(Runnable {

                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                        progressBar.visibility = View.VISIBLE
                    })

                    try {
                        var i = 0;
                        while (i < Int.MAX_VALUE) {
                            i++
                        }
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    this@MainActivity.runOnUiThread(java.lang.Runnable {
                        progressBar.visibility = View.GONE
                    })
                }).start()


                Log.e("TAG", "Add")

            }.addOnFailureListener {
                Log.e("TAG", "Failed")


            }

    }

}