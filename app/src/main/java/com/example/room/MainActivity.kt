package com.example.room

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.room.database.daftarBelanjaDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

private lateinit var DB: daftarBelanjaDB

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi database
        DB = daftarBelanjaDB.getDatabase(this)

        // OnClickListener untuk FloatingActionButton
        val fabAdd = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAdd)
        fabAdd.setOnClickListener {
            startActivity(Intent(this, TambahDaftar::class.java))
        }
        super.onStart()
        CoroutineScope(Dispatchers.Main).async {
            val daftarBelanja = DB.fundaftarBelanjaDAO().selectAll()
            Log.d("data ROOM", daftarBelanja.toString())
        }
    }
}
