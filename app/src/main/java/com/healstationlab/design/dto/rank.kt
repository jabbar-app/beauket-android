package com.healstationlab.design.dto

data class rank(
    val responseCode: String,
    val message: Any,
    val `data`: List<Data>,
    val totalCount: Int,
    val totalPages: Int,
    val currentPage: Int
) {
    data class Data(
            val id: Int,
            val merchant: Merchant,
            val category: Category,
            val name: String,
            val advertisement: Boolean,
            val advertisementTypes: Any?,
            val skinProblems: List<Any>,
            val brand: String,
            val dosageForm: Any,
            val cosmeticIngredients: Any,
            val code: String,
            val hashTags: List<Any>,
            val dose: String,
            val features: List<Any>,
            val recommendedIngredients: List<RecommendedIngredient>,
            val improvements: List<Improvement>,
            val effects: String,
            val skinType: List<String>,
            val description: Any,
            val shippingPrice: Int,
            val recommendAgeStart: Any,
            val recommendAgeEnd: Any,
            val imageUrl: String,
            val details: Any,
            val hidden: Boolean,
            val rating: Double,
            val options: List<Option>,
            val codes: List<String>,
            val cosSimilarity : recommend.Cos?,
            val ratingCountMap: Any,
            val ingredientsStr: Any,
            val hashTagsStr: Any,
            val category1st: Any,
            val category2nd: Any,
            val category3rd: Any,
            val improvementIdList: Any,
            val ingredientIdList: Any,
            val hashTagIdList: Any
    ) {
        data class Merchant(
            val id: Int,
            val name: String,
            val businessNumber: String,
            val contact: String,
            val zoneCode: String,
            val address: String,
            val addressDetail: String,
            val email: Any,
            val bizCertFileUrl: String,
            val smbCertFileUrl: String,
            val bankbookFileUrl: String,
            val plInsCertFileUrl: String,
            val bankName: String,
            val bankAccount: String,
            val representativeName: Any,
            val representativeCellphone: Any,
            val representativeEmail: Any,
            val saleName: Any,
            val saleCellphone: Any,
            val saleEmail: Any,
            val balanceName: Any,
            val balanceCellphone: Any,
            val balanceEmail: Any,
            val productName: Any,
            val productCategory: Any,
            val productPrice: Any,
            val productTaxType: Any,
            val productProductionType: Any,
            val productBrandType: Any,
            val productInfoFileUrl: Any,
            val marketUrl: Any,
            val license: Any,
            val makingDocFileUrl: Any,
            val licenseDocFileUrl: Any,
            val foodQualityDocFileUrl: Any,
            val bizReportDocFileUrl: Any,
            val cosmeticCertDocFileUrl: Any,
            val inspectDocFileUrl: Any,
            val loginId: Any,
            val commissionRate: Double,
            val registered: Boolean,
            val openDateFormatted: Any
        )

        data class Category(
            val id: Int,
            val name: String,
            val iconImageUrl: Any,
            val level: Int,
            val parent: Parent
        ) {
            data class Parent(
                val id: Int,
                val name: String,
                val iconImageUrl: Any,
                val level: Int,
                val parent: Parent
            ) {
                data class Parent(
                    val id: Int,
                    val name: String,
                    val iconImageUrl: String,
                    val level: Int,
                    val parent: Any
                )
            }
        }

        data class RecommendedIngredient(
            val id: Int,
            val name: String,
            val hashTags: List<Any>,
            val hashTagIdList: Any
        )

        data class Improvement(
            val id: Int,
            val name: String,
            val createdAt: List<Int>,
            val updatedAt: List<Int>
        )

        data class Option(
            val id: Int,
            val name: String,
            val price: Int,
            val originalPrice: Int,
            val stocks: Int
        )
    }
}