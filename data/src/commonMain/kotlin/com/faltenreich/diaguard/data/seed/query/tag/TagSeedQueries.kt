package com.faltenreich.diaguard.data.seed.query.tag

import com.faltenreich.diaguard.data.seed.query.SeedQueries
import com.faltenreich.diaguard.data.tag.Tag
import com.faltenreich.diaguard.localization.Localization
import com.faltenreich.diaguard.resource.Res
import com.faltenreich.diaguard.resource.tags_seed

class TagSeedQueries(
    private val localization: Localization,
) : SeedQueries<Tag.Seed> {

    override fun getAll(): List<Tag.Seed> {
        return localization.getStringArray(Res.array.tags_seed).map { tag ->
            Tag.Seed(name = tag)
        }
    }
}