package com.healstationlab.design.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.healstationlab.design.R
import com.healstationlab.design.adapter.CommentAdapter
import com.healstationlab.design.adapter.HorizontalImgAdapter
import com.healstationlab.design.databinding.ActivityDetailChatBinding
import com.healstationlab.design.dto.comment
import com.healstationlab.design.dto.detailChat
import com.healstationlab.design.model.Comment
import com.healstationlab.design.resource.Constant
import com.healstationlab.design.resource.Retrofit_Mansae
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailChatActivity : AppCompatActivity() {
//    var resRaw = ""
    val commentList : ArrayList<Comment> = arrayListOf()
    var imgList : ArrayList<Any> = arrayListOf()
    var commentAdapter : CommentAdapter? = null
    var category = ""

    var skin = ""
    var type = ""
    var age = ""

    lateinit var binding : ActivityDetailChatBinding
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.frameLayout10.setOnClickListener {
            hideKeyboard()
        }

        Constant.edit_check = false

        id = intent.getIntExtra("id", 0) // 프래그먼트에서 넘어온 id

        when(intent.getStringExtra("category")){
            "FREE" -> category = "자유"
            "PORE" -> category = "모공"
            "BLACK_HEAD" -> category = "블랙헤드"
            "COLORED" -> category = "색소"
            "WRINKLE" -> category = "주름"
            "TROUBLE" -> category = "트러블"
            "SENSITIVITY" -> category = "민감"
            "DARK_CIRCLE" -> category = "다크서클"
        }

        binding.freeText.text = "자유"

        /** 수다방 댓글 달기 **/
        binding.commentSendBtn.setOnClickListener {
            if(binding.commentEdit.text.toString().isNotEmpty()){
                postComment(id)
            } else {
                Toast.makeText(this, "댓글 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        /** 자유 버튼 **/
        binding.freeBtn.setOnClickListener {
            visivleView(binding.freeBtn, binding.freeImg, binding.freeText, binding.shareBtn, binding.shareImg, binding.shareText)
        }

        /** 공유버튼 **/
        binding.shareBtn.setOnClickListener {
            visivleView2(binding.freeBtn, binding.freeImg, binding.freeText, binding.shareBtn, binding.shareImg, binding.shareText)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "https://mansae.page.link/?link=https://api.ggumnol.net/?boardid%3D${id}&apn=com.healstationlab.design&isi=1576152941&ibi=com.digi.mansae")
            val chooser = Intent.createChooser(intent, "공유하기")
            startActivity(chooser)
        }

        binding.backBtn.setOnClickListener {
            finish()
            overridePendingTransition(0, R.xml.slide_right)
        }

        getComment(id)
        getCommentList(id)
    }

    /** 수다방 단건 조회 **/
    private fun getComment(id : Int){
        Retrofit_Mansae.server.getChatDetail(id = id)
            .enqueue(object : Callback<detailChat> {
                override fun onFailure(call: Call<detailChat>, t: Throwable) {

                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<detailChat>, response: Response<detailChat>) {
                    when(response.body()!!.responseCode){
                        "SUCCESS" -> {
                            if(response.body()?.data?.imageUrls!!.isNotEmpty()){
                                imgList = response.body()!!.data.imageUrls

                                when(imgList.size){
                                    0 -> {
                                        binding.horizontalRecyclerView.isVisible = false
                                    }
                                    else -> {
                                        binding.horizontalRecyclerView.isVisible = true
                                        val imgAdapter = HorizontalImgAdapter(imgList)

                                        binding.horizontalRecyclerView.apply {
                                            adapter = imgAdapter
                                            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                                        }

                                        imgAdapter.setItemClickListner(object : HorizontalImgAdapter.ItemClickListener{
                                            override fun onClick(view: View, position: Int) {
                                                val intent = Intent(this@DetailChatActivity, MyFaceActivity::class.java)
                                                intent.putExtra("img", imgList[position].toString())
                                                startActivity(intent)
                                                overridePendingTransition(R.xml.slide_left, R.xml.no_chagne)
                                            }

                                            override fun onDelete(view: View, position: Int) {

                                            }
                                        })
                                    }
                                }
                            }

                            if (!response.body()?.data!!.user?.skinProblems.isNullOrEmpty()) {
                                for (i in 0 until response.body()?.data!!.user?.skinProblems!!.size) {
                                    type = when (response.body()?.data!!.user?.skinProblems!![i]) {
                                        "PIMPLE" -> "여드름"
                                        "SENSITIVITY" -> "민감성 피부"
                                        "SHADE_SPOT" -> "색조반점"
                                        "DARK_CIRCLE" -> "다크서클"
                                        "PORE" -> "모공"
                                        "BLACK_HEAD" -> "블랙헤드"
                                        "WRINKLE" -> "주름"
                                        else -> {
                                            ""
                                        }
                                    }
                                    if(type != ""){
                                        skin += "$type | "
                                    }
                                }
                            }

                            if(response.body()!!.data.user?.id != 1){
                                val birthSplit = response.body()?.data!!.user!!.birthDate.split("-")

                                when(2021-birthSplit[0].toInt()+1) {
                                    in 10..19 -> age = "10대 | "
                                    in 20..29 -> age = "20대 | "
                                    in 30..39 -> age = "30대 | "
                                    in 40..49 -> age = "40대 | "
                                    in 50..59 -> age = "50대 | "
                                    in 60..69 -> age = "60대 | "
                                }

                                if (response.body()?.data!!.user?.recommendProductCode != null){
                                    val skinSplit = (age + skin + response.body()?.data!!.user?.recommendProductCode).split("A")
                                    if (skinSplit.count() > 1){
                                        binding.textView120.text = skinSplit[0] + skinSplit[1].removeRange(0, 1)
                                    } else {
                                        binding.textView120.text = skinSplit[0]
                                    }
                                } else {
                                    binding.textView120.text = age.replace("|","")
                                }
                            }

                            if (response.body()?.data!!.user?.imageUrl != null) {
                                Glide.with(this@DetailChatActivity).load(response.body()?.data!!.user?.imageUrl).into(binding.imageView38)
                            }



                            binding.textView119.text = response.body()?.data!!.createdDate
                            binding.textView121.text = response.body()?.data?.contents

//                            resRaw = response.raw().request.url.toString()
                            binding.textView118.text = response.body()!!.data.user?.nickname // 이메일
//                            binding.textView121.text = response.body()!!.data.contents // 내용
//                            binding.textView120.text = intent.getStringExtra("skin")
//                            binding.textView119.text = intent.getStringExtra("create")

                            if(intent.getStringExtra("img") != null){
                                Glide.with(this@DetailChatActivity).load(intent.getStringExtra("img")).into(binding.imageView38)
                            }
                        }
                    }
                }
            })
    }

    /** 수다방 댓글 리스트 **/
    fun getCommentList(id : Int){
        Retrofit_Mansae.server.getChatComment(id = id)
            .enqueue(object : Callback<comment>{
                override fun onFailure(call: Call<comment>, t: Throwable) {

                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<comment>, response: Response<comment>) {
                    when(response.body()!!.responseCode){
                        "SUCCESS" -> {
                            commentList.clear()
                            for(i in response.body()!!.data){
                                commentList.add(
                                    Comment(
                                        R.drawable.pro1,
                                        i.user.nickname,
                                        i.contents,
                                        i.updatedTime,
                                        i.user.imageUrl
                                    )
                                )
                            }
                            commentAdapter = CommentAdapter(commentList)
                            commentAdapter!!.notifyDataSetChanged()

                            binding.commentRecyclerView.apply {
                                this.adapter = commentAdapter
                                layoutManager = LinearLayoutManager(context)
                            }
                        }
                    }
                }
            })
    }

    /** 댓글 달기 **/
    private fun postComment(id : Int){
        val body : HashMap<String, String> = HashMap()
        body["content"] = binding.commentEdit.text.toString()

        Retrofit_Mansae.server.postComment(
            id = id,
            body = body
        ).enqueue(object : Callback<Unit>{
            override fun onFailure(call: Call<Unit>, t: Throwable) {
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                when(response.code()){
                    200 -> {
                        Toast.makeText(this@DetailChatActivity, "성공적으로 댓글을 달았습니다.", Toast.LENGTH_SHORT).show()
                        getCommentList(id)

                    }

                    else -> {
                        Toast.makeText(this@DetailChatActivity, "서버에러 댓글을 못달았습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun visivleView(background : ImageButton, icon : ImageView, text : TextView, background2 : ImageButton, icon2 : ImageView, text2 : TextView) {
        background.setImageResource(R.drawable.half_radius_left_green)
        icon.setColorFilter(Color.WHITE)
        text.setTextColor(Color.WHITE)

        background2.setImageResource(R.drawable.half_radius_right)
        icon2.setColorFilter(Color.GRAY)
        text2.setTextColor(Color.parseColor("#727272"))
    }

    private fun visivleView2(background : ImageButton, icon : ImageView, text : TextView, background2 : ImageButton, icon2 : ImageView, text2 : TextView) {
        background2.setImageResource(R.drawable.half_radius_right_green)
        icon2.setColorFilter(Color.WHITE)
        text2.setTextColor(Color.WHITE)

        background.setImageResource(R.drawable.half_radius_left)
        icon.setColorFilter(Color.GRAY)
        text.setTextColor(Color.parseColor("#727272"))
    }

    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.commentEdit.windowToken, 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, R.xml.slide_right)
    }
}