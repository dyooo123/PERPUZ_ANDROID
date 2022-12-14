package com.example.ugd3_kelompok19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok19.api.staffApi
import com.example.ugd3_kelompok19.models.Staff
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class AddEditStaffActivity : AppCompatActivity() {


    private var etNamaStaff: EditText? = null
    private var etAlamatStaff: EditText? = null
    private var etUmur: EditText?? = null
    private var etLahir:EditText?? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_staff)

        //Pendeklarasian request queue
        queue = Volley.newRequestQueue(this)
        etNamaStaff = findViewById(R.id.et_namastaff)
        etAlamatStaff = findViewById(R.id.et_alamatstaff)
        etUmur = findViewById(R.id.et_umur)
        etLahir = findViewById(R.id.et_lahir)


        layoutLoading = findViewById(R.id.layout_loading)


        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener{finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle =findViewById<TextView>(R.id.tv_title)
        val id = intent.getIntExtra("id", -1)
        if(id== -1){
            tvTitle.setText("Tambah Staff")
            btnSave.setOnClickListener { createStaff() }
        }else{
            tvTitle.setText("Edit Staff")
            getStaffById(id)

            btnSave.setOnClickListener { updateStaff(id) }
        }

    }


    private fun getStaffById(id: Int){
        // Fungsi untuk menampilkan data  berdasarkan id
        setLoading(true)
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, staffApi.GET_BY_ID_URL + id, Response.Listener { response ->

                var staffJo = JSONObject(response.toString())
                val staff = staffJo.getJSONObject("data")


                etNamaStaff!!.setText(staff.getString("namastaff"))
                etAlamatStaff!!.setText(staff.getString("alamatstaff"))
                etUmur!!.setText(staff.getString("umur"))
                etLahir!!.setText(staff.getString("lahir"))

                Toast.makeText(this@AddEditStaffActivity, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
                setLoading(false)
            },  Response.ErrorListener { error ->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditStaffActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@AddEditStaffActivity, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createStaff(){
        setLoading(true)
        if(etNamaStaff!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditStaffActivity, "Nama Staff tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else if(etAlamatStaff!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditStaffActivity, "Alamat Staff tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else if(etUmur!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditStaffActivity, "Umur tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        }
        else if(etLahir!!.text.toString().isEmpty()) {
            Toast.makeText(this@AddEditStaffActivity, "Tanggal Lahir tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else {
            val staff = Staff(
                0,
                etNamaStaff!!.text.toString(),
                etAlamatStaff!!.text.toString(),
                etUmur!!.text.toString(),
                etLahir!!.text.toString(),
            )
            val stringRequest: StringRequest =
                object :
                    StringRequest(Method.POST, staffApi.ADD_URL, Response.Listener { response ->
                        val gson = Gson()
                        val staff = gson.fromJson(response, Staff::class.java)

                        if (staff != null)
                            MotionToast.createColorToast(
                                this,
                                "Add Staff Success",
                                "Staff ditambahkan!",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(
                                    this,
                                    www.sanju.motiontoast.R.font.helvetica_regular
                                )
                            )

                        val returnIntent = Intent()
                        setResult(RESULT_OK, returnIntent)
                        finish()

                        setLoading(false)
                    }, Response.ErrorListener { error ->
                        setLoading(false)
                        try {
                            val responseBody =
                                String(error.networkResponse.data, StandardCharsets.UTF_8)
                            val errors = JSONObject(responseBody)
                            Toast.makeText(
                                this@AddEditStaffActivity,
                                errors.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@AddEditStaffActivity, e.message, Toast.LENGTH_SHORT)
                                .show()
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
                        val requestBody = gson.toJson(staff)
                        return requestBody.toByteArray(StandardCharsets.UTF_8)
                    }

                    override fun getBodyContentType(): String {
                        return "application/json"
                    }
                }
            // Menambahkan request ke request queue
            queue!!.add(stringRequest)
        }
        setLoading(false)
    }

    private fun updateStaff(id: Int){
        setLoading(true)

        val staff = Staff(0,
            etNamaStaff!!.text.toString(),
            etAlamatStaff!!.text.toString(),
            etUmur!!.text.toString(),
            etLahir!!.text.toString(),
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, staffApi.UPDATE_URL + id, Response.Listener{ response ->
                val gson = Gson()

                val staff = gson.fromJson(response, Staff::class.java)

                if(staff != null)
                    MotionToast.createColorToast(this,"Update Staff",
                        "Staff terupdate!",
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
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
                        this@AddEditStaffActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@AddEditStaffActivity, e.message, Toast.LENGTH_SHORT).show()
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
                val requestBody = gson.toJson(staff)
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