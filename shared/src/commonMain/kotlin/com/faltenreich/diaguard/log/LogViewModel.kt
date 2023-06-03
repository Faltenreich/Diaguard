package com.faltenreich.diaguard.log

import app.cash.paging.Pager
import app.cash.paging.PagingData
import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.log.usecase.LogItemPagingSource
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.cachedIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LogViewModel(
    initialDate: Date,
    private val dispatcher: CoroutineDispatcher = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
) : ViewModel() {

    val currentDate = MutableStateFlow(initialDate)
    private val pagination = MutableStateFlow(
        LogPaginationState(
            minimumDate = initialDate.minusMonths(1),
            maximumDate = initialDate.plusMonths(1),
        )
    )
    val items: Flow<PagingData<LogItem>> = Pager(
        config = LogItemPagingSource.newConfig(),
        initialKey = initialDate,
        pagingSourceFactory = { LogItemPagingSource() },
    ).flow.cachedIn(viewModelScope)
    private val state = combine(pagination, items) { pagination, items ->
        LogViewState(
            items = items,
            scrollPosition = null, // TODO: pagination.targetDate?.let { date -> items.indexOfFirst { it.date == date } }
        )
    }.flowOn(dispatcher)
    val viewState: StateFlow<LogViewState> = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = LogViewState(PagingData.empty<LogItem>()),
    )

    fun setDate(date: Date) = viewModelScope.launch(dispatcher) {
        val indexOfDate = -1 // TODO: items.first().indexOfFirst { it.date == date }
        if (indexOfDate >= 0) {
            pagination.value = pagination.value.copy(targetDate = date)
        } else {
            val startDate = date.minusMonths(1)
            val endDate = date.plusMonths(1)
            println("LogViewModel: Start date is $startDate, end date is $endDate")
            pagination.value = pagination.value.copy(
                minimumDate = startDate,
                maximumDate = endDate,
                targetDate = date,
            )
        }
    }

    fun remove(item: LogItem.EntryContent) = viewModelScope.launch(dispatcher) {
        deleteEntry(item.entry.id)
    }
}