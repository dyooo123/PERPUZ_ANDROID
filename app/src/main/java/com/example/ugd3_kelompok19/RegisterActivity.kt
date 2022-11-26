package com.example.ugd3_kelompok19

import android.annotation.SuppressLint
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
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment

import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
                createPdf(username,password,email,tanggalLahir,noTelp)
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

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Throws(
        FileNotFoundException::class
    )
    private fun createPdf(username: String, password: String, email: String, tanggalLahir: String, noTelp: String) {
        //akses writing ke storage hp dalam mode download
        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString()
        val file = File(pdfPath, "pdf_UGD19_PERPUZ.pdf")
        FileOutputStream(file)

        //inisialisasi pembuatan PDF
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(5f, 5f, 5f, 5f)
        @SuppressLint("useCompatLoadingForDrawables") val d = getDrawable(R.drawable.logo)

        //penambahan gambar
        val bitmap = (d as BitmapDrawable)!!.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapData = stream.toByteArray()
        val imageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)
        val namapengguna = Paragraph("Identitas Pengguna").setBold().setFontSize(24f)
            .setTextAlignment(TextAlignment.CENTER)
        val group = Paragraph(
            """
                                    Berikut adalah
                                    Nama Pengguna UAJY 2022/2023
                                """.trimIndent()
        ).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)

        //pembuatan table
        val width = floatArrayOf(100f, 100f)
        val table = Table(width)
        //pengisian data ke dalam tabel
        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Username")))
        table.addCell(Cell().add(Paragraph(username)))
        table.addCell(Cell().add(Paragraph("Password")))
        table.addCell(Cell().add(Paragraph(password)))
        table.addCell(Cell().add(Paragraph("Email")))
        table.addCell(Cell().add(Paragraph(email)))
        table.addCell(Cell().add(Paragraph("Tanggal Lahir")))
        table.addCell(Cell().add(Paragraph(tanggalLahir)))
        table.addCell(Cell().add(Paragraph("Nomor Telepon")))
        table.addCell(Cell().add(Paragraph(noTelp)))
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        table.addCell(Cell().add(Paragraph("Tanggal Buat PDF")))
        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
        table.addCell(Cell().add(Paragraph("Pukul Pembuatan")))
        table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))

        //pembuatan QR CODE secara generate dengan bantuan IText7
        val barcodeQRCode = BarcodeQRCode(
            """
            $username
            $password
            $email
            $tanggalLahir
            $noTelp
            ${LocalDate.now().format(dateTimeFormatter)}
            ${LocalTime.now().format(timeFormatter)}
        """.trimIndent()
        )
        val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
        val qrCodeImage =
            Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(HorizontalAlignment.CENTER)

        document.add(image)
        document.add(namapengguna)
        document.add(group)
        document.add(table)
        document.add(qrCodeImage)

        document.close()
        Toast.makeText(this, "Pdf Created", Toast.LENGTH_SHORT).show()

    }
    }