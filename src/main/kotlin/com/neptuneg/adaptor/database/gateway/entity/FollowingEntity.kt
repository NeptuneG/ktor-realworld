package com.neptuneg.adaptor.database.gateway.entity

import com.neptuneg.adaptor.database.gateway.table.FollowingsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FollowingEntity(id: EntityID<Int>) : IntEntity(id) {
    var followerId by FollowingsTable.followerId
    var followeeId by FollowingsTable.followeeId

    companion object : IntEntityClass<FollowingEntity>(FollowingsTable)
}