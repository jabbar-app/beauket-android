package com.healstationlab.design.dto

data class test(
    val responseCode: String,
    val message: Any,
    val `data`: Data
) {
    data class Data(
        val id: Int,
        val merchant: Any,
        val category: Category,
        val name: String,
        val advertisement: Boolean,
        val advertisementTypes: List<Any>?,
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
        val skinType: List<Any>,
        val description: Any,
        val shippingPrice: Int,
        val recommendAgeStart: Any,
        val recommendAgeEnd: Any,
        val imageUrl: Any,
        val details: Any,
        val hidden: Boolean,
        val rating: Double,
        val options: List<Option>,
        val codes: List<String>,
        val ratingCountMap: RatingCountMap,
        val ingredientsStr: Any,
        val hashTagsStr: Any,
        val category1st: Any,
        val category2nd: Any,
        val category3rd: Any,
        val improvementIdList: Any,
        val ingredientIdList: Any,
        val hashTagIdList: Any
    ) {
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

        data class RatingCountMap(
            val `1`: Int,
            val `2`: Int,
            val `3`: Int,
            val `4`: Int,
            val `5`: Int
        )
    }
}