package com.faltenreich.diaguard.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.faltenreich.diaguard.adapter.list.ListItemEntry;
import com.faltenreich.diaguard.data.dao.EntryDao;
import com.faltenreich.diaguard.data.entity.Entry;
import com.faltenreich.diaguard.event.Events;
import com.faltenreich.diaguard.event.data.EntryDeletedEvent;
import com.faltenreich.diaguard.ui.view.viewholder.BaseViewHolder;

/**
 * Created by Faltenreich on 13.05.2017
 */

public class ListSwipeHelper extends ItemTouchHelper.Callback {

    private BaseAdapter adapter;

    public ListSwipeHelper(BaseAdapter adapter) {
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
        if (item instanceof ListItemEntry) {
            ListItemEntry listItem = (ListItemEntry) item;
            Entry entry = listItem.getEntry();
            EntryDao.getInstance().delete(entry);
            Events.post(new EntryDeletedEvent(entry, listItem.getEntryTags()));
        }
    }
}
