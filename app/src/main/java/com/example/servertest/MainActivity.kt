package com.example.servertest

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    lateinit var editText: EditText
    lateinit var imageView: ImageView
    lateinit var selectButton: MaterialButton
    lateinit var uploadButton: MaterialButton
    lateinit var imageUri:Uri

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val contract = registerForActivityResult(ActivityResultContracts.GetContent()){
            imageUri = it!!
            imageView.setImageURI(it)
        }

        editText = findViewById(R.id.edit_text)
        imageView = findViewById(R.id.imageView)
        selectButton = findViewById(R.id.select_image)
        uploadButton = findViewById(R.id.upload_button)

        selectButton.setOnClickListener {
            contract.launch("image/*")
        }

        uploadButton.setOnClickListener {
            val fileDir = applicationContext.filesDir
            val file = File(fileDir, "image.png")
            val inputStream = contentResolver.openInputStream(imageUri)
            val outputStream = FileOutputStream(file)
            inputStream!!.copyTo(outputStream)

            val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
            val part = MultipartBody.Part.createFormData("rasmlar", file.name, requestBody)

            lifecycleScope.launch(Dispatchers.IO){
                val result = RetrofitInstance.api.uploadImage(part)
                withContext(Dispatchers.Main){
                    Toast.makeText(this@MainActivity, ""+result.rasmlar, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}