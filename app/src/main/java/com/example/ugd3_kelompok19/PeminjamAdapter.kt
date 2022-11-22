package com.example.ugd3_kelompok19

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd3_kelompok19.models.Peminjam
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class PeminjamAdapter(private var peminjamList: List<Peminjam>, context: Context) :
    RecyclerView.Adapter<PeminjamAdapter.ViewHolder>(), Filterable {

    private var filteredPeminjamList: MutableList<Peminjam>
    private val context: Context

    init{
        filteredPeminjamList = ArrayList(peminjamList)
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_peminjaman, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredPeminjamList.size
    }

    fun setPeminjamList(peminjamList: Array<Peminjam>){
        this.peminjamList = peminjamList.toList()
        filteredPeminjamList = peminjamList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val peminjam = filteredPeminjamList[position]
        holder.tvNama.text = peminjam.nama
        holder.tvAlamat.text = peminjam.alamat
        holder.tvJudul.text = peminjam.judulBukuPinjaman
        holder.tvPinjam.text = peminjam.tanggalPinjam
        holder.tvKembali.text = peminjam.tanggalKembali


        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data peminjam ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){_, _ ->
                    if(context is PeminjamActivity) peminjam.id?.let{ it1 ->
                        context.deletePeminjam(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvPeminjam.setOnClickListener{
            val i = Intent(context, AddEditActivity::class.java)
            i.putExtra("id", peminjam.id)
            if(context is PeminjamActivity)
                context.startActivityForResult(i, PeminjamActivity.LAUNCH_ADD_ACTIVITY)

        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered : MutableList<Peminjam> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(peminjamList)
                }else{
                    for(peminjam in peminjamList){
                        if(peminjam.nama.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(peminjam)

                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredPeminjamList.clear()
                filteredPeminjamList.addAll((filterResults.values as List<Peminjam>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvNama: TextView
        var tvAlamat: TextView
        var tvJudul: TextView
        var tvPinjam: TextView
        var tvKembali : TextView
        var btnDelete: ImageButton
        var cvPeminjam: CardView

        init{
            tvNama = itemView.findViewById(R.id.tv_nama)
            tvAlamat = itemView.findViewById(R.id.tv_alamat)
            tvJudul = itemView.findViewById(R.id.tv_judul)
            tvPinjam = itemView.findViewById(R.id.tv_pinjam)
            tvKembali = itemView.findViewById(R.id.tv_kembali)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvPeminjam = itemView.findViewById(R.id.cv_peminjam)
        }
    }

}
