@file:Suppress("DEPRECATION")

package com.healstationlab.design.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.healstationlab.design.R
import com.healstationlab.design.adapter.HomeAdapter
import com.healstationlab.design.adapter.SkinDotAdapter
import com.healstationlab.design.dto.banner
import com.healstationlab.design.dto.popup
import com.healstationlab.design.dto.reportMeitu
import com.healstationlab.design.dto.userInfo
import com.healstationlab.design.model.Banner
import com.healstationlab.design.model.DotModel
import com.healstationlab.design.resource.App
import com.healstationlab.design.resource.Constant
import com.healstationlab.design.resource.Constant.Companion.CURRENT_SKIN_POSITION
import com.healstationlab.design.resource.ProgressDialog
import com.healstationlab.design.resource.Retrofit_Mansae
import com.healstationlab.design.ui.MainActivity
import com.healstationlab.design.ui.MySkinResultListActivity
import com.healstationlab.design.ui.PopupActivity
import com.healstationlab.design.ui.report.ReportActivity
import me.relex.circleindicator.CircleIndicator3
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
    var mainActivity: MainActivity? = null


    lateinit var viewpager: ViewPager2
    lateinit var indicator : CircleIndicator3

    lateinit var imageView62 : ImageView

    var context2 : Context? = null
    private lateinit var username : TextView
    lateinit var skinage : TextView
    lateinit var skintype : TextView
    lateinit var skincolor : ImageView
    lateinit var colorcode : TextView
    lateinit var tscore : TextView
    lateinit var cheeckscore : TextView
    lateinit var chinscore : TextView
    lateinit var slsdscore : TextView
    lateinit var pfpzscore : TextView
    lateinit var hssdxscore : TextView
    lateinit var textView64 : TextView
    lateinit var imageView88 : ImageView

    lateinit var tprogress : ProgressBar
    lateinit var cheekprogress : ProgressBar
    lateinit var chinprogress : ProgressBar
    lateinit var slsdprgress : ProgressBar
    lateinit var pfpzprogress : ProgressBar
    lateinit var hssdxprogress : ProgressBar

    lateinit var tzone : ImageView
    lateinit var bol1 : ImageView
    lateinit var bol2 : ImageView
    lateinit var tuk : ImageView

    lateinit var skinAvg : TextView // ?????? ??????
    lateinit var textView69 : TextView // ??????
    lateinit var textView71 : TextView // ?????????
    lateinit var appCompatButton2 : Button


    lateinit var simgak : TextView
    lateinit var nabbem : TextView
    lateinit var yangho : TextView

    private lateinit var greendot : ImageView
    private lateinit var darkbluedot : ImageView
    private lateinit var yellowdot : ImageView
    private lateinit var purpledot : ImageView
    private lateinit var bluedot : ImageView
    private lateinit var crimsondot : ImageView
    private lateinit var reddot : ImageView

    lateinit var dotRecyclerView : RecyclerView
    lateinit var airecommendbutton : TextView
    lateinit var imageView90 : ImageView

    var avg = 0

    var simgakList : ArrayList<DotModel> = arrayListOf()
    var nabbemList : ArrayList<DotModel> = arrayListOf()
    var yanghoList : ArrayList<DotModel> = arrayListOf()

    private var skinDotAdapter : SkinDotAdapter? = null

    lateinit var appCompatButton : Button

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        imageView88 = view.findViewById(R.id.imageView88)
        imageView90 = view.findViewById(R.id.imageView90)
        appCompatButton = view.findViewById(R.id.appCompatButton)
        viewpager = view!!.findViewById(R.id.viewPager2)
        indicator = view.findViewById(R.id.indicator)
        imageView62 = view.findViewById(R.id.imageView62)
        dotRecyclerView = view.findViewById(R.id.dotInfoRecyclerview)
        skinAvg = view.findViewById(R.id.textView70)

        /** ?????? ?????? **/
        imageView90.setOnClickListener {
            mainActivity!!.fragmentChange("go_mypage")
        }

        /** AI ?????? ?????? **/
        airecommendbutton = view.findViewById(R.id.ai_recommend_button)
        airecommendbutton.text = "${App.prefs.getStringData(Constant.NAME)}????????? ?????? ?????? ??????"

        airecommendbutton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", 1)
            mainActivity!!.setFragment(bundle, ShoppingFragment())
        }

        /** ????????? **/
        textView64 = view.findViewById(R.id.textView64)
        textView64.setOnClickListener {
            val intent = Intent(context, MySkinResultListActivity::class.java)
            startActivityForResult(intent, 1007)
            activity!!.overridePendingTransition(R.xml.fade_in, R.xml.no_chagne)
        }

        /** ???????????? **/
        appCompatButton.setOnClickListener {
            val intent = Intent(context, MySkinResultListActivity::class.java)
            startActivityForResult(intent, 1007)
            activity!!.overridePendingTransition(R.xml.fade_in, R.xml.no_chagne)
        }

        /** ????????? ?????? **/
        darkbluedot = view.findViewById(R.id.dark_blue_dot)
        yellowdot = view.findViewById(R.id.yellow_dot)
        purpledot = view.findViewById(R.id.purple_dot)
        bluedot = view.findViewById(R.id.blue_dot)
        crimsondot = view.findViewById(R.id.crimson_dot)
        reddot = view.findViewById(R.id.red_dot)
        greendot = view.findViewById(R.id.green_dot)

        /** ??????, ?????? ?????? **/
        simgak = view.findViewById(R.id.simgak)
        simgak.setOnClickListener { buttonClick(simgak) }

        nabbem = view.findViewById(R.id.nabbem)
        nabbem.setOnClickListener { buttonClick(nabbem) }

        yangho = view.findViewById(R.id.yangho)
        yangho.setOnClickListener { buttonClick(yangho) }


        /** ??????, ??????, ??? **/
        tzone = view.findViewById(R.id.t_zone)
        bol1 = view.findViewById(R.id.bol1)
        bol2 = view.findViewById(R.id.bol2)
        tuk = view.findViewById(R.id.tuk)

        /** User data **/
        username = view.findViewById(R.id.user_name)
        skinage = view.findViewById(R.id.skin_age)
        skintype = view.findViewById(R.id.skin_type)
        skincolor = view.findViewById(R.id.skin_color)
        colorcode = view.findViewById(R.id.color_code)

        /** Progress bar **/
        tscore = view.findViewById(R.id.t_score)
        cheeckscore = view.findViewById(R.id.cheeck_score)
        chinscore = view.findViewById(R.id.chin_score)
        slsdscore = view.findViewById(R.id.slsd_score)
        pfpzscore = view.findViewById(R.id.pfpz_score)
        hssdxscore = view.findViewById(R.id.hssdx_score)


        tprogress = view.findViewById(R.id.t_progress)
        cheekprogress = view.findViewById(R.id.cheek_progress)
        chinprogress = view.findViewById(R.id.chin_progress)
        slsdprgress = view.findViewById(R.id.slsd_prgress)
        pfpzprogress = view.findViewById(R.id.pfpz_progress)
        hssdxprogress = view.findViewById(R.id.hssdx_progress)

        appCompatButton2 = view.findViewById(R.id.appCompatButton2)
        textView69 = view.findViewById(R.id.textView69)
        textView71 = view.findViewById(R.id.textView71)


        /** ??????????????? ?????? ?????? **/
        appCompatButton2.setOnClickListener {
            mainActivity!!.fragmentChange("go_mypage")
            CURRENT_SKIN_POSITION += 1
        }

