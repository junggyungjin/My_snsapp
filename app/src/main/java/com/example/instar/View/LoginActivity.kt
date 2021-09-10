package com.example.instar.View

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.instar.Data.User
import com.example.instar.Network.MasterApplication
import com.example.instar.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLogin.setOnClickListener {
            login()
        }

        binding.tvSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    fun login() {
        val username = binding.etId.text.toString()
        val userpassword = binding.etPw.text.toString()
        (application as MasterApplication).service.login(username, userpassword).enqueue(
            object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if(response.isSuccessful){
                        val user = response.body()
                        val token = user!!.token
                        saveUserToken(token!!)
                        (application as MasterApplication).creatRetrofit()
                        Toast.makeText(this@LoginActivity, "로그인성공!",Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@LoginActivity, PostListActivity::class.java))
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "로그인실패",Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    fun saveUserToken(token: String) {
        val sp = getSharedPreferences("login_sp",Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("login_sp", token)
        editor.commit()
    }
}