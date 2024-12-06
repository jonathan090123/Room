package com.example.room

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.room.database.daftarBelanja
import com.example.room.database.daftarBelanjaDB
import com.example.room.helper.DateHelper.getCurrentDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class TambahDaftar : AppCompatActivity() {

    private lateinit var _etItem: EditText
    private lateinit var _etJumlah: EditText
    private lateinit var _btnTambah: Button
    private lateinit var _btnUpdate: Button

    var tanggal = getCurrentDate()
    var DB = daftarBelanjaDB.getDatabase(this)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_daftar)

        _etItem = findViewById(R.id.etItem)
        _etJumlah = findViewById(R.id.etJumlah)
        _btnTambah = findViewById(R.id.btnTambah)
        _btnUpdate = findViewById(R.id.btnUpdate)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.actTambahDaftar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _btnTambah.setOnClickListener {
            CoroutineScope(Dispatchers.IO).async {
                DB.fundaftarBelanjaDAO().insert(
                    daftarBelanja(
                        tanggal = tanggal,
                        item = _etItem.text.toString(),
                        jumlah = _etJumlah.text.toString()
                    )
                )
            }
        }
    }
}
