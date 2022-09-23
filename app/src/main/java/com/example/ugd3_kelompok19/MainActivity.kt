package com.example.ugd3_kelompok19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    var mBundle : Bundle? = null
    var tempUsername : String = "admin"
    var tempPass : String = "admin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            .getBoolean("isFirstRun", true)
        if(isFirstRun) {
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

        if(intent.getBundleExtra("register")!=null){
            mBundle = intent.getBundleExtra("register")!!
            tempUsername = mBundle!!.getString("Username")!!
            tempPass = mBundle!!.getString("Password")!!
            println(tempUsername)
            inputUsername.editText?.setText(tempUsername)
            inputPassword.editText?.setText(tempPass)
        }

        btnRegister.setOnClickListener {
            val moveLogin = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(moveLogin)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin = false
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

            if (username == "admin" && password == "admin" || (username == tempUsername && password == tempPass)){
                checkLogin = true

                Snackbar.make(mainLayout, "Berhasil Login", Snackbar.LENGTH_LONG).show()

                val moveLogin = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(moveLogin)
            }else{
                Snackbar.make(mainLayout, "Username atau Password Salah!", Snackbar.LENGTH_LONG).show()

            }
            if (!checkLogin) return@OnClickListener

        })

    }
}