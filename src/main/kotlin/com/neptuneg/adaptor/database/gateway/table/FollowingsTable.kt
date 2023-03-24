package com.neptuneg.adaptor.database.gateway.table

import org.jetbrains.exposed.dao.id.IntIdTable

object FollowingsTable : IntIdTable("followings") {
    val followerId = uuid(name = "follower_id")
    val followeeId = uuid(name = "followee_id")
}
