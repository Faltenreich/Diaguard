package com.faltenreich.diaguard.feature.export;

import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.databinding.FragmentExportBinding;
import com.faltenreich.diaguard.feature.datetime.DateRangePicker;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.feature.export.history.ExportHistoryFragment;
import com.faltenreich.diaguard.feature.export.job.Export;
import com.faltenreich.diaguard.feature.export.job.ExportCallback;
import com.faltenreich.diaguard.feature.export.job.FileType;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportStyle;
import com.faltenreich.diaguard.feature.navigation.FabDescribing;
import com.faltenreich.diaguard.feature.navigation.FabDescription;
import com.faltenreich.diaguard.feature.navigation.FabProperties;
import com.faltenreich.diaguard.feature.navigation.ToolbarDescribing;
import com.faltenreich.diaguard.feature.navigation.ToolbarProperties;
import com.faltenreich.diaguard.feature.preference.data.PreferenceStore;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.data.file.FileUtils;
import com.faltenreich.diaguard.shared.data.permission.Permission;
import com.faltenreich.diaguard.shared.data.permission.PermissionUseCase;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.permission.PermissionRequestEvent;
import com.faltenreich.diaguard.shared.event.permission.PermissionResponseEvent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.shared.view.progress.ProgressComponent;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.ListDividerItemDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExportFragment extends BaseFragment<FragmentExportBinding> implements ToolbarDescribing, ExportCallback, FabDescribing {

    private static final String TAG = ExportFragment.class.getSimpleName();

    private final ProgressComponent progressComponent = new ProgressComponent();
    private ExportCategoryListAdapter categoryListAdapter;

    private DateTime dateStart;
    private DateTime dateEnd;

    @Override
    protected FragmentExportBinding createBinding(LayoutInflater layoutInflater) {
        return FragmentExportBinding.inflate(layoutInflater);
    }

    @Override
    public ToolbarProperties getToolbarProperties() {
        return new ToolbarProperties.Builder()
            .setTitle(getContext(), R.string.export)
            .setMenu(R.menu.export)
            .build();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean isRecreated = categoryListAdapter.getItemCount() > 0;
        initLayout();
        if (!isRecreated) {
            initCategories();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Events.register(this);
    }

    @Override
    public void onDestroy() {
        Events.unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_history) {
            openHistory();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        dateEnd = DateTimeUtils.atEndOfDay(DateTime.now());
        // JodaTime cannot localize days of week, so consider falling back to java.util.Calendar
        dateStart = DateTimeUtils.atStartOfWeek(dateEnd);
        categoryListAdapter = new ExportCategoryListAdapter(getContext());
    }

    private void initLayout() {
        setFormat(FileType.PDF);
        setStyle(PreferenceStore.getInstance().getPdfExportStyle());

        pickDateRange(dateStart, dateEnd);
        getBinding().dateRangeButton.setOnClickListener((view) -> openDateRangePicker());
        getBinding().dateMoreButton.setOnClickListener((view) -> openDateRangeSuggestionsPicker());

        ViewUtils.setChecked(getBinding().includeCalendarWeekCheckbox, PreferenceStore.getInstance().includeCalendarWeek(), false);
        ViewUtils.setChecked(getBinding().includeGeneratedDateCheckbox, PreferenceStore.getInstance().includeGeneratedDate(), false);
        ViewUtils.setChecked(getBinding().includePageNumberCheckbox, PreferenceStore.getInstance().includePageNumber(), false);
        ViewUtils.setChecked(getBinding().noteCheckbox, PreferenceStore.getInstance().exportNotes(), false);
        ViewUtils.setChecked(getBinding().tagsCheckbox, PreferenceStore.getInstance().exportTags(), false);
        ViewUtils.setChecked(getBinding().emptyDaysCheckbox, PreferenceStore.getInstance().skipEmptyDays(), false);

        getBinding().noteCheckbox.setOnCheckedChangeListener((buttonView, isChecked) ->
            PreferenceStore.getInstance().setExportNotes(isChecked));
        getBinding().tagsCheckbox.setOnCheckedChangeListener((buttonView, isChecked) ->
            PreferenceStore.getInstance().setExportTags(isChecked));

        RecyclerView categoryListView = getBinding().categoryListView;
        categoryListView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryListView.addItemDecoration(new ListDividerItemDecoration(getContext()));
        categoryListView.setAdapter(categoryListAdapter);

        ViewCompat.setNestedScrollingEnabled(categoryListView, false);

        getBinding().formatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setFormat(getFormat());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void initCategories() {
        List<ExportCategoryListItem> items = new ArrayList<>();
        Category[] activeCategories = PreferenceStore.getInstance().getActiveCategories();
        List<Category> selectedCategories = Arrays.asList(PreferenceStore.getInstance().getExportCategories());

        for (Category category : activeCategories) {
            boolean isCategorySelected = selectedCategories.contains(category);
            boolean isExtraSelected;
            switch (category) {
                case BLOODSUGAR:
                    isExtraSelected = PreferenceStore.getInstance().limitsAreHighlighted();
                    break;
                case INSULIN:
                    isExtraSelected = PreferenceStore.getInstance().exportInsulinSplit();
                    break;
                case MEAL:
                    isExtraSelected = PreferenceStore.getInstance().exportFood();
                    break;
                default:
                    isExtraSelected = false;
            }
            items.add(new ExportCategoryListItem(category, isCategorySelected, isExtraSelected));
        }

        categoryListAdapter.addItems(items);
        categoryListAdapter.notifyDataSetChanged();
    }

    private FileType getFormat() {
        int position = getBinding().formatSpinner.getSelectedItemPosition();
        switch (position) {
            case 0: return FileType.PDF;
            case 1: return FileType.CSV;
            default: throw new IllegalArgumentException("Unknown type at position: " + position);
        }
    }

    private void setFormat(FileType format) {
        getBinding().styleGroup.setVisibility(format == FileType.PDF ? View.VISIBLE : View.GONE);
        getBinding().includeCalendarWeekGroup.setVisibility(format == FileType.PDF ? View.VISIBLE : View.GONE);
        getBinding().includeGeneratedDateGroup.setVisibility(format == FileType.PDF ? View.VISIBLE : View.GONE);
        getBinding().includePageNumberGroup.setVisibility(format == FileType.PDF ? View.VISIBLE : View.GONE);
    }

    private PdfExportStyle getStyle() {
        int position = getBinding().styleSpinner.getSelectedItemPosition();
        switch (position) {
            case 0: return PdfExportStyle.TABLE;
            case 1: return PdfExportStyle.TIMELINE;
            case 2: return PdfExportStyle.LOG;
            default: throw new IllegalArgumentException("Unknown style at position: " + position);
        }
    }

    private void setStyle(PdfExportStyle style) {
        switch (style) {
            case TABLE: getBinding().styleSpinner.setSelection(0); break;
            case TIMELINE: getBinding().styleSpinner.setSelection(1); break;
            case LOG: getBinding().styleSpinner.setSelection(2); break;
        }
    }

    private boolean isInputValid() {
        // Exports without selected categories are valid as of 3.4.0+
        return true;
    }

    private void exportIfInputIsValid() {
        if (isInputValid()) {
            Events.post(new PermissionRequestEvent(Permission.WRITE_EXTERNAL_STORAGE, PermissionUseCase.EXPORT));
        }
    }

    private void export() {
        progressComponent.show(getContext());
        progressComponent.setMessage(getString(R.string.export_progress));

        DateTime dateStart = this.dateStart != null ? this.dateStart.withTimeAtStartOfDay() : null;
        DateTime dateEnd = this.dateEnd != null ? this.dateEnd.withTimeAtStartOfDay() : null;
        Category[] categories = categoryListAdapter.getSelectedCategories();

        PdfExportConfig config = new PdfExportConfig(
            getContext(),
            this,
            dateStart,
            dateEnd,
            categories,
            getStyle(),
            getBinding().includeCalendarWeekCheckbox.isChecked(),
            getBinding().includeGeneratedDateCheckbox.isChecked(),
            getBinding().includePageNumberCheckbox.isChecked(),
            getBinding().noteCheckbox.isChecked(),
            getBinding().tagsCheckbox.isChecked(),
            getBinding().emptyDaysCheckbox.isChecked(),
            categoryListAdapter.exportFood(),
            categoryListAdapter.splitInsulin(),
            categoryListAdapter.highlightLimits()
        );
        config.persistInSharedPreferences();

        FileType type = getFormat();
        switch (type) {
            case PDF:
                Export.exportPdf(config);
                break;
            case CSV:
                Export.exportCsv(getContext(), this, dateStart, dateEnd, categories);
                break;
        }
    }

    private void openFile(File file) {
        try {
            FileUtils.openFile(getContext(), file);
        } catch (ActivityNotFoundException exception) {
            Log.e(TAG, exception.toString());
            ViewUtils.showSnackbar(getView(), getString(R.string.error_no_app));
        }
    }

    private void openHistory() {
        openFragment(new ExportHistoryFragment(), true);
    }

    @Override
    public void onProgress(@NonNull String message) {
        progressComponent.setMessage(message);
    }

    @Override
    public void onSuccess(@NonNull File file, @NonNull String mimeType) {
        progressComponent.dismiss();
        ViewUtils.showToast(getContext(), getString(R.string.export_complete, file.getAbsolutePath()));
        openFile(file);
    }

    @Override
    public void onError(@NonNull String message) {
        progressComponent.dismiss();
        ViewUtils.showSnackbar(getView(), message);
    }

    @Override
    public FabDescription getFabDescription() {
        return new FabDescription(FabProperties.confirmButton(view -> exportIfInputIsValid()), false);
    }

    private void openDateRangePicker() {
        new DateRangePicker.Builder()
            .startDate(dateStart)
            .endDate(dateEnd)
            .callback(dateTimes -> {
                if (dateTimes != null) {
                    pickDateRange(dateTimes.first, dateTimes.second);
                }
            })
            .build()
            .show(getParentFragmentManager());
    }

    private void openDateRangeSuggestionsPicker() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), getBinding().dateMoreButton);
        popupMenu.getMenuInflater().inflate(R.menu.export_date_range, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            DateTime now = DateTime.now();
            int itemId = menuItem.getItemId();
            if (itemId == R.id.action_today) {
                pickDateRange(now, now);
            } else if (itemId == R.id.action_week) {
                pickDateRange(now.withDayOfWeek(1), now);
            } else if (itemId == R.id.action_weeks_two) {
                pickDateRange(now.withDayOfWeek(1).minusWeeks(1), now);
            } else if (itemId == R.id.action_weeks_four) {
                pickDateRange(now.withDayOfWeek(1).minusWeeks(3), now);
            } else if (itemId == R.id.action_month) {
                pickDateRange(now.withDayOfMonth(1), now);
            } else if (itemId == R.id.action_quarter) {
                pickDateRange(DateTimeUtils.withStartOfQuarter(now), now);
            }
            return true;
        });
        popupMenu.show();
    }

    private void pickDateRange(DateTime start, DateTime end) {
        dateStart = start;
        dateEnd = end;
        getBinding().dateRangeButton.setText(String.format("%s  -  %s",
            Helper.getDateFormat().print(dateStart),
            Helper.getDateFormat().print(dateEnd)));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionResponseEvent event) {
        if (event.context == Permission.WRITE_EXTERNAL_STORAGE &&
            event.useCase == PermissionUseCase.EXPORT
        ) {
            if (event.isGranted) {
                export();
            } else {
                ViewUtils.showSnackbar(getView(), getString(R.string.error_unexpected));
            }
        }
    }
}
