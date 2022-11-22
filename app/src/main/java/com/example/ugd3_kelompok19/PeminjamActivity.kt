package com.example.ugd3_kelompok19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok19.api.peminjamApi
import com.example.ugd3_kelompok19.models.Peminjam
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pinjam.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class PeminjamActivity : AppCompatActivity() {
    private var srPeminjam: SwipeRefreshLayout? =null
    private var adapter: PeminjamAdapter? = null
    private var svPeminjam: SearchView? =null
    private var layoutLoading: LinearLayout? =null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pinjam)

        queue =  Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srPeminjam = findViewById(R.id.sr_peminjam)
        svPeminjam = findViewById(R.id.sv_peminjam)

        srPeminjam?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener { allPeminjam() })
        svPeminjam?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener{
            val i = Intent(this@PeminjamActivity, AddEditActivity::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_peminjam)
        adapter = PeminjamAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allPeminjam()
    }

    private fun allPeminjam(){
        srPeminjam!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, peminjamApi.GET_ALL_URL, Response.Listener { response ->
                //val gson = Gson()
                //var peminjam: Array<Peminjam> = gson.fromJson(response, Array<Peminjam>::class.java)

                var peminjamJo = JSONObject(response.toString())
                var peminjamArray = arrayListOf<Peminjam>()
                var id : Int = peminjamJo.getJSONArray("data").length()

                for (i in 0 until id){
                    var peminjams = Peminjam(
                        peminjamJo.getJSONArray("data").getJSONObject(i).getInt("id"),
                        peminjamJo.getJSONArray("data").getJSONObject(i).getString("nama"),
                        peminjamJo.getJSONArray("data").getJSONObject(i).getString("alamat"),
                        peminjamJo.getJSONArray("data").getJSONObject(i).getString("judulBukuPinjaman") ,
                        peminjamJo.getJSONArray("data").getJSONObject(i).getString("tanggalPinjam"),
                        peminjamJo.getJSONArray("data").getJSONObject(i).getString("tanggalKembali")
                    )
                    peminjamArray.add(peminjams)
                }
                var peminjam: Array<Peminjam> = peminjamArray.toTypedArray()

                adapter!!.setPeminjamList(peminjam)
                adapter!!.filter.filter(svPeminjam!!.query)
                srPeminjam!!.isRefreshing = false

                if(!peminjam.isEmpty())
                    Toast.makeText(this@PeminjamActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT ).show()
                else
                    Toast.makeText(this@PeminjamActivity, "Data Kosong!", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                srPeminjam!!.isRefreshing = false
                try{
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@PeminjamActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@PeminjamActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    fun deletePeminjam(id: Int){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, peminjamApi.DELETE_URL + id, Response.Listener{ response ->
                setLoading(false)

                val gson = Gson()
                var peminjam = gson.fromJson(response, Peminjam::class.java)
                if(peminjam != null)
                    Toast.makeText(this@PeminjamActivity, "Data Berhasil Dihapus",Toast.LENGTH_SHORT).show()
                allPeminjam()
            }, Response.ErrorListener{ error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@PeminjamActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@PeminjamActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            // Menambahkan header pada request
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers

            }

        }
        queue!!.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(resultCode, resultCode, data)
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allPeminjam()
    }

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