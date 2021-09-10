package com.example.instar.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.instar.Network.MasterApplication
import com.example.instar.R
import com.example.instar.databinding.ActivityUserInfoBinding

class UserInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabCursor()

        binding.tvLogout.setOnClickListener {
            (application as MasterApplication).logout()
            finish()
            startActivity(Intent(this@UserInfoActivity, LoginActivity::class.java))
        }
    }

    fun tabCursor() {
        binding.tvMylist.setOnClickListener {
            startActivity(Intent(this@UserInfoActivity, MyListActivity::class.java))
        }

        binding.tvUpload.setOnClickListener {
            startActivity(Intent(this@UserInfoActivity, UploadActivity::class.java))
        }

        binding.tvAlllist.setOnClickListener {
            startActivity(Intent(this@UserInfoActivity, PostListActivity::class.java))
        }
    }
}