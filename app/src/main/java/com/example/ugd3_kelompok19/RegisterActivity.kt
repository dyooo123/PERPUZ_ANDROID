package com.example.ugd3_kelompok19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {
    private lateinit var inputusername: TextInputLayout
    private lateinit var inputpassword: TextInputLayout
    private lateinit var inputemail: TextInputLayout
    private lateinit var inputtanggalLahir: TextInputLayout
    private lateinit var inputnoTelp: TextInputLayout
    private lateinit var btnRegister: Button
    private lateinit var registerLayout: ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        inputusername = findViewById(R.id.inputLayoutUsername)
        inputpassword = findViewById(R.id.inputLayoutPassword)
        inputemail = findViewById(R.id.inputLayoutEmail)
        inputtanggalLahir = findViewById(R.id.inputLayoutTanggalLahir)
        inputnoTelp = findViewById(R.id.inputLayoutNoTelp)
        btnRegister = findViewById(R.id.btnRegister)
        val btnRegister: Button = findViewById(R.id.btnRegister)
        val btnReset: Button = findViewById(R.id.btnReset)
        var checkLogin = true

        btnRegister.setOnClickListener(View.OnClickListener  {
            val mBundle = Bundle()
            val intent = Intent (this, MainActivity::class.java)

            val username: String = inputusername.getEditText()?.getText().toString()
            val password: String = inputpassword.getEditText()?.getText().toString()
            val email: String = inputemail.getEditText()?.getText().toString()
            val tanggalLahir: String = inputtanggalLahir.getEditText()?.getText().toString()
            val noTelp: String = inputnoTelp.getEditText()?.getText().toString()

            if (username.isEmpty()) {
                inputusername.setError("Username must be filled with text")
                checkLogin = false
            }

            if (password.isEmpty()) {
                inputpassword.setError("Password must be filled with text")
                checkLogin = false
            }

            if (email.isEmpty()) {
                inputemail.setError("Email must be filled with text")
                checkLogin = false
            }

            if (tanggalLahir.isEmpty()) {
                inputtanggalLahir.setError("Tangal Lahir must be filled with text")
                checkLogin = false
            }

            if (noTelp.isEmpty()) {
                inputnoTelp.setError("Nomor Telepon must be filled with text")
                checkLogin = false
            }

            if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !tanggalLahir.isEmpty() && !noTelp.isEmpty()){
                checkLogin = true
            }



            if(inputusername.getEditText()?.getText()==null){
                inputusername.getEditText()?.setText("")
            }

            if(inputpassword.getEditText()?.getText()==null){
                inputpassword.getEditText()?.setText("")
            }

            if(checkLogin==true) {
                val moveRegister = Intent(this@RegisterActivity, MainActivity::class.java)
                mBundle.putString("Username",inputusername.getEditText()?.getText().toString())
                mBundle.putString("Password",inputpassword.getEditText()?.getText().toString())
                moveRegister.putExtra("register", mBundle)
                startActivity(moveRegister)

            }
            if (!checkLogin) return@OnClickListener

        })

            btnReset.setOnClickListener{
                inputusername.editText?.setText("")
                inputpassword.editText?.setText("")
                inputemail.editText?.setText("")
                inputnoTelp.editText?.setText("")
                inputtanggalLahir.editText?.setText("")

                inputusername.setError("")
                inputpassword.setError("")
                inputemail.setError("")
                inputnoTelp.setError("")
                inputtanggalLahir.setError("")

            }

    }

}