package com.example.servertest

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.create
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var forGuvohnomaContract: ActivityResultLauncher<String>
    private lateinit var forPassportOldContract: ActivityResultLauncher<String>
    private lateinit var forPassportOrqaContract: ActivityResultLauncher<String>
    private lateinit var forRasm1Contract: ActivityResultLauncher<String>
    private lateinit var forRasm2Contract: ActivityResultLauncher<String>
    private lateinit var forRasm3Contract: ActivityResultLauncher<String>
    private lateinit var forRasm4Contract: ActivityResultLauncher<String>

    private val partList = mutableMapOf<Int, MultipartBody.Part>()

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fayllarni tanlash uchun launcherlar
        forGuvohnomaContract = registerImagePicker(7, "guvohnoma", "guvohnoma_rasm")
        forPassportOldContract = registerImagePicker(5, "passport_old", "passport_old_rasm")
        forPassportOrqaContract = registerImagePicker(6, "passport_orqa", "passport_orqa_rasm")
        forRasm1Contract = registerImagePicker(1, "rasm_1", "rasm_1")
        forRasm2Contract = registerImagePicker(2, "rasm_2", "rasm_2")
        forRasm3Contract = registerImagePicker(3, "rasm_3", "rasm_3")
        forRasm4Contract = registerImagePicker(4, "rasm_4", "rasm_4")

        // Tugmalarni bosilganda rasm tanlashni ishga tushirish
        findViewById<MaterialButton>(R.id.btn_guvohnoma).setOnClickListener {
            forGuvohnomaContract.launch("image/*")
            it.setBackgroundColor(Color.GREEN)
        }
        findViewById<MaterialButton>(R.id.btn_passport_old).setOnClickListener {
            forPassportOldContract.launch("image/*")
            it.setBackgroundColor(Color.GREEN)
        }
        findViewById<MaterialButton>(R.id.btn_passport_orqa).setOnClickListener {
            forPassportOrqaContract.launch("image/*")
            it.setBackgroundColor(Color.GREEN)
        }
        findViewById<MaterialButton>(R.id.btn_img_1).setOnClickListener {
            forRasm1Contract.launch("image/*")
            it.setBackgroundColor(Color.GREEN)
        }
        findViewById<MaterialButton>(R.id.btn_img_2).setOnClickListener {
            forRasm2Contract.launch("image/*")
            it.setBackgroundColor(Color.GREEN)
        }
        findViewById<MaterialButton>(R.id.btn_img_3).setOnClickListener {
            forRasm3Contract.launch("image/*")
            it.setBackgroundColor(Color.GREEN)
        }
        findViewById<MaterialButton>(R.id.btn_img_4).setOnClickListener {
            forRasm4Contract.launch("image/*")
            it.setBackgroundColor(Color.GREEN)
        }
        findViewById<MaterialButton>(R.id.btn_upload).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val token = "Token "+findViewById<EditText>(R.id.text_token).text.toString()
                    val kartaRaqam = createRequestBody(findViewById<EditText>(R.id.text_karta).text.toString())
                    val elonMatni = createRequestBody(findViewById<EditText>(R.id.text_elon).text.toString())
                    RetrofitInstance.api.uploadImage(
                        token, partList[1], partList[2], partList[3], partList[4],
                        partList[5], partList[6], partList[7], kartaRaqam, elonMatni
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun createRequestBody(s: String): RequestBody {
        return RequestBody.create(MultipartBody.FORM, s)
    }

    private fun registerImagePicker(index: Int, fileName: String, key: String): ActivityResultLauncher<String> {
        return registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                partList[index] = getImage(it, fileName, key)
            }
        }
    }

    private fun getImage(uri: Uri, fileName: String, key: String): MultipartBody.Part {
        val fileDir = applicationContext.filesDir
        val file = File(fileDir, "$fileName.png")

        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        } ?: throw IllegalArgumentException("Faylni o‘qib bo‘lmadi: $uri")

        val body = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(key, file.name, body)
    }
}
