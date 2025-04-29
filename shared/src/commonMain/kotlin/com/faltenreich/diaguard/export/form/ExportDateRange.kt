package com.faltenreich.diaguard.export.form

import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.export_date_range_dropdown_month_current
import diaguard.shared.generated.resources.export_date_range_dropdown_quarter_current
import diaguard.shared.generated.resources.export_date_range_dropdown_week_current
import diaguard.shared.generated.resources.export_date_range_dropdown_week_today
import diaguard.shared.generated.resources.export_date_range_dropdown_weeks_four
import diaguard.shared.generated.resources.export_date_range_dropdown_weeks_two
import org.jetbrains.compose.resources.StringResource

enum class ExportDateRange(
    val titleResource: StringResource,
) {

    TODAY(
        titleResource = Res.string.export_date_range_dropdown_week_today,
    ),
    WEEK_CURRENT(
        titleResource = Res.string.export_date_range_dropdown_week_current,
    ),
    WEEKS_TWO(
        titleResource = Res.string.export_date_range_dropdown_weeks_two,
    ),
    WEEKS_FOUR(
        titleResource = Res.string.export_date_range_dropdown_weeks_four,
    ),
    MONTH_CURRENT(
        titleResource = Res.string.export_date_range_dropdown_month_current,
    ),
    QUARTER_CURRENT(
        titleResource = Res.string.export_date_range_dropdown_quarter_current,
    ),
}
