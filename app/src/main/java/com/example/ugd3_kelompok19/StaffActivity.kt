package com.example.ugd3_kelompok19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok19.api.staffApi
import com.example.ugd3_kelompok19.models.Staff
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class StaffActivity : AppCompatActivity() {
    private var srStaff: SwipeRefreshLayout? =null
    private var adapter: StaffAdapter? = null
    private var svStaff: SearchView? =null
    private var layoutLoading: LinearLayout? =null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff)

        queue =  Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srStaff = findViewById(R.id.sr_staff)
        svStaff = findViewById(R.id.sv_staff)

        srStaff?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener { allStaff() })
        svStaff?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })

        val fabsxAdd = findViewById<FloatingActionButton>(R.id.fabs_add)
        fabsxAdd.setOnClickListener{
            val i = Intent(this@StaffActivity, AddEditStaffActivity::class.java)
            startActivityForResult(i, StaffActivity.LAUNCH_ADD_ACTIVITY)
        }

        val rvProdukStaff = findViewById<RecyclerView>(R.id.rv_staff)
        adapter = StaffAdapter(ArrayList(), this)
        rvProdukStaff.layoutManager = LinearLayoutManager(this)
        rvProdukStaff.adapter = adapter
        allStaff()
    }

    private fun allStaff(){
        srStaff!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, staffApi.GET_ALL_URL, Response.Listener { response ->

                var staffJo = JSONObject(response.toString())
                var staffArray = arrayListOf<Staff>()
                var id : Int = staffJo.getJSONArray("data").length()

                for (i in 0 until id){
                    var staffs = Staff(
                        staffJo.getJSONArray("data").getJSONObject(i).getInt("id"),
                        staffJo.getJSONArray("data").getJSONObject(i).getString("namastaff"),
                        staffJo.getJSONArray("data").getJSONObject(i).getString("alamatstaff"),
                        staffJo.getJSONArray("data").getJSONObject(i).getString("umur") ,
                        staffJo.getJSONArray("data").getJSONObject(i).getString("lahir"),
                    )
                    staffArray.add(staffs)
                }
                var staff: Array<Staff> = staffArray.toTypedArray()

                adapter!!.setStaffList(staff)
                adapter!!.filter.filter(svStaff!!.query)
                srStaff!!.isRefreshing = false

                if(!staff.isEmpty())
                    Toast.makeText(this@StaffActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT ).show()
                else
                    Toast.makeText(this@StaffActivity, "Data Kosong!", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                srStaff!!.isRefreshing = false
                try{
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@StaffActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@StaffActivity, e.message, Toast.LENGTH_SHORT).show()
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

    fun deleteStaff(id: Int){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, staffApi.DELETE_URL + id, Response.Listener{ response ->
                setLoading(false)

                val gson = Gson()
                var staff = gson.fromJson(response, Staff::class.java)
                if(staff != null)
                    MotionToast.createColorToast(this,
                        "Delete Success",
                        "Data telah terhapus!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                allStaff()
            }, Response.ErrorListener{ error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@StaffActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@StaffActivity, e.message, Toast.LENGTH_SHORT).show()
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
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allStaff()
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