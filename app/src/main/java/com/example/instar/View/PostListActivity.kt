package com.example.instar.View

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.instar.Data.Post
import com.example.instar.Network.MasterApplication
import com.example.instar.R
import com.example.instar.databinding.ActivityPostListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostListActivity : AppCompatActivity() {
    lateinit var binding: ActivityPostListBinding
    lateinit var glide : RequestManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        glide = Glide.with(this)

        createAllList() // 전체 게시물 보기

        tabCursor() // 탭 이동
    }

    fun createAllList() {
        (application as MasterApplication).service.getAllPosts().enqueue(
            object : Callback<ArrayList<Post>>{
                override fun onResponse(
                    call: Call<ArrayList<Post>>,
                    response: Response<ArrayList<Post>>
                ) {
                    if(response.isSuccessful){
                        val postList = response.body()
                        val adapter = PostAdapter(
                            postList!!,
                            LayoutInflater.from(this@PostListActivity),
                            glide
                        )
                        binding.postRecyclerview.adapter = adapter
                        binding.postRecyclerview.layoutManager = LinearLayoutManager(this@PostListActivity)
                    }
                }

                override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {

                }
            }
        )
    }

    fun tabCursor() {
        binding.tvMylist.setOnClickListener {
            startActivity(Intent(this@PostListActivity, MyListActivity::class.java))
        }

        binding.tvUpload.setOnClickListener {
            startActivity(Intent(this@PostListActivity, UploadActivity::class.java))
        }

        binding.tvUserinfo.setOnClickListener {
            startActivity(Intent(this@PostListActivity, UserInfoActivity::class.java))
        }
    }
}

class PostAdapter(
    var postList: ArrayList<Post>,
    val inflater: LayoutInflater,
    val glide: RequestManager
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postOwner : TextView
        val postImageView : ImageView
        val postContent : TextView

        init {
            postOwner = itemView.findViewById(R.id.post_owner)
            postImageView = itemView.findViewById(R.id.post_img)
            postContent = itemView.findViewById(R.id.post_content)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.post_item_view_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.postOwner.setText(postList.get(position).owner)
        holder.postContent.setText(postList.get(position).content)
        glide.load(postList.get(position).image).into(holder.postImageView)
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}