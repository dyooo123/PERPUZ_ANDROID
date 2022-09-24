package com.example.ugd3_kelompok19

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ugd3_kelompok19.room.Constant
import com.example.ugd3_kelompok19.room.Peminjam
import com.example.ugd3_kelompok19.room.PeminjamDB
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.peminjam_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    val db by lazy { PeminjamDB(this) }
    private var peminjamId : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setupView()
        setupListener()
    }

    fun setupView() {
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_CREATE-> {
                btnUpdate.visibility = View.GONE
            }
            Constant.TYPE_READ-> {
                btnSave.visibility = View.GONE
                btnUpdate.visibility = View.GONE
                getPeminjam()
            }
            Constant.TYPE_UPDATE-> {
                btnSave.visibility = View.GONE
                getPeminjam()
            }
        }
    }

    private fun setupListener() {
        btnSave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.peminjamDao().addPeminjam(
                    Peminjam(
                        0, edit_namaPeminjam.text.toString(),
                        edit_alamat.text.toString(),
                        edit_judulBuku.text.toString(),
                        edit_tanggalPinjam.text.toString(),
                        edit_tanggalKembali.text.toString()
                    )
                )
                finish()
            }
        }
        btnUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.peminjamDao().updatePeminjam(
                    Peminjam(peminjamId,
                        edit_namaPeminjam.text.toString(),
                        edit_alamat.text.toString(),
                        edit_judulBuku.text.toString(),
                        edit_tanggalPinjam.text.toString(),
                        edit_tanggalKembali.text.toString())
                )
                finish()
            }
        }
    }
        fun getPeminjam(){
            peminjamId = intent.getIntExtra("intent_id", 0)
            CoroutineScope(Dispatchers.IO).launch {
                val pinjam = db.peminjamDao().getPeminjam(peminjamId)[0]
                edit_namaPeminjam.setText(pinjam.nama)
                edit_alamat.setText(pinjam.alamat)
                edit_judulBuku.setText(pinjam.judulBukuPinjaman)
                edit_tanggalPinjam.setText(pinjam.tanggalPinjam)
                edit_tanggalKembali.setText(pinjam.tanggalKembali)
            }
        }

        override fun onSupportNavigateUp(): Boolean {
            onBackPressed()
            return super.onSupportNavigateUp()
        }
}