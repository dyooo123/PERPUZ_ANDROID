package com.example.ugd3_kelompok19

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ugd3_kelompok19.room.Constant
import com.example.ugd3_kelompok19.room.Peminjam
import com.example.ugd3_kelompok19.room.PeminjamDB
import kotlinx.android.synthetic.main.activity_peminjam.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PeminjamActivity : AppCompatActivity() {
    val db by lazy{ PeminjamDB(this) }
    lateinit var peminjamAdapter: PeminjamAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peminjam)
        setupListener()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        peminjamAdapter = PeminjamAdapter(arrayListOf(), object :
            PeminjamAdapter.OnAdapterListener{
            override fun onClick(peminjam : Peminjam) {
                Toast.makeText(applicationContext, peminjam.nama,
                    Toast.LENGTH_SHORT).show()
                intentEdit(peminjam.id, Constant.TYPE_READ)
            }
            override fun onUpdate(peminjam : Peminjam) {
                intentEdit(peminjam.id, Constant.TYPE_UPDATE)
            }
            override fun onDelete(peminjam : Peminjam) {
                deleteDialog(peminjam)
            }
        })
        list_peminjam.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = peminjamAdapter
        }
    }

    private fun deleteDialog(peminjam : Peminjam){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You Sure to delete this data From ${peminjam.nama}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.peminjamDao().deletePeminjam(peminjam)
                    loadData()
                }
            })
        }
        alertDialog.show()
    }
    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val peminjam = db.peminjamDao().getPeminjams()
            Log.d("PeminjamActivity","dbResponse: $peminjam")
            withContext(Dispatchers.Main){
                peminjamAdapter.setData( peminjam )
            }
        }
    }
    fun setupListener() {
        button_create.setOnClickListener{
            intentEdit(0,Constant.TYPE_CREATE)
        }
    }
    fun intentEdit(peminjamId : Int, intentType: Int){
        startActivity(
            Intent(applicationContext, EditActivity::class.java)
                .putExtra("intent_id", peminjamId)
                .putExtra("intent_type", intentType)
        )
    }
}