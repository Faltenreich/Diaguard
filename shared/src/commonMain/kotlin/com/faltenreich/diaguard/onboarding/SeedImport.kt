package com.faltenreich.diaguard.onboarding

import net.mamoe.yamlkt.Yaml

class SeedImport {

    fun import() {
        val yaml = """
            properties:
                - blood_sugar:
                    name:
                        - en: Blood sugar
                    types:
                        - measurement:
                            name:
                                - en: Measurement
                            units:
                                - milligrams per deciliter:
                                    factor: 1.0
                                    name:
                                        - en: Milligrams per deciliter
                                    abbreviation:
                                        - en: mg/dl
                                - millimoles per liter:
                                    factor: 0.0555
                                    name:
                                        - en: Millimoles per liter
                                    abbreviation:
                                        - en: mmol
        """
        val data = Yaml.decodeFromString(Properties.serializer(), yaml)
        data.properties.size
    }
}