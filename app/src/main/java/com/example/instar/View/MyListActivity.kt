package com.example.instar.View

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
import com.example.instar.databinding.ActivityMyListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyListActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyListBinding
    lateinit var glide: RequestManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        glide = Glide.with(this@MyListActivity)

        tabCursor() // 탭이동

        createList() // 업로드한 나의 게시물 보기
    }

    fun tabCursor() {
        binding.tvAlllist.setOnClickListener {
            startActivity(Intent(this@MyListActivity, PostListActivity::class.java))
        }
        binding.tvUpload.setOnClickListener {
            startActivity(Intent(this@MyListActivity, UploadActivity::class.java))
        }
        binding.tvUserinfo.setOnClickListener {
            startActivity(Intent(this@MyListActivity, UserInfoActivity::class.java))
        }
    }

    fun createList() {
        (application as MasterApplication).service.getMyPosts().enqueue(
            object : Callback<ArrayList<Post>> {
                override fun onResponse(
                    call: Call<ArrayList<Post>>,
                    response: Response<ArrayList<Post>>
                ) {
                    if (response.isSuccessful) {
                        val myPostList = response.body()
                        val adapter = MyPostListAdapter(
                            myPostList!!,
                            LayoutInflater.from(this@MyListActivity),
                            glide
                        )
                        binding.postRecyclerview.adapter = adapter
                        binding.postRecyclerview.layoutManager =
                            LinearLayoutManager(this@MyListActivity)
                    }
                }

                override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {

                }
            }
        )
    }
}

class MyPostListAdapter(
    val postList: ArrayList<Post>,
    val inflater: LayoutInflater,
    val glide: RequestManager
) : RecyclerView.Adapter<MyPostListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postOwner: TextView
        val postImage: ImageView
        val postContent: TextView

        init {
            postOwner = itemView.findViewById(R.id.post_owner)
            postImage = itemView.findViewById(R.id.post_img)
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
        glide.load(postList.get(position).image).into(holder.postImage)
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}