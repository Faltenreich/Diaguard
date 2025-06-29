package com.faltenreich.diaguard.entry.search

import androidx.paging.PagingData
import com.faltenreich.diaguard.entry.list.EntryListItemState
import kotlinx.coroutines.flow.Flow

data class EntrySearchState(
    val query: String,
    val pagingData: Flow<PagingData<EntryListItemState>>,
)