package com.example.room

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.room.database.daftarBelanja
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class adapterDaftar(private val daftarBelanja: MutableList<daftarBelanja>) :
    RecyclerView.Adapter<adapterDaftar.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun delData(dtBelanja: daftarBelanja)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val _btnSelesai: ImageButton = itemView.findViewById(R.id.btnSelesai)
        val _tv1: TextView = itemView.findViewById(R.id.tv1)
        val _tv2: TextView = itemView.findViewById(R.id.tv2)
        val _tv3: TextView = itemView.findViewById(R.id.tv3)
        val _btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val _btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return daftarBelanja.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val daftar = daftarBelanja[position]

        holder._tv1.text = daftar.tanggal
        holder._tv2.text = daftar.item
        holder._tv3.text = daftar.jumlah.toString()

        holder._btnEdit.setOnClickListener {
            val intent = Intent(it.context, TambahDaftar::class.java)
            intent.putExtra("id", daftar.id)
            intent.putExtra("addEdit", 1)
            it.context.startActivity(intent)
        }

        holder._btnDelete.setOnClickListener {
            onItemClickCallback.delData(daftar)
        }

        holder._btnSelesai.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                // Tambahkan data ke tabel historyBarang
                DB.funHistoryBarangDAO().insert(
                    historyBarang(
                        tanggal = daftar.tanggal,
                        item = daftar.item,
                        jumlah = daftar.jumlah
                    )
                )

                // Hapus data dari tabel daftarBelanja
                DB.fundaftarBelanjaDAO().delete(daftar)

                // Muat ulang data
                withContext(Dispatchers.Main) {
                    loadData() // Panggil fungsi yang sama di `MainActivity` untuk memuat ulang RecyclerView
                }
            }
        }
    }

    private suspend fun loadData() {
        val daftarBelanja = DB.fundaftarBelanjaDAO().selectAll()
        withContext(Dispatchers.Main) {
            adpDaftar.isiData(daftarBelanja)
        }
    }


    fun isiData(daftar: List<daftarBelanja>) {
        daftarBelanja.clear()
        daftarBelanja.addAll(daftar)
        notifyDataSetChanged()
    }
}
