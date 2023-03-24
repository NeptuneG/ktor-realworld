package com.neptuneg.adaptor.keycloak.gateway

import com.neptuneg.domain.entity.serializer.Serializer
import com.squareup.moshi.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.UUID

internal data class OAuthUserInfo(
    val sub: UUID,
    val email: String,
    @Json(name = "email_verified")
    val emailVerified: Boolean,
    @Json(name = "preferred_username")
    val preferredUsername: String,
    val bio: String?,
    val image: String?,
)

internal object OAuthClient {
    val client: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }

    fun getUserInfo(userInfoUrl: String, accessToken: String): Result<OAuthUserInfo> {
        return runCatching {
            val request = Request.Builder().url(userInfoUrl).get()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
            client.newCall(request).execute().let { response ->
                if (!response.isSuccessful) throw Exception(response.message)

                response.body?.let { Serializer.moshi.adapter(OAuthUserInfo::class.java).fromJson(it.source()) }
            }!!
        }
    }
}
