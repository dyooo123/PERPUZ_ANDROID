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
import com.example.ugd3_kelompok19.models.Staff
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class StaffAdapter(private var staffList: List<Staff>, context: Context) :
    RecyclerView.Adapter<StaffAdapter.ViewHolder>(), Filterable {

    private var filteredStaffList: MutableList<Staff>
    private val context: Context

    init{
        filteredStaffList = ArrayList(staffList)
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_staff, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredStaffList.size
    }

    fun setStaffList(staffList: Array<Staff>){
        this.staffList = staffList.toList()
        filteredStaffList = staffList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val staff = filteredStaffList[position]
        holder.tvNamaStaff.text = staff.namastaff
        holder.tvAlamatStaff.text = staff.alamatstaff
        holder.tvUmur.text = staff.umur
        holder.tvLahir.text = staff.lahir


        holder.btnDelete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data staff ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){_, _ ->
                    if(context is StaffActivity) staff.id?.let{ it1 ->
                        context.deleteStaff(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvStaff.setOnClickListener{
            val i = Intent(context, AddEditStaffActivity::class.java)
            i.putExtra("id", staff.id)
            if(context is StaffActivity)
                context.startActivityForResult(i, StaffActivity.LAUNCH_ADD_ACTIVITY)

        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered : MutableList<Staff> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(staffList)
                }else{
                    for(staff in staffList){
                        if(staff.namastaff.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(staff)

                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredStaffList.clear()
                filteredStaffList.addAll((filterResults.values as List<Staff>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvNamaStaff: TextView
        var tvAlamatStaff: TextView
        var tvUmur: TextView
        var tvLahir: TextView
        var btnDelete: ImageButton
        var cvStaff: CardView

        init{
            tvNamaStaff = itemView.findViewById(R.id.tv_namastaff)
            tvAlamatStaff = itemView.findViewById(R.id.tv_alamatstaff)
            tvUmur = itemView.findViewById(R.id.tv_umur)
            tvLahir= itemView.findViewById(R.id.tv_lahir)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvStaff = itemView.findViewById(R.id.cv_staff)
        }
    }

}
