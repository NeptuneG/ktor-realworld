package com.neptuneg.adaptor.database.gateway.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object FollowingsTable : IntIdTable("followings") {
    val followerId = uuid(name = "follower_id")
    val followingUserId = uuid(name = "following_user_id")
}
