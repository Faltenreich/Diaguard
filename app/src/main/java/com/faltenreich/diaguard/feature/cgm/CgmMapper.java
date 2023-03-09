package com.faltenreich.diaguard.feature.cgm;

import com.faltenreich.diaguard.shared.data.database.entity.BloodSugar;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;

class CgmMapper {
    
    Entry mapEntry(CgmData cgmData) {
        Entry entry = new Entry();
        entry.setDate(cgmData.getDateTime());
        entry.setNote("CGM");
        return entry;
    }

    BloodSugar mapBloodSugar(CgmData cgmData) {
        BloodSugar bloodSugar = new BloodSugar();
        bloodSugar.setMgDl(cgmData.getGlucoseInMgDl());
        return bloodSugar;
    }
}
