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
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ugd3_kelompok19.databinding.ActivityEditBinding


class EditActivity : AppCompatActivity() {
    val db by lazy { PeminjamDB(this) }
    private var peminjamId : Int = 0
    private var binding: ActivityEditBinding? = null
    private val PEMINJAM_ID_1 = "peminjam_notification_01"
    private val notificationId2 = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setupView()
        setupListener()

    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(
                PEMINJAM_ID_1,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = descriptionText
            }


            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotifications() {
        val builder = NotificationCompat.Builder(this,PEMINJAM_ID_1)
            .setSmallIcon(R.drawable.ic_person)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setColor(Color.BLUE)
            .setContentTitle("Peminjam Buku")
            .setContentText("Data Peminjam Buku PERPUZ")
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine("Nama Peminjam : "+ binding?.editNamaPeminjam?.text.toString())
                    .addLine("Alamat : "+ binding?.editAlamat?.text.toString())
                    .addLine("Judul Buku : "+ binding?.editJudulBuku?.text.toString())
                    .addLine("Tanggal Pinjam : "+ binding?.editTanggalPinjam?.text.toString())
                    .addLine("Tanggal Kembali : "+ binding?.editTanggalKembali?.text.toString())
            )


        with(NotificationManagerCompat.from(this)) {
            notify(notificationId2, builder.build())
        }
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
                createNotificationChannels()
                sendNotifications()
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