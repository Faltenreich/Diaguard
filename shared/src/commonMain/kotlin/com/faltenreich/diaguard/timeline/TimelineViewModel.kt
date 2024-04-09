package com.faltenreich.diaguard.timeline

import com.faltenreich.diaguard.datetime.Date
import com.faltenreich.diaguard.datetime.DateUnit
import com.faltenreich.diaguard.datetime.factory.GetTodayUseCase
import com.faltenreich.diaguard.measurement.category.MeasurementCategory
import com.faltenreich.diaguard.measurement.category.MeasurementCategoryRepository
import com.faltenreich.diaguard.measurement.value.MeasurementValueRepository
import com.faltenreich.diaguard.navigation.CloseModalUseCase
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.OpenModalUseCase
import com.faltenreich.diaguard.navigation.modal.DatePickerModal
import com.faltenreich.diaguard.navigation.screen.EntryFormScreen
import com.faltenreich.diaguard.navigation.screen.EntrySearchScreen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.logging.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class TimelineViewModel(
    date: Date?,
    valueRepository: MeasurementValueRepository = inject(),
    measurementCategoryRepository: MeasurementCategoryRepository = inject(),
    getToday: GetTodayUseCase = inject(),
    private val navigateToScreen: NavigateToScreenUseCase = inject(),
    private val showModal: OpenModalUseCase = inject(),
    private val closeModal: CloseModalUseCase = inject(),
) : ViewModel<TimelineState, TimelineIntent, Unit>() {

    private val initialDate = date ?: getToday()
    private val currentDate = MutableStateFlow(initialDate)
    private val values = currentDate.flatMapLatest { date ->
        // FIXME: Called twice on every date change
        Logger.debug("flatMapLatest: Changing date to: $date")
        valueRepository.observeByDateRange(
            startDateTime = date.minus(2, DateUnit.DAY).atStartOfDay(),
            endDateTime = date.plus(2, DateUnit.DAY).atEndOfDay(),
        )
    }
    private val valuesForChart = values.map { it.filter { value -> value.property.category.isBloodSugar } }
    private val valuesForList = values.map { it.filterNot { value -> value.property.category.isBloodSugar } }
    private val categoriesForList = measurementCategoryRepository.observeAll().map { categories ->
        categories.filterNot(MeasurementCategory::isBloodSugar)
    }

    override val state = combine(
        flowOf(initialDate),
        currentDate,
        valuesForChart,
        valuesForList,
        categoriesForList,
        ::TimelineState,
    )

    override fun handleIntent(intent: TimelineIntent) {
        when (intent) {
            is TimelineIntent.CreateEntry -> navigateToScreen(EntryFormScreen())
            is TimelineIntent.SearchEntries -> navigateToScreen(EntrySearchScreen())
            is TimelineIntent.SelectDate -> selectDate()
            is TimelineIntent.SetDate -> currentDate.value = intent.date
        }
    }

    private fun selectDate() {
        showModal(
            DatePickerModal(
                date = currentDate.value,
                onPick = {
                    dispatchIntent(TimelineIntent.SetDate(it))
                    closeModal()
                },
            )
        )
    }
}