/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package com.neptuneg.autogen.model

import com.neptuneg.autogen.model.Comment

import com.squareup.moshi.Json

/**
 * 
 *
 * @param comments 
 */


data class GetArticleComments200Response (

    @Json(name = "comments")
    val comments: kotlin.collections.List<Comment>

)
