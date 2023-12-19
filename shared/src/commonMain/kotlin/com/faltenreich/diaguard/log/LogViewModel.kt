package com.faltenreich.diaguard.log

import app.cash.paging.Pager
import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import com.faltenreich.diaguard.entry.form.DeleteEntryUseCase
import com.faltenreich.diaguard.log.item.LogItem
import com.faltenreich.diaguard.navigation.NavigateToUseCase
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.datetime.Date
import com.faltenreich.diaguard.shared.datetime.DateTimeFactory
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.view.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class LogViewModel(
    date: Date?,
    dateTimeFactory: DateTimeFactory = inject(),
    private val navigateTo: NavigateToUseCase = inject(),
    private val deleteEntry: DeleteEntryUseCase = inject(),
) : ViewModel<PagingData<LogItem>, LogIntent>() {

    private val initialDate: Date = date ?: dateTimeFactory.today()
    private lateinit var dataSource: PagingSource<Date, LogItem>
    val currentDate = MutableStateFlow(initialDate)
    
    override val state: Flow<PagingData<LogItem>> = Pager(
        config = LogItemSource.newConfig(),
        initialKey = initialDate,
        pagingSourceFactory = { LogItemSource().also { dataSource = it } },
    ).flow.cachedIn(scope)

    override fun onIntent(intent: LogIntent) {
        when (intent) {
            is LogIntent.CreateEntry -> navigateTo(EntryFormScreen())
            is LogIntent.SetDate -> setDate(intent.date)
            is LogIntent.Remove -> {
                deleteEntry(intent.item.entry.id)
                dataSource.invalidate()
            }
        }
    }

    fun setDate(date: Date) {
        // TODO
        /*
        val indexOfDate = items.first().indexOfFirst { it.date == date }
        if (indexOfDate >= 0) {
            pagination.value = pagination.value.copy(targetDate = date)
        } else {
            val startDate = date.minusMonths(1)
            val endDate = date.plusMonths(1)
            Logger.debug("LogViewModel: Start date is $startDate, end date is $endDate")
            pagination.value = pagination.value.copy(
                minimumDate = startDate,
                maximumDate = endDate,
                targetDate = date,
            )
        }
        */
    }
}