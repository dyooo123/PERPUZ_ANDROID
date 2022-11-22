package com.example.ugd3_kelompok19

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok19.api.profilApi
import com.example.ugd3_kelompok19.databinding.FragmentProfilBinding
import com.example.ugd3_kelompok19.databinding.FragmentUpdateProfileBinding
import com.example.ugd3_kelompok19.models.user
import com.example.ugd3_kelompok19.room.User
import com.example.ugd3_kelompok19.room.UserDB
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_update_profile.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets


class updateProfileFragment : Fragment() {
    private var _binding:FragmentUpdateProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private var queue: RequestQueue? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateProfileBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var checkUpdate = true
        queue = Volley.newRequestQueue(activity)
        sharedPreferences = (activity as HomeActivity).getSharedPreferences("login", Context.MODE_PRIVATE)

        var id = sharedPreferences.getInt("id",0)
        var password = sharedPreferences.getString("password",null)


        binding.btnUpdate.setOnClickListener {
            if(binding.tietUsername.text.toString().isEmpty()){
                binding.tietUsername.setError("Inputan Harus Diisi!")
                checkUpdate=false
            }
            if(binding.tietEmail.text.toString().isEmpty()){
                binding.tietEmail.setError("Inputan Harus Diisi!")
                checkUpdate=false
            }
            if(binding.tietTanggalLahir.text.toString().isEmpty()){
                binding.tietTanggalLahir.setError("Inputan Harus Diisi!")
                checkUpdate=false
            }
            if(binding.tietNoTelp.text.toString().isEmpty()){
                binding.tietNoTelp.setError("Inputan Harus Diisi!")
                checkUpdate=false
            }
            if(!checkUpdate){
                checkUpdate = true
                return@setOnClickListener
            }else{
                if (password != null) {
                    updateUser(id, password)
                }
                moveFragment(ProfilFragment())
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateData(){
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        val db by lazy { UserDB(activity as  HomeActivity) }
        val userDao = db.userDao()
        val id = sharedPreferences!!.getInt("id",0)
        val getUser = userDao.getUser(id)

        val user = User(id,
            binding.tietUsername.text.toString(),
            binding.tietEmail.text.toString(),
            getUser.password,
            binding.tietTanggalLahir.text.toString(),
            binding.tietNoTelp.text.toString(),
        )
        userDao.updateUser(user)



    }

    private fun moveFragment(fragment: Fragment){
        val move = requireActivity().supportFragmentManager.beginTransaction()
        move.replace(R.id.layoutProfile,fragment)
            .addToBackStack(null).commit()
        move.hide(ProfilFragment())
    }

    private fun updateUser(id: Int, password: String) {

        val user= user(
            id,
            binding.tietUsername.text.toString(),
            password,
            binding.tietEmail.text.toString(),
            binding.tietTanggalLahir.text.toString(),
            binding.tietNoTelp.text.toString(),

        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.PUT, profilApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()
                var user = gson.fromJson(response, user::class.java)

                if(user != null) {
                    var resJO = JSONObject(response.toString())
                    val  userobj = resJO.getJSONObject("data")

                    sharedPreferences.edit()
                        .putInt("id",userobj.getInt("id"))
                        .putString("username",userobj.getString("username"))
                        .putString("pass",userobj.getString("password"))
                        .apply()
                    Toast.makeText(activity, "User Berhasil Diupdate", Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener { error ->
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

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(user)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)

    }

}