package com.faltenreich.diaguard.preference

import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.preference_decimal_places

data object DecimalPlaces : Preference<Int, Int>(
    key = Res.string.preference_decimal_places,
    default = 1,
    onRead = { it },
    onWrite = { it },
)