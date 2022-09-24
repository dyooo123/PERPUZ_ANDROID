package com.example.ugd3_kelompok19

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd3_kelompok19.room.Peminjam
import kotlinx.android.synthetic.main.activity_peminjam_adapter.view.*

class PeminjamAdapter (private val peminjams : ArrayList<Peminjam>, private val
listener: OnAdapterListener) :
    RecyclerView.Adapter<PeminjamAdapter.PeminjamViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            PeminjamViewHolder {
        return PeminjamViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.activity_peminjam_adapter, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: PeminjamViewHolder, position:
        Int ) {
        val peminjam = peminjams[position]
        holder.view.text_title.text = peminjam.nama
        holder.view.text_title.setOnClickListener {
            listener.onClick(peminjam)
        }
        holder.view.icon_edit.setOnClickListener {
            listener.onUpdate(peminjam)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(peminjam)
        }
    }

    override fun getItemCount() = peminjams.size
    inner class PeminjamViewHolder(val view: View) :
        RecyclerView.ViewHolder(view)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Peminjam>) {
        peminjams.clear()
        peminjams.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(peminjam: Peminjam)
        fun onUpdate(peminjam: Peminjam)
        fun onDelete(peminjam: Peminjam)

    }
}
