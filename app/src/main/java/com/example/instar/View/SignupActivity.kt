package com.example.instar.View

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.instar.Data.Register
import com.example.instar.Data.User
import com.example.instar.Network.MasterApplication
import com.example.instar.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)

        if((application as MasterApplication).checkisLogin()){
            finish()
            startActivity(Intent(this, PostListActivity::class.java))
        }else {
            setContentView(binding.root)
            setupListener()
        }
    }

    fun setupListener() {
        binding.tvSignup.setOnClickListener {
            Log.d("register","가입하기 클릭")
            register()
        }
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
        }
    }

    fun register() {
        val username = binding.etId.text.toString()
        val userpassword1 = binding.etPw.text.toString()
        val userpassword2 = binding.etPw2.text.toString()

        (application as MasterApplication).service.register(username, userpassword1, userpassword2).enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    Toast.makeText(this@SignupActivity, "가입에 성공하였습니다", Toast.LENGTH_LONG).show()
                    val user = response.body()
                    val token = user!!.token!!
                    saveUserToken(token)
                    (application as MasterApplication).creatRetrofit()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "가입에 실패하였습니다", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun saveUserToken(token: String) {
        val sp = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("login_sp", token)
        editor.commit()
    }
}