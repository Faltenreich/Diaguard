package com.faltenreich.diaguard.feature.log;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.feature.log.entry.LogEntryListItem;
import com.faltenreich.diaguard.shared.data.database.entity.Entry;
import com.faltenreich.diaguard.shared.data.repository.EntryRepository;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.shared.view.recyclerview.adapter.BaseAdapter;
import com.faltenreich.diaguard.shared.view.recyclerview.viewholder.BaseViewHolder;

/**
 * Created by Faltenreich on 13.05.2017
 */

class LogSwipeCallback extends ItemTouchHelper.Callback {

    private final BaseAdapter adapter;

    LogSwipeCallback(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, viewHolder instanceof BaseViewHolder ? ((BaseViewHolder)viewHolder).getSwipeFlags() : 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Object item = adapter.getItem(viewHolder.getAdapterPosition());
        if (item instanceof LogEntryListItem) {
            LogEntryListItem listItem = (LogEntryListItem) item;
            Entry entry = listItem.getEntry();
            EntryRepository.getInstance().delete(entry);
            Events.post(new EntryDeletedEvent(entry, listItem.getEntryTags(), listItem.getFoodEatenList()));
        }
    }
}
