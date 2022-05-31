package com.healstationlab.design.ui.report

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.healstationlab.design.R
import com.healstationlab.design.databinding.ActivityReport4Binding
import com.healstationlab.design.dto.getQuestion
import com.healstationlab.design.resource.App
import com.healstationlab.design.resource.Constant
import com.healstationlab.design.resource.Retrofit_Mansae
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Report4Activity : AppCompatActivity(), View.OnClickListener {

    private lateinit var one : String
    private lateinit var two : String
    private lateinit var three : String
    var four : HashMap<String, ArrayList<Int>> = HashMap()
    var five : HashMap<String, ArrayList<Int>> = HashMap()
//    var six : HashMap<String, ArrayList<Int>> = HashMap()
    var list : ArrayList<Int> = arrayListOf()


    lateinit var binding : ActivityReport4Binding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityReport4Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.textView332.text = "${App.prefs.getStringData(Constant.NAME)} 회원님에 대한 정보를 더 주세요"

        one = intent.getStringExtra("1")!!
        two = intent.getStringExtra("2")!!
        three = intent.getStringExtra("3")!!

        getMyReport()

        binding.imageView102.setOnClickListener {
            onBackPressed()
        }

        binding.textView406.setOnClickListener {
            if(three.isNotEmpty()){
                val intent = Intent(this, Report5Activity::class.java)
                intent.putExtra("1", one)
                intent.putExtra("2", two)
                intent.putExtra("3", three)
                intent.putExtra("4", four["4"].toString())
                startActivity(intent)
                overridePendingTransition(R.xml.slide_left, R.xml.no_chagne)
            } else {
                Toast.makeText(this, "항목을 선택해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        /** 클렌징(세안)후 당신의 얼굴 피부 상태는 어떤가요? **/
        binding.cleansingBtn1.setOnClickListener(this)
        binding.cleansingBtn2.setOnClickListener(this)
        binding.cleansingBtn3.setOnClickListener(this)
        binding.cleansingBtn4.setOnClickListener(this)
    }

    fun firstCheck(){
        if(four["4"]!!.isNotEmpty()){
            when(four["4"]){
                arrayListOf(16) -> { radio(binding.cleansingBtn1, binding.cleasingCheck1)}
                arrayListOf(17) -> {radio(binding.cleansingBtn2, binding.cleasingCheck2)}
                arrayListOf(18) -> {radio(binding.cleansingBtn3, binding.cleasingCheck3)}
                arrayListOf(19)-> {radio(binding.cleansingBtn4, binding.cleasingCheck4)}
            }
        }
    }

    private fun radio(btn : TextView, check : ImageView){
        btn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F05E49"))
        btn.setTextColor(Color.parseColor("#ffffff"))
        check.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
    }

    private fun radio1VisibleFalse(){
        binding.cleansingBtn1.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F2F5F5"))
        binding.cleansingBtn2.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F2F5F5"))
        binding.cleansingBtn3.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F2F5F5"))
        binding.cleansingBtn4.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F2F5F5"))

        binding.cleansingBtn1.setTextColor(Color.parseColor("#777777"))
        binding.cleansingBtn2.setTextColor(Color.parseColor("#777777"))
        binding.cleansingBtn3.setTextColor(Color.parseColor("#777777"))
        binding.cleansingBtn4.setTextColor(Color.parseColor("#777777"))

        binding.cleasingCheck1.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
        binding.cleasingCheck2.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
        binding.cleasingCheck3.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
        binding.cleasingCheck4.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
    }


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, R.xml.slide_right)
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.cleansing_btn1 -> {
                four.clear()
                radio1VisibleFalse()
                radio(binding.cleansingBtn1, binding.cleasingCheck1)
                four["4"] = arrayListOf(16)
            }
            R.id.cleansing_btn2 -> {
                four.clear()
                radio1VisibleFalse()
                radio(binding.cleansingBtn2, binding.cleasingCheck2)
                four["4"] = arrayListOf(17)
            }
            R.id.cleansing_btn3 -> {
                four.clear()
                radio1VisibleFalse()
                radio(binding.cleansingBtn3, binding.cleasingCheck3)
                four["4"] = arrayListOf(18)
            }
            R.id.cleansing_btn4 -> {
                four.clear()
                radio1VisibleFalse()
                radio(binding.cleansingBtn4, binding.cleasingCheck4)
                four["4"] = arrayListOf(19)
            }
        }
    }

    private fun getMyReport(){
        Retrofit_Mansae.server.myQuestion()
                .enqueue(object : Callback<getQuestion>{
                    override fun onFailure(call: Call<getQuestion>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<getQuestion>, response: Response<getQuestion>) {
                        when(response.body()?.responseCode){
                            "SUCCESS" -> {
                                val answer : ArrayList<Int> = arrayListOf()
                                if(response.body()!!.data.isNotEmpty()){
                                    for(i in response.body()!!.data[response.body()!!.data.size-1].answers){ answer.add(i.option.id) }
                                    four["4"] = arrayListOf(answer[3])
                                    five["5"] = arrayListOf(answer[4])

                                    firstCheck()
                                }
                            }
                        }
                    }
                })
    }
}