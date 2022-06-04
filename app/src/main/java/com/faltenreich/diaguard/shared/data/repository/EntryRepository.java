package com.faltenreich.diaguard.shared.data.repository;


import com.faltenreich.diaguard.shared.data.database.dao.EntryDao;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;

public class EntryRepository {

    private static EntryRepository instance;

    public static EntryRepository getInstance() {
        if (instance == null) {
            instance = new EntryRepository();
        }
        return instance;
    }

    private final EntryDao dao = EntryDao.getInstance();

    public Entry createOrUpdate(Entry entry) {
        return dao.createOrUpdate(entry);
    }
}
