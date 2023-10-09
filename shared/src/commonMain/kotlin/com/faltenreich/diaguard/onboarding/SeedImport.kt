package com.faltenreich.diaguard.onboarding

import com.faltenreich.diaguard.shared.serialization.Serialization

class SeedImport(private val serialization: Serialization) {

    fun import() {
        val yaml = """
              - name:
                - en: Blood sugar
                types:
                  - name:
                      - en: Measurement
                    units:
                        - factor: 1.0
                          name:
                            - en: Milligrams per deciliter
                          abbreviation:
                            - en: mg/dl
                        - factor: 0.0555
                          name:
                            - en: Millimoles per liter
                          abbreviation:
                            - en: mmol/l
        """
        val data = serialization.decodeYaml<List<Property>>(yaml)
        data.size
    }
}