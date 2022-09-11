package com.example.ugd3_kelompok19

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd3_kelompok19.entity.Buku

class RVBukuAdapter (private val data: Array<Buku>) : RecyclerView.Adapter<RVBukuAdapter.viewHolder>() {

    private val images = intArrayOf(
        R.drawable.dongeng,
        R.drawable.sejarah,
        R.drawable.novel,
        R.drawable.komik,
        R.drawable.ensiklopedia,
        R.drawable.majalah,
        R.drawable.biografi,
        R.drawable.kamus,
        R.drawable.karyailmiah,
        R.drawable.fotografi)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_item_buku, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tvBuku.text = currentItem.buku
        holder.tvKategori.text = currentItem.kategori
        holder.ivBuku.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBuku: TextView = itemView.findViewById(R.id.tv_buku)
        val tvKategori: TextView = itemView.findViewById(R.id.tv_kategori)
        val ivBuku : ImageView = itemView.findViewById(R.id.iv_buku)
    }
}
