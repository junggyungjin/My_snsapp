package com.example.instar.View

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.instar.Data.Post
import com.example.instar.Network.MasterApplication
import com.example.instar.R
import com.example.instar.databinding.ActivityUploadBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadActivity : AppCompatActivity() {
    lateinit var binding: ActivityUploadBinding
    lateinit var filePath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvChoicepicture.setOnClickListener {
            getPicture() // 카메라 앨범에 접근
            storagePermission() // 권한 허용

        }

        binding.tvPictureupload.setOnClickListener {
            uploadPost() // 게시물 업로드
        }

        tabCursor() // 탭 이동
    }

    fun storagePermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this@UploadActivity,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1000
            )
        }else {
            Log.d("permissionsss", "권한이 이미 있음")
        }
    }

    fun tabCursor() {
        binding.tvAlllist.setOnClickListener {
            startActivity(Intent(this@UploadActivity, PostListActivity::class.java))
        }
        binding.tvMylist.setOnClickListener {
            startActivity(Intent(this@UploadActivity, MyListActivity::class.java))
        }
        binding.tvUserinfo.setOnClickListener {
            startActivity(Intent(this@UploadActivity, UserInfoActivity::class.java))
        }
    }

    fun getPicture() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setType("image/*")
        resultLauncher.launch(intent)
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data : Intent? = result.data
            filePath = getImageFilePath(data!!.data!!)
        }
    }

    // 경로를 알아내는 함수
    fun getImageFilePath(contentUri : Uri): String {
        var columnIndex = 0
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, projection, null, null, null)
        if(cursor!!.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        return cursor.getString(columnIndex)
    }

    fun uploadPost() {
        val file = File(filePath)
        val fileRequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val part = MultipartBody.Part.createFormData("image", file.name, fileRequestBody)
        val content = RequestBody.create(MediaType.parse("text/plain"), getContent())

        (application as MasterApplication).service.uploadPost(
            part, content
        ).enqueue(object : Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if(response.isSuccessful){
                    finish()
                    startActivity(Intent(this@UploadActivity, MyListActivity::class.java))
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {

            }
        })
    }

    fun getContent():String {
        return binding.etContent.text.toString()
    }
}