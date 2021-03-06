@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)

package com.healstationlab.design.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.healstationlab.design.R
import com.healstationlab.design.adapter.DetailOrder2Adapter
import com.healstationlab.design.databinding.ActivityOrderBinding
import com.healstationlab.design.dto.auth
import com.healstationlab.design.dto.myOrderDTO
import com.healstationlab.design.dto.paymentCancelDTO
import com.healstationlab.design.model.ShippingMent
import com.healstationlab.design.model.myOrderModel
import com.healstationlab.design.resource.Retrofit_Mansae
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class OrderActivity : AppCompatActivity() {
    lateinit var binding : ActivityOrderBinding
    var orderId : Int = 0
    var delivery : Long = 0
    var paymentFee : Long = 0
    var total = 0
    var position = 0
    var myOrderList : ArrayList<myOrderModel> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOrderBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        position = intent.getIntExtra("position", 0)
        orderId = intent.getIntExtra("id", 0)
        binding.backButton.setOnClickListener { onBackPressed() }
        getMyOrder()

        binding.textView400.setOnClickListener {
            val body : HashMap<String, String> = HashMap()
            body["hidden"] = "true"
            putHidden(orderId, body)
        }

        /** ?????? ?????? ?????? **/
        binding.textView418.setOnClickListener {
            var isBool = true
            for(i in 0 until myOrderList.size){
                if(myOrderList[i].status != "ACCEPT"){
                    isBool = false
                }
            }
            if(isBool){
                postCancel(orderId)
            } else {
                val intent = Intent(this@OrderActivity, sevenDaysActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.xml.fade_in, R.xml.no_chagne)
            }
        }
    }

    override fun onBackPressed() {
        when(intent.getBooleanExtra("check", false)){
            true -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.xml.slide_left, R.xml.no_chagne)
                finish()

            }
            false -> {
                super.onBackPressed()
                overridePendingTransition(0, R.xml.slide_right)
            }
        }

    }

    fun getMyOrder(){
        Retrofit_Mansae.server.getMyOrder()
                .enqueue(object : Callback<myOrderDTO>{
                    override fun onFailure(call: Call<myOrderDTO>, t: Throwable) {
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call<myOrderDTO>, response: Response<myOrderDTO>) {
                        when(response.body()?.responseCode){
                            "SUCCESS" -> {
                                myOrderList.clear()
                                delivery = 0
                                paymentFee = 0

                                /** ???????????? **/ // ?????????
                                when(response.body()!!.data[1].ProductOrderMeta[position].paymentType){
                                    "CREDIT_CARD" -> {binding.textView156.text = "????????????"}
                                    "TRANSFER" -> {binding.textView156.text = "????????? ????????????"}
                                }

                                /** ????????? **/
                                binding.textView164.text = response.body()!!.data[1].ProductOrderMeta[position].receiverName

                                /** ????????? **/
                                binding.textView167.text = response.body()!!.data[1].ProductOrderMeta[position].contact

                                /** ?????? **/
                                binding.textView186.text = "(${response.body()!!.data[1].ProductOrderMeta[0].zoneCode}) ${response.body()!!.data[1].ProductOrderMeta[position].address} ${response.body()!!.data[1].ProductOrderMeta[position].addressDetail}"

                                /** ???????????? **/
                                binding.textView189.text = response.body()!!.data[1].ProductOrderMeta[position].deliveryInstruction


                                var num = 0
                                for(i in response.body()!!.data[1].ProductOrderMeta){
                                    if(i.id == orderId){
                                        /** ??? ?????? ?????? **/ // ??????????????????
                                        binding.textView146.text = sliceAmountNumber(i.totalPrice.toLong() + i.couponDiscountPrice + i.usePoint)+" ???"

                                        /** ???????????? **/
                                        binding.textView79.text = "???????????? ${intent.getStringExtra("orderNo")}"

                                        val split = response.body()!!.data[1].ProductOrderMeta[position].createdDate.split("-")
                                        binding.textView72.text = "${split[0]}.${split[1]}.${split[2]} ??????"


                                        for(j in response.body()!!.data[1].ProductOrderMeta[num].orderItems){
                                            if(j.deliveryCount){
                                                delivery += j.deliveryFee
                                            }
                                            paymentFee += j.paymentFee
                                            myOrderList.add(
                                                    myOrderModel(
                                                            orderItemsId = j.id,
                                                            deliveryFee = j.deliveryFee,
                                                            pgTid = i.pgTid,
                                                            paymentType = i.paymentType,
                                                            status = j.status,
                                                            merchantName = j.product.merchant.name,
                                                            name = j.product.name,
                                                            brand = j.product.brand,
                                                            imageUrl = j.product.imageUrl,
                                                            amount = j.amount,
                                                            totalPrice = j.totalPrice / j.amount,
                                                            productId = j.product.id,
                                                            productOrderId = j.id,
                                                            productOptionId = j.productOption.id,
                                                            invoiceUrl = j.invoiceUrl,
                                                            update = j.product.updatedAt
                                                    )
                                            )
                                        }
                                        /** ?????? ?????? ?????? **/
                                        binding.textView154.text = "${sliceAmountNumber(delivery)} ???"
                                        binding.textView159.text =
                                            sliceAmountNumber(paymentFee.minus(i.usePoint).minus(i.couponDiscountPrice))

                                        break
                                    } else {
                                        num++
                                    }
                                }
                                val detailOrder = DetailOrder2Adapter(myOrderList)
                                binding.detailOrderRecyclerview.apply {
                                    adapter = detailOrder
                                    layoutManager = LinearLayoutManager(this@OrderActivity)
                                }

                                detailOrder.setItemClickListner(object : DetailOrder2Adapter.ItemClickListener{
                                    override fun onCart(view: View, position: Int) {
                                        val body : HashMap<String, String> = HashMap()
                                        body["count"] = myOrderList[position].amount.toString()
                                        addCart(id = myOrderList[position].productOptionId, body = body)
                                    }

                                    @SuppressLint("SimpleDateFormat")
                                    override fun onLeft(view: View, text: String, position: Int) {
                                        when(text){
                                            "????????????" -> {
                                                val alertDialog = AlertDialog.Builder(this@OrderActivity)
                                                    .setTitle("MANSAE")
                                                    .setMessage("????????? ?????? ???????????????????")
                                                    .setPositiveButton("??????"){ _: DialogInterface, _: Int ->
                                                        paymentCancel(
                                                                paymethod = if(myOrderList[position].paymentType == "CREDIT_CARD"){ "Card" } else { "Acct" },
                                                                tid = myOrderList[position].pgTid.toString(),
                                                                price = myOrderList[position].totalPrice.toString().replace("", "").toInt(),
                                                                confirmPrice = binding.textView159.text.toString().replace(",", "").toInt().minus(myOrderList[position].totalPrice.toString().replace(",","").toInt() + myOrderList[position].deliveryFee.toString().replace(",","").toInt()),
                                                                position = position
                                                        )
                                                    }
                                                    .setNegativeButton("??????"){ _: DialogInterface, _: Int ->
                                                    }.create()
                                                alertDialog.show()
                                            }
                                            "??????/??????" -> {
                                                val alertDialog = AlertDialog.Builder(this@OrderActivity)
                                                    .setTitle("MANSAE")
                                                    .setMessage("?????? ??? ??????????????? ???????????????????")
                                                    .setPositiveButton("??????"){ _: DialogInterface, _: Int ->
                                                        val split = myOrderList[position].update
                                                        val update = "${split[0]}-${split[1]}-${split[2]}"
                                                        val format = SimpleDateFormat("yyyy-MM-dd")

                                                        val cal = Calendar.getInstance() // 34
                                                        cal.time = format.parse(update)
                                                        cal.add(Calendar.DATE, 7)


                                                        val df1 = format.parse(format.format(cal.time)) // 7??? ?????????
                                                        val today = Date()

                                                        when(df1.compareTo(today)){
                                                            1, 0 -> {
                                                                val body : HashMap<String, String> = HashMap()
                                                                body["status"] = "REFUND"
                                                                statusChange(
                                                                    myOrderList[position].productOrderId,
                                                                    body
                                                                )
                                                            }
                                                            -1 -> {
                                                                val intent = Intent(this@OrderActivity, sevenDaysActivity::class.java)
                                                                startActivity(intent)
                                                                overridePendingTransition(R.xml.fade_in, R.xml.no_chagne)
                                                            }
                                                        }
                                                    }
                                                    .setNegativeButton("??????"){ _: DialogInterface, _: Int ->
                                                    }.create()
                                                alertDialog.show()
                                            }
                                            "????????????" -> {
                                                // 7????????? ?????? ??? ??????
                                            }
                                        }
                                    }

                                    override fun onRight(view: View, text: String, position: Int) {
                                        if(myOrderList[position].invoiceUrl != null && myOrderList[position].invoiceUrl != ""){
                                            val intent = Intent(Intent.ACTION_VIEW)
                                            intent.data = Uri.parse(myOrderList[position].invoiceUrl)
                                            startActivity(intent)
                                        }
                                    }

                                    override fun onReview(view: View, position: Int) {
                                        val intent = Intent(this@OrderActivity, WriteReviewActivity::class.java)
                                        intent.putExtra("img", myOrderList[position].imageUrl)
                                        intent.putExtra("name", myOrderList[position].name)
                                        intent.putExtra("price", myOrderList[position].totalPrice.toString())
                                        intent.putExtra("brand", myOrderList[position].brand)
                                        intent.putExtra("id", myOrderList[position].orderItemsId)
                                        startActivity(intent)
                                    }

                                    override fun onClick(view: View, position: Int) {
                                        val intent = Intent(this@OrderActivity, ProductDetailActivity::class.java)
                                        intent.putExtra("id", myOrderList[position].productId)
                                        startActivity(intent)
                                    }

                                    override fun onCenter(view: View, text: String, position: Int) {
                                        when(text){
                                            "????????????" -> {
                                                if(myOrderList[position].invoiceUrl != null && myOrderList[position].invoiceUrl != ""){
                                                    val intent = Intent(Intent.ACTION_VIEW)
                                                    intent.data = Uri.parse(myOrderList[position].invoiceUrl)
                                                    startActivity(intent)
                                                }
                                            }

                                            "????????????" -> {
                                                val shipment : ArrayList<ShippingMent> = arrayListOf()
                                                for(i in 0 until myOrderList.size){
                                                    shipment.add(
                                                            ShippingMent(myOrderList[i].merchantName, myOrderList[i].deliveryFee.toInt())
                                                    )
                                                }

                                                val hash : Hashtable<String, Int> = Hashtable()

                                                for(i in 0 until shipment.size) {
                                                    if(!hash.containsKey(shipment[i].merchant)){
                                                        hash["${shipment[i].merchant}"] =
                                                            shipment[i].shippingMent
                                                    } else {
                                                        if( hash["${shipment[i].merchant}"]!! < shipment[i].shippingMent!!){
                                                            hash["${shipment[i].merchant}"] =
                                                                shipment[i].shippingMent
                                                        }
                                                    }
                                                }

                                                val merchant = myOrderList[position].merchantName // ??????

                                                var confirmPrice = 0
                                                var size = 0
                                                val alertDialog = AlertDialog.Builder(this@OrderActivity)
                                                        .setTitle("MANSAE")
                                                        .setMessage("????????? ?????? ???????????????????")
                                                        .setPositiveButton("??????"){ _: DialogInterface, _: Int ->
                                                            for(i in 0 until myOrderList.size){
                                                                if(myOrderList[i].status != "CANCELLED"){
                                                                    confirmPrice += myOrderList[i].totalPrice
                                                                    // confirmPrice += myOrderList[i].deliveryFee.toInt()
                                                                    if(merchant == myOrderList[i].merchantName){
                                                                        size++
                                                                    }
                                                                }
                                                            }

                                                            // ???????????? ????????? ??????
                                                            if(size == 1){
                                                                val merchantFee = hash[merchant]
                                                                confirmPrice += merchantFee!!
                                                                total += myOrderList[position].totalPrice.toString().replace(",","").toInt() + merchantFee
                                                                paymentCancel(
                                                                        paymethod = if(myOrderList[position].paymentType == "CREDIT_CARD"){ "Card" } else { "Acct" },
                                                                        tid = myOrderList[position].pgTid.toString(),
                                                                        price = myOrderList[position].totalPrice.toString().replace("", "").toInt().plus(
                                                                            merchantFee
                                                                        ),
                                                                        confirmPrice = confirmPrice.minus(total),
                                                                        position = position
                                                                )
                                                            } else {
                                                                total += myOrderList[position].totalPrice.toString().replace(",","").toInt()
                                                                paymentCancel(
                                                                        paymethod = if(myOrderList[position].paymentType == "CREDIT_CARD"){ "Card" } else { "Acct" },
                                                                        tid = myOrderList[position].pgTid.toString(),
                                                                        price = myOrderList[position].totalPrice.toString().replace("", "").toInt(),
                                                                        confirmPrice = confirmPrice.minus(total),
                                                                        position = position
                                                                )
                                                            }
                                                        }
                                                        .setNegativeButton("??????"){ _: DialogInterface, _: Int ->
                                                        }.create()
                                                alertDialog.show()
                                            }
                                        }
                                    }
                                })
                            }
                            else -> {
                                Toast.makeText(this@OrderActivity, "????????????", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
    }

    fun statusChange(id: Int, body: HashMap<String, String>){
        Retrofit_Mansae.server.putStatus(id = id, body = body)
                .enqueue(object : Callback<auth>{
                    override fun onFailure(call: Call<auth>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<auth>, response: Response<auth>) {

                        when(response.body()?.responseCode){
                            "SUCCESS" -> {

                            }
                            else -> {
                                Toast.makeText(this@OrderActivity, "????????????", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })

    }

    fun addCart(id : Int, body : HashMap<String, String>){
        Retrofit_Mansae.server.postAddCard(
                id =  id,
                body = body
        ).enqueue(object : Callback<auth>{
            override fun onFailure(call: Call<auth>, t: Throwable) {

            }

            override fun onResponse(call: Call<auth>, response: Response<auth>) {
                when(response.body()!!.responseCode){
                    "SUCCESS" -> {
                        Toast.makeText(this@OrderActivity, "??????????????? ?????????????????????!", Toast.LENGTH_SHORT).show()
                    }

                    else -> {

                    }
                }
            }
        })
    }

    private fun putHidden(id : Int, body: HashMap<String, String>){
        Retrofit_Mansae.server.putHidden(id = id, body = body)
            .enqueue(object : Callback<auth>{
                override fun onFailure(call: Call<auth>, t: Throwable) {

                }

                override fun onResponse(call: Call<auth>, response: Response<auth>) {
                    when(response.body()?.responseCode){
                        "SUCCESS" -> {
                            Toast.makeText(this@OrderActivity, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                            setResult(Activity.RESULT_OK)
                            finish()
                            overridePendingTransition(R.xml.slide_left, R.xml.no_chagne)
                        }

                        else -> {

                        }
                    }
                }
            })
    }

    fun paymentCancel(paymethod : String, tid : String, price : Int, confirmPrice : Int, position : Int){
        Retrofit_Mansae.server.paymentCancel(
                paymethod = paymethod, tid = tid, price = price, confirmPrice = confirmPrice
        )
                .enqueue(object : Callback<paymentCancelDTO>{
                    override fun onFailure(call: Call<paymentCancelDTO>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<paymentCancelDTO>, response: Response<paymentCancelDTO>) {
                        when(response.body()?.responseCode){
                            "SUCCESS" -> {
                                when(response.body()!!.data!!.resultCode){
                                    "00" -> {
                                        // ??????
                                        val body : HashMap<String, String> = HashMap()
                                        body["status"] = "CANCELLED"
                                        statusChange(myOrderList[position].productOrderId, body)
                                    }

                                    else -> {
                                        Toast.makeText(this@OrderActivity, "${response.body()?.data?.resultMsg}??? ?????? ???????????? ??????", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                })
    }

    private fun postCancel(id : Int){
        Retrofit_Mansae.server.postCancel(id = id)
            .enqueue(object : Callback<paymentCancelDTO>{

                override fun onFailure(call: Call<paymentCancelDTO>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call<paymentCancelDTO>, response: Response<paymentCancelDTO>) {
                    when(response.body()?.responseCode){
                        "SUCCESS" -> {
                            val body : HashMap<String, String> = HashMap()
                            body["status"] = "CANCELLED"
                            var count = 0
                            for(i in 0 until myOrderList.size){
                                ++count
                                statusChange(myOrderList[i].productOrderId, body)
                            }

                            if(count == myOrderList.size){
                                getMyOrder()
                                Toast.makeText(this@OrderActivity, "?????? ?????? ???????????????.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            })
    }

    fun sliceAmountNumber(number : Long) : String {
        val decimalFormat = DecimalFormat("###,###")
        return decimalFormat.format(number)
    }
}