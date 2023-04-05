package com.neptuneg.adaptor.database.gateway.repository

import com.neptuneg.adaptor.database.gateway.extension.runTxCatching
import com.neptuneg.adaptor.database.gateway.table.TagsTable
import com.neptuneg.domain.entity.Tag
import com.neptuneg.domain.logic.TagRepository
import org.jetbrains.exposed.sql.selectAll

class TagRepositoryImpl : TagRepository {
    override fun getTags(): Result<List<Tag>> {
        return runTxCatching {
            TagsTable
                .slice(TagsTable.value)
                .selectAll()
                .map { Tag(it[TagsTable.value]) }
        }
    }
}