//        if(!Constant.CEHCK){
//            testMansae(CURRENT_SKIN_POSITION)
//            getBanner()
//        } else {
//            Constant.CEHCK = false
//        }
        username.text = App.prefs.getStringData(Constant.NAME) // 1@1.com??? ?????? ??????
        getUser()

        return view
    }

    /** ????????? ?????? ?????????(??????) **/
    fun testMansae(reportPosition : Int){

        val dialog= ProgressDialog.progressDialog(context!!)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

        Retrofit_Mansae.server.getMansaeMeituReport()
            .enqueue(object : Callback<reportMeitu>{
                override fun onFailure(call: Call<reportMeitu>, t: Throwable) {

                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<reportMeitu>, response: Response<reportMeitu>) {
                    when(response.body()?.responseCode){
                        "SUCCESS" -> {
                            getBanner()
                            if(response.body()?.data != null) {
                                /** ????????? ????????? **/
                                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
                                val date = Date(response.body()!!.data[reportPosition].created_at.toLong()*1000)
                                textView64.text = "????????? "+ dateFormat.format(date)

                                avg = (response.body()!!.data[0].preview.slsd.score + response.body()!!.data[0].preview.pfpz.score + response.body()!!.data[0].preview.hssdx.score)/3 // ?????? ??????
                                skinAvg.text = (avg).toString()+"???" // ?????? ??????


                                /** ???????????? ?????? **/
                                // ???????????? ????????? ??????, ????????????????????? ??????????????????
                                if(response.body()!!.data.size == 1 || CURRENT_SKIN_POSITION+1 == response.body()!!.data.size){
                                    appCompatButton2.isVisible = false
                                    textView69.text = "-"
                                    textView71.text = "-" // ?????????
                                } else {
                                    appCompatButton2.isVisible = true
                                    val avg2 = (response.body()!!.data[reportPosition+1].preview.slsd.score + response.body()!!.data[reportPosition+1].preview.pfpz.score + response.body()!!.data[reportPosition+1].preview.hssdx.score)/3 // ????????????
                                    textView69.text = (avg2).toString()+"???" // ????????????
                                    val percent = ((avg - avg2) % (avg2)) * 1 // ????????????
                                    textView71.text = "$percent%"
                                }

                                App.prefs.putStringData(Constant.AGE, response.body()!!.data[reportPosition].preview.age.toString()) // ?????? ??????
                                App.prefs.putStringData(Constant.SKIN_TYPE, response.body()!!.data[reportPosition].preview.skin_type.toString()) // ?????? ?????? ??????
                                skinage.text = response.body()!!.data[reportPosition].preview.skin_age.toString()+"???" // ????????????

                                skinage.text = when(response.body()!!.data[reportPosition].preview.skin_age) {
                                    in 0..9 -> "10???"
                                    in 10..19 -> "20???"
                                    in 20..29 -> "20???"
                                    in 30..39 -> "30???"
                                    in 40..49 -> "40???"
                                    in 50..59 -> "50???"
                                    in 60..69 -> "60???"
                                    in 70..79 -> "70???"
                                    in 80..89 -> "80???"
                                    else -> "90??? ??????"
                                }

                                /** ????????? **/
                                /** ?????? **/
                                when (response.body()!!.data[reportPosition].pore.degree) {
                                    2 -> yanghoList.add(DotModel("??????", degree = 2))
                                    3 -> nabbemList.add(DotModel("??????", degree = 3))
                                    4 -> simgakList.add(DotModel("??????", degree = 4))
                                }

                                /** ???????????? **/
                                when (response.body()!!.data[reportPosition].blackhead.degree) {
                                    2 -> yanghoList.add(DotModel("????????????", degree = 2))
                                    3 -> nabbemList.add(DotModel("????????????", degree = 3))
                                    4 -> simgakList.add(DotModel("????????????", degree = 4))
                                }


                                when (response.body()!!.data[reportPosition].wrinkle.degree) {
                                    2 -> yanghoList.add(DotModel("??????", degree = 2))
                                    3 -> nabbemList.add(DotModel("??????", degree = 3))
                                    4 -> simgakList.add(DotModel("??????", degree = 4))
                                }

                                when (response.body()!!.data[reportPosition].speckle.degree) {
                                    2 -> yanghoList.add(DotModel("????????????", degree = 2))
                                    3 -> nabbemList.add(DotModel("????????????", degree = 3))
                                    4 -> simgakList.add(DotModel("????????????", degree = 4))
                                }

                                when (response.body()!!.data[reportPosition].acne.degree) {
                                    2 -> yanghoList.add(DotModel("?????????", degree = 2))
                                    3 -> nabbemList.add(DotModel("?????????", degree = 3))
                                    4 -> simgakList.add(DotModel("?????????", degree = 4))
                                }

                                when (response.body()!!.data[reportPosition].sensitive.degree) {
                                    1 -> yanghoList.add(DotModel("????????? ??????", degree = 1))
                                    2 -> nabbemList.add(DotModel("????????? ??????", degree = 2))
                                    3 -> simgakList.add(DotModel("????????? ??????", degree = 3))
                                }

                                when (response.body()!!.data[reportPosition].black_rim_of_eye.degree) {
                                    2 -> yanghoList.add(DotModel("????????????", degree = 2))
                                    3 -> nabbemList.add(DotModel("????????????", degree = 3))
                                    4 -> simgakList.add(DotModel("????????????", degree = 4))
                                }

                                /** ?????? ?????? **/
                                when(response.body()!!.data[reportPosition].preview.skin_type){
                                    1 -> skintype.text = "????????? ??????"
                                    2 -> skintype.text = "?????? ??????"
                                    3 -> skintype.text = "?????? ??????"
                                    4 -> skintype.text = "????????? ??????"
                                }

                                /** Skin color & code **/
                                skincolor.setBackgroundColor(Color.parseColor(response.body()!!.data[reportPosition].preview.color))
                                colorcode.text = response.body()!!.data[reportPosition].preview.color_code

                                /** Progress **/
                                colorSkin(response.body()!!.data[reportPosition].preview.t_score.toInt(), tzone)
                                colorSkin(response.body()!!.data[reportPosition].preview.cheek_score.toInt(), bol1)
                                colorSkin(response.body()!!.data[reportPosition].preview.cheek_score.toInt(), bol2)
                                colorSkin(response.body()!!.data[reportPosition].preview.chin_score.toInt(), tuk)

                                setPrgoress(response.body()!!.data[reportPosition].preview.t_score.toInt(), tscore, tprogress) // T???
                                setPrgoress(response.body()!!.data[reportPosition].preview.cheek_score.toInt(), cheeckscore, cheekprogress) // ?????????
                                setPrgoress(response.body()!!.data[reportPosition].preview.chin_score.toInt(), chinscore, chinprogress) // ???

                                setPrgoress(response.body()!!.data[reportPosition].preview.slsd.score, slsdscore, slsdprgress) // ????????????
                                setPrgoress(response.body()!!.data[reportPosition].preview.pfpz.score, pfpzscore, pfpzprogress) // ????????????
                                setPrgoress(response.body()!!.data[reportPosition].preview.hssdx.score, hssdxscore, hssdxprogress) // ?????????


                                /** ??? ?????? **/
                                simgak.text = "????????????${simgakList.size}"
                                nabbem.text = "????????????${nabbemList.size}"
                                yangho.text = "??????${yanghoList.size}"

                                skinDotHide()
                                skinDotShow(simgakList)

                                val dotAdapter = SkinDotAdapter(simgakList)
                                dotRecyclerView.apply {
                                    adapter = dotAdapter
                                    layoutManager = LinearLayoutManager(context)
                                }
                            } else {
                                textView64.isVisible = false // ?????????
                                appCompatButton.isVisible = false // ????????? ????????? ??????
                                imageView90.isVisible = false // ?????? ??????
                                airecommendbutton

                            }
                        }

                        else -> {
                            Toast.makeText(context2, "?????? ??????", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }
                    Toast.makeText(context2, "???????????? ????????????!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()

                    /** ??? ?????? **/
                    if(!Constant.POPUP_CHECK){
                        getPopup()
                        Constant.POPUP_CHECK = true
                    }
                }
            })
    }

    private fun getUser(){

        Retrofit_Mansae.server.getUserInfo()
            .enqueue(object : Callback<userInfo>{
                override fun onFailure(call: Call<userInfo>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call<userInfo>, response: Response<userInfo>) {
                    when(response.body()?.responseCode){
                        "SUCCESS" -> {
                            if(response.body()?.data?.recommendProductCode == null || response.body()?.data?.recommendProductCode == "") {
                                val intent = Intent(context!!, ReportActivity::class.java)
                                startActivity(intent)
                            } else {
                                App.prefs.putStringData(Constant.PHONE, response.body()?.data!!.contact.toString()) // ????????? ??????
                                App.prefs.putStringData(Constant.SKIN_TYPE, response.body()?.data!!.skinType.toString()) // ????????????
                                App.prefs.putStringData(Constant.BIRTH, response.body()?.data!!.birthDate)
                                if(!Constant.CEHCK || Constant.meituCheck){
                                    testMansae(CURRENT_SKIN_POSITION)
                                } else {
                                    Constant.CEHCK = false
                                }
                                when(response.body()?.data?.gender){
                                    "MAN" -> {
                                        App.prefs.putStringData(Constant.GENDER, "MAN")
                                        imageView88.setBackgroundResource(R.drawable.dot_man)
                                        context?.let { Glide.with(it).load(R.drawable.my_face_male).into(imageView62) }
                                    }
                                    else -> {
                                        App.prefs.putStringData(Constant.GENDER, "WOMAN")
                                        imageView88.setBackgroundResource(R.drawable.total_state)
                                        context?.let { Glide.with(it).load(R.drawable.my_face).into(imageView62) }
                                    }
                                }
                            }
                        }
                        else -> {

                        }
                    }
                }
            })
    }

    /** ?????? **/
    fun getBanner(){
        Retrofit_Mansae.server.getBanner()
            .enqueue(object : Callback<banner>{
                override fun onFailure(call: Call<banner>, t: Throwable) {

                }

                override fun onResponse(call: Call<banner>, response: Response<banner>) {

                    when(response.body()?.responseCode){
                        "SUCCESS" -> {
                            val imgList : ArrayList<Banner> = arrayListOf()
                            for(i in response.body()!!.data){
                                if(i.positionType == "UPPER"){
                                    imgList.add(
                                            Banner(
                                                    i.imageUrl, i.linkUrl, i.id, i.product
                                            )
                                    )
                                }
                            }

                            val homeAdapter = HomeAdapter(imgList)
                            viewpager.adapter = homeAdapter
                            viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

                            indicator.setViewPager(viewpager)
                            indicator.createIndicators(imgList.size, 0)
                        }
                        else -> {

                    }
                    }
                }
            })
    }

    fun getPopup(){
        Retrofit_Mansae.server.getPopup()
            .enqueue(object : Callback<popup>{
                override fun onFailure(call: Call<popup>, t: Throwable) {

                }

                override fun onResponse(call: Call<popup>, response: Response<popup>) {
                    when(response.body()?.responseCode){
                        "SUCCESS" -> {
                            if(response.body()?.data?.size != 0){
                                val intent = Intent(context, PopupActivity::class.java)

                                for(i in 0 until response.body()?.data!!.size){
                                    intent.putExtra("pos", i)
                                    startActivity(intent)
                                    activity!!.overridePendingTransition(R.xml.fade_in, R.xml.no_chagne)
                                }
                            }
                        }

                        else -> {

                        }
                    }
                }
            })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setPrgoress(score : Int, score_text : TextView, progressBar : ProgressBar){
        when(score){
            in 0..33 -> {
                score_text.setTextColor(Color.parseColor("#F05E49"))
                score_text.text = score.toString()
                progressBar.progressDrawable = context2!!.getDrawable(R.drawable.progress_red)
                progressBar.progress = score
            }
            in 34..59 -> {
                score_text.setTextColor(Color.parseColor("#F6AC1D"))
                score_text.text = score.toString()
                progressBar.progressDrawable = context2!!.getDrawable(R.drawable.progress_yellow)
                progressBar.progress = score
            }
            in 60..100 -> {
                score_text.setTextColor(Color.parseColor("#2DBCDE"))
                score_text.text = score.toString()
                progressBar.progressDrawable = context2!!.resources.getDrawable(R.drawable.progress_blue)
                progressBar.progress = score
            }
        }
    }

    fun colorSkin(score : Int, img : ImageView){
        when(score){
            in 0..33 -> {
                img.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F05E49"))
            }
            in 34..59 -> {
                img.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F6AC1D"))
            }
            in 60..100 -> {
                img.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2DBCDE"))
            }
        }
    }

    private fun buttonClick(text : TextView){

        buttonColor()
        text.apply {
            backgroundTintList = ColorStateList.valueOf(Color.parseColor("#059899"))
            setTextColor(Color.parseColor("#ffffff"))
        }

        when(text.text.toString()){
            "????????????${simgakList.size}" -> {
                skinDotHide()
                skinDotAdapter = SkinDotAdapter(simgakList)
                dotRecyclerView.apply {
                    adapter = skinDotAdapter
                    layoutManager = LinearLayoutManager(context)
                }
                skinDotShow(simgakList)
            }

            "????????????${nabbemList.size}" -> {
                skinDotHide()
                skinDotAdapter = SkinDotAdapter(nabbemList)
                dotRecyclerView.apply {
                    adapter = skinDotAdapter
                    layoutManager = LinearLayoutManager(context)
                }
                skinDotShow(nabbemList)
            }

            "??????${yanghoList.size}" -> {
                skinDotHide()
                skinDotAdapter = SkinDotAdapter(yanghoList)
                dotRecyclerView.apply {
                    adapter = skinDotAdapter
                    layoutManager = LinearLayoutManager(context)
                }
                skinDotShow(yanghoList)
            }
        }
    }

    private fun buttonColor(){
        simgak.apply {
            backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F2F5F5"))
            setTextColor(Color.parseColor("#777777"))
        }
        yangho.apply {
            backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F2F5F5"))
            setTextColor(Color.parseColor("#777777"))
        }
        nabbem.apply {
            backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F2F5F5"))
            setTextColor(Color.parseColor("#777777"))
        }
    }

    fun skinDotHide(){
        darkbluedot.isInvisible = true
        yellowdot.isInvisible = true
        purpledot.isInvisible = true
        bluedot.isInvisible = true
        crimsondot.isInvisible = true
        reddot.isInvisible = true
        greendot.isInvisible = true
    }

    fun skinDotShow(list : ArrayList<DotModel>){
        for(i in 0 until list.size){
            when(list[i].result){
                "??????" -> {
                    greendot.isInvisible = false
                }

                "????????????" -> {
                    reddot.isInvisible = false
                }

                "??????" -> {
                    darkbluedot.isInvisible = false
                }

                "????????????" -> {
                    purpledot.isInvisible = false
                }

                "?????????" -> {
                    yellowdot.isInvisible = false
                }

                "????????? ??????" -> {
                    crimsondot.isInvisible = false
                }

                "????????????" -> {
                    bluedot.isInvisible = false
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when (requestCode) {
                1007 -> {
                    simgakList.clear()
                    yanghoList.clear()
                    nabbemList.clear()
                    CURRENT_SKIN_POSITION = data!!.getIntExtra("position", 0)
                    testMansae(CURRENT_SKIN_POSITION)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
        this.context2 = context.applicationContext
    }

    override fun onDetach() {
        super.onDetach()
        simgakList.clear()
        yanghoList.clear()
        nabbemList.clear()
        mainActivity = null
    }
}