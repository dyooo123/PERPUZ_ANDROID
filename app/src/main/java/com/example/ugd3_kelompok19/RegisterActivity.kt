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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val viewBinding = binding.root
        setContentView(viewBinding)
        var checkLogin = true


        var userId: Int = 0
        val db by lazy { UserDB(this) }
        val userDao = db.userDao()

        binding.btnRegister.setOnClickListener(View.OnClickListener {
            val mBundle = Bundle()
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)

            val username: String = binding.inputLayoutUsername.getEditText()?.getText().toString()
            val password: String = binding.inputLayoutPassword.getEditText()?.getText().toString()
            val email: String = binding.inputLayoutEmail.getEditText()?.getText().toString()
            val tanggalLahir: String =
                binding.inputLayoutTanggalLahir.getEditText()?.getText().toString()
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

            if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !tanggalLahir.isEmpty() && !noTelp.isEmpty()) {
                checkLogin = true
            }

            if (checkLogin == true) {

                val user = User(0, username, email, password, tanggalLahir, noTelp)
                userDao.addUser(user)

                val moveRegister = Intent(this@RegisterActivity, MainActivity::class.java)
                mBundle.putString(
                    "Username",
                    binding.inputLayoutUsername.getEditText()?.getText().toString()
                )
                mBundle.putString(
                    "Password",
                    binding.inputLayoutPassword.getEditText()?.getText().toString()
                )
                moveRegister.putExtra("register", mBundle)
                startActivity(moveRegister)
                binding1 = ActivityRegisterBinding.inflate(layoutInflater)
                setContentView(binding1!!.root)

                createNotificationChannel()
                sendNotification()

            }
            if (!checkLogin) return@OnClickListener
        })

        binding.btnReset.setOnClickListener {
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
        broadcastIntent.putExtra("toastMessage","Hello! "+binding.inputLayoutUsername.editText?.text.toString())
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
}