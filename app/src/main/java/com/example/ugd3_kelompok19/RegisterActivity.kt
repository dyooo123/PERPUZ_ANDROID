package com.example.ugd3_kelompok19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ugd3_kelompok19.databinding.ActivityRegisterBinding
import com.example.ugd3_kelompok19.room.User
import com.example.ugd3_kelompok19.room.UserDB
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_profil.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var inputusername: TextInputLayout
    private lateinit var inputpassword: TextInputLayout
    private lateinit var inputemail: TextInputLayout
    private lateinit var inputtanggalLahir: TextInputLayout
    private lateinit var inputnoTelp: TextInputLayout
    private lateinit var btnRegister: Button
    private lateinit var registerLayout: ConstraintLayout
    private lateinit var binding: ActivityRegisterBinding

    private var userId: Int = 0

    val db by lazy{ UserDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val viewBinding = binding.root
        setContentView(viewBinding)
        var checkLogin = true

        binding.btnRegister.setOnClickListener(View.OnClickListener  {
            val mBundle = Bundle()
            val intent = Intent (this@RegisterActivity, MainActivity::class.java)

            val username: String = binding.inputLayoutUsername.getEditText()?.getText().toString()
            val password: String = binding.inputLayoutPassword.getEditText()?.getText().toString()
            val email: String = binding.inputLayoutEmail.getEditText()?.getText().toString()
            val tanggalLahir: String = binding.inputLayoutTanggalLahir.getEditText()?.getText().toString()
            val noTelp: String = binding.inputLayoutNoTelp.getEditText()?.getText().toString()

            if (username.isEmpty()) {
                binding.inputLayoutUsername.setError("Username must be filled with text")
                checkLogin = false
            }

            if (password.isEmpty()) {
                binding.inputLayoutPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            if (email.isEmpty()) {
                binding.inputLayoutEmail.setError("Email must be filled with text")
                checkLogin = false
            }

            if (tanggalLahir.isEmpty()) {
                binding.inputLayoutTanggalLahir.setError("Tangal Lahir must be filled with text")
                checkLogin = false
            }

            if (noTelp.isEmpty()) {
                binding.inputLayoutNoTelp.setError("Nomor Telepon must be filled with text")
                checkLogin = false
            }

            if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !tanggalLahir.isEmpty() && !noTelp.isEmpty()){
                checkLogin = true
            }



            if(checkLogin==true) {
                val moveRegister = Intent(this@RegisterActivity, MainActivity::class.java)
                mBundle.putString("Username",binding.inputLayoutUsername.getEditText()?.getText().toString())
                mBundle.putString("Password",binding.inputLayoutPassword.getEditText()?.getText().toString())
                moveRegister.putExtra("register", mBundle)
                startActivity(moveRegister)

            }
            if (!checkLogin) return@OnClickListener


            CoroutineScope(Dispatchers.IO).launch {
                run {
                    db.userDao().addUser(
                        User(0, username, password, email, tanggalLahir, noTelp)
                    )
                    finish()
                }
            }
            intent.putExtra("register", mBundle)
            intent.putExtra("intent_id", 0)
            startActivity(intent)
        })

            binding.btnReset.setOnClickListener{
                binding.inputLayoutUsername.editText?.setText("")
                binding.inputLayoutPassword.editText?.setText("")
                binding.inputLayoutEmail.editText?.setText("")
                binding.inputLayoutNoTelp.editText?.setText("")
                binding.inputLayoutTanggalLahir.editText?.setText("")

                binding.inputLayoutUsername.setError("")
                binding.inputLayoutPassword.setError("")
                binding.inputLayoutEmail.setError("")
                binding.inputLayoutNoTelp.setError("")
                binding.inputLayoutTanggalLahir.setError("")

            }

    }



}