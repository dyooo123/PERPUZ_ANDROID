package com.example.ugd3_kelompok19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ugd3_kelompok19.databinding.ActivityMainBinding
import com.example.ugd3_kelompok19.databinding.ActivityRegisterBinding
import com.example.ugd3_kelompok19.room.User
import com.example.ugd3_kelompok19.room.UserDB
import com.example.ugd3_kelompok19.room.UserDao
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_profil.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd3_kelompok19.api.profilApi
import com.example.ugd3_kelompok19.models.user
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class RegisterActivity : AppCompatActivity() {
    private lateinit var inputusername: TextInputLayout
    private lateinit var inputpassword: TextInputLayout
    private lateinit var inputemail: TextInputLayout
    private lateinit var inputtanggalLahir: TextInputLayout
    private lateinit var inputnoTelp: TextInputLayout
    private lateinit var btnRegister: Button
    private lateinit var registerLayout: ConstraintLayout
    private lateinit var binding: ActivityRegisterBinding
    private var binding1: ActivityRegisterBinding? = null
    private val REGISTER_ID_01 = "register_notification"
    private val notificationId1 = 101
    private var queue: RequestQueue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val viewBinding = binding.root
        setContentView(viewBinding)
        var checkLogin = true

        queue = Volley.newRequestQueue(this)
        var userId: Int = 0
        val db by lazy { UserDB(this) }
        val userDao = db.userDao()

        binding.btnRegister.setOnClickListener(View.OnClickListener {
            val mBundle = Bundle()
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)

            val username: String = binding.etUsername.getEditText()?.getText().toString()
            val password: String = binding.etPassword.getEditText()?.getText().toString()
            val email: String = binding.etEmail.getEditText()?.getText().toString()
            val tanggalLahir: String = binding.etTanggalLahir.getEditText()?.getText().toString()
            val noTelp: String = binding.etNoTelp.getEditText()?.getText().toString()

            if (username.isEmpty()) {
                binding.etUsername.setError("Username must be filled with text")
                checkLogin = false
            }

            if (password.isEmpty()) {
                binding.etPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            if (email.isEmpty()) {
                binding.etEmail.setError("Email must be filled with text")
                checkLogin = false
            }

            if (tanggalLahir.isEmpty()) {
                binding.etTanggalLahir.setError("Tangal Lahir must be filled with text")
                checkLogin = false
            }

            if (noTelp.isEmpty()) {
                binding.etNoTelp.setError("Nomor Telepon must be filled with text")
                checkLogin = false
            }

            if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !tanggalLahir.isEmpty() && !noTelp.isEmpty()) {
                checkLogin = true
            }

            if (checkLogin == true) {
                addUser(mBundle)


//                val user = User(0, username, email, password, tanggalLahir, noTelp)
//                userDao.addUser(user)
//
//                val moveRegister = Intent(this@RegisterActivity, MainActivity::class.java)
//                mBundle.putString(
//                    "Username",
//                    binding.inputLayoutUsername.getEditText()?.getText().toString()
//                )
//                mBundle.putString(
//                    "Password",
//                    binding.inputLayoutPassword.getEditText()?.getText().toString()
//                )
//                moveRegister.putExtra("register", mBundle)
//                startActivity(moveRegister)
//                binding1 = ActivityRegisterBinding.inflate(layoutInflater)
//                setContentView(binding1!!.root)
//
                createNotificationChannel()
                sendNotification()

            }
            if (!checkLogin) return@OnClickListener
        })

        binding.btnReset.setOnClickListener {
            binding.etUsername.editText?.setText("")
            binding.etPassword.editText?.setText("")
            binding.etEmail.editText?.setText("")
            binding.etNoTelp.editText?.setText("")
            binding.etTanggalLahir.editText?.setText("")

            binding.etUsername.setError("")
            binding.etPassword.setError("")
            binding.etEmail.setError("")
            binding.etNoTelp.setError("")
            binding.etTanggalLahir.setError("")

        }


    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(
                REGISTER_ID_01,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = descriptionText
            }


            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotification() {

        val intent: Intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val broadcastIntent: Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage","Hello, Welcome to our Apps, "+binding.etUsername.editText?.text.toString() + "!")
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val picture = BitmapFactory.decodeResource(resources, R.drawable.logo)
        val builder = NotificationCompat.Builder(this, REGISTER_ID_01)
            .setSmallIcon(R.drawable.logo)
            .setContentText("Berhasil Register")
            .setLargeIcon(picture)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigLargeIcon(null)
                    .bigPicture(picture)
            )
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.RED)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "Pesan", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId1, builder.build())
        }
    }
    private fun addUser(mBundle: Bundle){

        val users = user(0,
        binding.etUsername.getEditText()?.getText().toString(),
        binding.etPassword.getEditText()?.getText().toString(),
        binding.etEmail.getEditText()?.getText().toString(),
        binding.etTanggalLahir.getEditText()?.getText().toString(),
        binding.etNoTelp.getEditText()?.getText().toString())

        val stringRequest: StringRequest = object: StringRequest(Method.POST, profilApi.REG, Response.Listener { response ->
                val gson = Gson()
                val user = gson.fromJson(response, user::class.java)

                if (user != null)
                    MotionToast.createColorToast(this,
                        "Register Completed!",
                        "Akun anda terdaftar!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))

                val moveRegister = Intent(this@RegisterActivity, MainActivity::class.java)
                mBundle.putString(
                    "username",
                    binding.etUsername.getEditText()?.getText().toString()
                )
                mBundle.putString(
                    "password",
                    binding.etPassword.getEditText()?.getText().toString()

                )
                moveRegister.putExtra("register", mBundle)
                startActivity(moveRegister)
                createNotificationChannel()
                sendNotification()
            }, Response.ErrorListener { error->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@RegisterActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
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
                    val requestBody = gson.toJson(users)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        // Menambahkan request ke request queue
        queue!!.add(stringRequest)
    }
}