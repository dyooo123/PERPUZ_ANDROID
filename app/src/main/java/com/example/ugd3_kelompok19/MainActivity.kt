package com.example.ugd3_kelompok19

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ugd3_kelompok19.room.UserDB
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.graphics.Color
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok19.api.profilApi
import com.example.ugd3_kelompok19.databinding.ActivityMainBinding
import com.example.ugd3_kelompok19.models.user
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    private var queue: RequestQueue? = null
    lateinit  var mBundle: Bundle
    var tempUsername: String? = null
    var tempPass: String? = null

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        queue = Volley.newRequestQueue(this)
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            .getBoolean("isFirstRun", true)
        if (isFirstRun) {
            startActivity(Intent(this@MainActivity, SplashScreen::class.java))
            finish()
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
            .putBoolean("isFirstRun", false).commit()

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.mainLayout)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val btnLogin: Button = findViewById(R.id.btnLogin)

        if (intent.getBundleExtra("register") != null) {
            mBundle = intent.getBundleExtra("register")!!
            tempUsername = mBundle!!.getString("username")!!
            tempPass = mBundle!!.getString("password")!!
            println(tempUsername)
            inputUsername.editText?.setText(tempUsername)
            inputPassword.editText?.setText(tempPass)
        }

        btnRegister.setOnClickListener {
            val moveLogin = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(moveLogin)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = true
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if (username.isEmpty()) {
                inputUsername.setError("Username must be filled with text")
                checkLogin = false
            }

            if (password.isEmpty()) {
                inputPassword.setError("Password must be filled with text")
                checkLogin = false
            }

//            if (username == "admin" && password == "admin" || (username == tempUsername && password == tempPass)) {
//                checkLogin = true
//                val db by lazy { UserDB(this@MainActivity) }
//                val userDao = db.userDao()
//                val user = userDao.checkUser(username, password)
//                if (user != null) {
//                    sharedPreferences.edit()
//                        .putInt("id", user.id)
//                        .apply()
//                }
//
//                Snackbar.make(mainLayout, "Berhasil Login", Snackbar.LENGTH_LONG).show()
//
//                val moveLogin = Intent(this@MainActivity, HomeActivity::class.java)
//                startActivity(moveLogin)
//            } else {
//                Snackbar.make(mainLayout, "Username atau Password Salah!", Snackbar.LENGTH_LONG)
//                    .show()
//
//            }
            if (!checkLogin) {
                return@OnClickListener
            }else {
                LoginUser()
            }

        })
    }

    private fun LoginUser() {
        //  setLoading(true)

        val userprofil = user(
            0,
            inputUsername.getEditText()?.getText().toString(),
            inputPassword.getEditText()?.getText().toString(),
            "",
            "",
            ""

        )
        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, profilApi.LOG, Response.Listener { response ->
                val gson = Gson()
                var user = gson.fromJson(response, user::class.java)

                if(user!=null) {
                    var resJO = JSONObject(response.toString())
                    val  userobj = resJO.getJSONObject("data")

                    Toast.makeText(this@MainActivity, "Berhasil Login", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    sharedPreferences.edit()
                        .putInt("id",userobj.getInt("id"))
                        .putString("username",userobj.getString("username"))
                        .putString("password",userobj.getString("password"))
                        .apply()
                    startActivity(intent)
                }else {
                    Toast.makeText(this@MainActivity, "Gagal Login", Toast.LENGTH_SHORT).show()
                    return@Listener
                }

            }, Response.ErrorListener { error ->
                // setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@MainActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
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
                    val requestBody = gson.toJson(userprofil)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }

}