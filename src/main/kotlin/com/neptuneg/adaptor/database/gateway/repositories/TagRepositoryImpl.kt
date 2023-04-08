package com.neptuneg.adaptor.database.gateway.repositories

import com.neptuneg.adaptor.database.gateway.extensions.runTxCatching
import com.neptuneg.adaptor.database.gateway.tables.TagsTable
import com.neptuneg.domain.entities.Tag
import com.neptuneg.domain.logics.TagRepository
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
