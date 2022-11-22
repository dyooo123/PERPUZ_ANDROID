package com.example.ugd3_kelompok19

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok19.api.profilApi
import com.example.ugd3_kelompok19.databinding.FragmentProfilBinding
import com.example.ugd3_kelompok19.room.UserDB
import com.example.ugd3_kelompok19.room.UserDao
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class ProfilFragment : Fragment() {
    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!
    private var queue: RequestQueue? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfilBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queue = Volley.newRequestQueue(activity)
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        var id = sharedPreferences.getInt("id",0)
        showUserbyId(id)

        val btnLogout: Button = view.findViewById(R.id.btnLogout)

        binding.btnUpdate.setOnClickListener {
            moveFragment(updateProfileFragment())
        }

        btnLogout.setOnClickListener {
            activity?.let { it1 ->
                MaterialAlertDialogBuilder(it1)
                    .setTitle("Are You Sure Want to Exit?")
                    .setNegativeButton("Yes") {dialog, which ->
                        activity?.finish()
                    }
                    .setPositiveButton("No") {dialog, which ->
                    }
                    .show()
            }
        }

        val camera: Button = view.findViewById(R.id.btnCamera)
        camera.setOnClickListener{
            val moveCamera = Intent(this@ProfilFragment.context, cameraActivity::class.java)
            startActivity(moveCamera)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setData(){
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        val db by lazy { UserDB(activity as HomeActivity) }
        val userDao = db.userDao()
        val user = sharedPreferences?.let { userDao.getUser(it.getInt("id",0)) }

        binding.viewUsername.setText(user?.username)
        binding.viewEmail.setText(user?.email)
        binding.viewTanggalLahir.setText(user?.tanggalLahir)
        binding.viewNoTelp.setText(user?.noTelp)
    }

    private fun moveFragment(fragment: Fragment){
        val move = requireActivity().supportFragmentManager.beginTransaction()
        move.replace(R.id.layoutProfile,fragment)
            .addToBackStack(null).commit()
        move.hide(ProfilFragment())
    }

    private fun showUserbyId(id: Int) {
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, profilApi.GET_BY_ID_URL + id, Response.Listener { response ->

                var jsonUser = JSONObject(response.toString())
                val userdata = jsonUser.getJSONObject("data")

                binding.viewUsername.setText(userdata.getString("username"))
                binding.viewEmail.setText(userdata.getString("email"))
                binding.viewTanggalLahir.setText(userdata.getString("tanggalLahir"))
                binding.viewNoTelp.setText(userdata.getString("notelp"))

                Toast.makeText(activity, "Data User berhasil diambil!", Toast.LENGTH_SHORT).show()
                //setLoading(false)
            }, Response.ErrorListener { error ->
                //setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        activity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }
}