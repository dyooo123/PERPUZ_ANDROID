package com.example.ugd3_kelompok19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok19.api.peminjamApi
import com.example.ugd3_kelompok19.models.Peminjam
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_edit.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class AddEditActivity : AppCompatActivity() {


    private var etNama: EditText? = null
    private var etAlamat: EditText? = null
    private var etJudul: EditText?? = null
    private var etPinjam:EditText?? = null
    private var etKembali:EditText?? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        //Pendeklarasian request queue
        queue = Volley.newRequestQueue(this)
        etNama = findViewById(R.id.et_nama)
        etAlamat = findViewById(R.id.et_alamat)
        etJudul = findViewById(R.id.et_judul)
        etPinjam = findViewById(R.id.et_pinjam)
        etKembali = findViewById(R.id.et_kembali)

        layoutLoading = findViewById(R.id.layout_loading)


        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener{finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle =findViewById<TextView>(R.id.tv_title)
        val id = intent.getIntExtra("id", -1)
        if(id== -1){
            tvTitle.setText("Tambah Peminjam")
            btnSave.setOnClickListener { createPeminjam() }
        }else{
            tvTitle.setText("Edit Peminjam")
            getPeminjamById(id)

            btnSave.setOnClickListener { updatePeminjam(id) }
        }

    }


    private fun getPeminjamById(id: Int){
        // Fungsi untuk menampilkan data  berdasarkan id
        setLoading(true)
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, peminjamApi.GET_BY_ID_URL + id, Response.Listener { response ->
                //val gson = Gson()
                //val peminjam = gson.fromJson(response, Peminjam::class.java)

                var peminjamJo = JSONObject(response.toString())
                val peminjam = peminjamJo.getJSONObject("data")


                etNama!!.setText(peminjam.getString("nama"))
                etAlamat!!.setText(peminjam.getString("alamat"))
                etJudul!!.setText(peminjam.getString("judulBukuPinjaman"))
                etPinjam!!.setText(peminjam.getString("tanggalPinjam"))
                etKembali!!.setText(peminjam.getString("tanggalKembali"))

                Toast.makeText(this@AddEditActivity, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
                setLoading(false)
            },  Response.ErrorListener { error ->
                    setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
            }
        queue!!.add(stringRequest)
    }

    private fun createPeminjam(){
        setLoading(true)

        val peminjam = Peminjam(0,
            etNama!!.text.toString(),
            etAlamat!!.text.toString(),
            etJudul!!.text.toString(),
            etPinjam!!.text.toString(),
            etKembali!!.text.toString()
        )
        val stringRequest: StringRequest =
            object: StringRequest(Method.POST, peminjamApi.ADD_URL, Response.Listener {response->
                val gson = Gson()
                val peminjam = gson.fromJson(response, Peminjam::class.java)

                if(peminjam!=null)
                    Toast.makeText(this@AddEditActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers

                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(peminjam)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        // Menambahkan request ke request queue
        queue!!.add(stringRequest)
    }

    private fun updatePeminjam(id: Int){
        setLoading(true)

        val peminjam = Peminjam(0,
            etNama!!.text.toString(),
            etAlamat!!.text.toString(),
            etJudul!!.text.toString(),
            etPinjam!!.text.toString(),
            etKembali!!.text.toString()
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, peminjamApi.UPDATE_URL + id, Response.Listener{ response ->
                val gson = Gson()

                val peminjam = gson.fromJson(response, Peminjam::class.java)

                if(peminjam != null)
                    Toast.makeText(this@AddEditActivity, "Data Berhasil Diupdate",Toast.LENGTH_SHORT).show()
                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener{ error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers

            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val gson = Gson()
                val requestBody = gson.toJson(peminjam)
                return requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue!!.add(stringRequest)

    }

    //Fungsi ini digunakan untuk menampilkan layout Loading

    private fun setLoading(isLoading: Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }
}