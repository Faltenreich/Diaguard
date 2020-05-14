package com.faltenreich.diaguard.feature.export;

import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.feature.preference.data.PreferenceHelper;
import com.faltenreich.diaguard.shared.data.database.entity.Category;
import com.faltenreich.diaguard.shared.event.Events;
import com.faltenreich.diaguard.shared.event.permission.PermissionRequestEvent;
import com.faltenreich.diaguard.shared.event.permission.PermissionResponseEvent;
import com.faltenreich.diaguard.feature.export.history.ExportHistoryFragment;
import com.faltenreich.diaguard.feature.export.job.Export;
import com.faltenreich.diaguard.feature.export.job.ExportCallback;
import com.faltenreich.diaguard.feature.export.job.FileType;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportConfig;
import com.faltenreich.diaguard.feature.export.job.pdf.meta.PdfExportStyle;
import com.faltenreich.diaguard.shared.view.fragment.BaseFragment;
import com.faltenreich.diaguard.feature.datetime.DatePickerFragment;
import com.faltenreich.diaguard.shared.view.recyclerview.decoration.LinearDividerItemDecoration;
import com.faltenreich.diaguard.feature.navigation.MainButton;
import com.faltenreich.diaguard.feature.navigation.MainButtonProperties;
import com.faltenreich.diaguard.feature.datetime.DateTimeUtils;
import com.faltenreich.diaguard.shared.data.file.FileUtils;
import com.faltenreich.diaguard.shared.Helper;
import com.faltenreich.diaguard.shared.view.progress.ProgressComponent;
import com.faltenreich.diaguard.shared.view.ViewUtils;
import com.faltenreich.diaguard.shared.data.permission.Permission;
import com.faltenreich.diaguard.shared.data.permission.PermissionUseCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class ExportFragment extends BaseFragment implements ExportCallback, MainButton {

    private static final String TAG = ExportFragment.class.getSimpleName();

    @BindView(R.id.scroll_view) NestedScrollView scrollView;
    @BindView(R.id.date_start_button) Button buttonDateStart;
    @BindView(R.id.date_end_button) Button buttonDateEnd;
    @BindView(R.id.format_spinner) Spinner spinnerFormat;
    @BindView(R.id.style_group) Group styleGroup;
    @BindView(R.id.style_spinner) Spinner spinnerStyle;
    @BindView(R.id.header_checkbox) CheckBox checkBoxHeader;
    @BindView(R.id.header_group) Group headerGroup;
    @BindView(R.id.footer_checkbox) CheckBox checkBoxFooter;
    @BindView(R.id.footer_group) Group footerGroup;
    @BindView(R.id.note_checkbox) CheckBox checkBoxNotes;
    @BindView(R.id.tags_checkbox) CheckBox checkBoxTags;
    @BindView(R.id.categories_list) RecyclerView categoryCheckBoxList;

    private ProgressComponent progressComponent = new ProgressComponent();
    private ExportCategoryListAdapter categoryCheckBoxListAdapter;

    private DateTime dateStart;
    private DateTime dateEnd;

    public ExportFragment() {
        super(R.layout.fragment_export, R.string.export, R.menu.export);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
        initCategories();
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
        switch (item.getItemId()) {
            case R.id.action_history:
                openHistory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private FileType getFormat() {
        int selectedPosition = spinnerFormat.getSelectedItemPosition();
        switch (selectedPosition) {
            case 0: return FileType.PDF;
            case 1: return FileType.CSV;
            default: throw new IllegalArgumentException("Unknown type at position: " + selectedPosition);
        }
    }

    private void init() {
        // FIXME: Week days cannot be localized via JodaTime, so consider switching to ThreeTenABP
        dateEnd = DateTime.now().withTime(23, 59, 59, 999);
        dateStart = dateEnd.withDayOfWeek(1).withTimeAtStartOfDay();
        categoryCheckBoxListAdapter = new ExportCategoryListAdapter(getContext());
    }

    private void initLayout() {
        setFormat(FileType.PDF);
        setStyle(PreferenceHelper.getInstance().getPdfExportStyle());

        buttonDateStart.setText(Helper.getDateFormat().print(dateStart));
        buttonDateEnd.setText(Helper.getDateFormat().print(dateEnd));

        ViewUtils.setChecked(checkBoxHeader, PreferenceHelper.getInstance().exportHeader(), false);
        ViewUtils.setChecked(checkBoxFooter, PreferenceHelper.getInstance().exportFooter(), false);
        ViewUtils.setChecked(checkBoxNotes, PreferenceHelper.getInstance().exportNotes(), false);
        ViewUtils.setChecked(checkBoxTags, PreferenceHelper.getInstance().exportTags(), false);

        checkBoxNotes.setOnCheckedChangeListener((buttonView, isChecked) ->
            PreferenceHelper.getInstance().setExportNotes(isChecked));
        checkBoxTags.setOnCheckedChangeListener((buttonView, isChecked) ->
            PreferenceHelper.getInstance().setExportTags(isChecked));

        categoryCheckBoxList.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryCheckBoxList.addItemDecoration(new LinearDividerItemDecoration(getContext()));
        categoryCheckBoxList.setAdapter(categoryCheckBoxListAdapter);

        ViewCompat.setNestedScrollingEnabled(categoryCheckBoxList, false);
    }

    private void initCategories() {
        List<ExportCategoryListItem> items = new ArrayList<>();
        Category[] activeCategories = PreferenceHelper.getInstance().getActiveCategories();
        List<Category> selectedCategories = Arrays.asList(PreferenceHelper.getInstance().getExportCategories());

        for (Category category : activeCategories) {
            boolean isCategorySelected = selectedCategories.contains(category);
            boolean isExtraSelected;
            switch (category) {
                case BLOODSUGAR:
                    isExtraSelected = PreferenceHelper.getInstance().limitsAreHighlighted();
                    break;
                case INSULIN:
                    isExtraSelected = PreferenceHelper.getInstance().exportInsulinSplit();
                    break;
                case MEAL:
                    isExtraSelected = PreferenceHelper.getInstance().exportFood();
                    break;
                default:
                    isExtraSelected = false;
            }
            items.add(new ExportCategoryListItem(category, isCategorySelected, isExtraSelected));
        }

        categoryCheckBoxListAdapter.addItems(items);
        categoryCheckBoxListAdapter.notifyItemRangeInserted(0, items.size());
    }

    private void setFormat(FileType format) {
        styleGroup.setVisibility(format == FileType.PDF ? View.VISIBLE : View.GONE);
        headerGroup.setVisibility(format == FileType.PDF ? View.VISIBLE : View.GONE);
        footerGroup.setVisibility(format == FileType.PDF ? View.VISIBLE : View.GONE);
    }

    private PdfExportStyle getStyle() {
        int selectedPosition = spinnerStyle.getSelectedItemPosition();
        switch (selectedPosition) {
            case 0: return PdfExportStyle.TABLE;
            case 1: return PdfExportStyle.TIMELINE;
            case 2: return PdfExportStyle.LOG;
            default: throw new IllegalArgumentException("Unknown style at position: " + selectedPosition);
        }
    }

    private void setStyle(PdfExportStyle style) {
        switch (style) {
            case TABLE:
                spinnerStyle.setSelection(0);
                break;
            case TIMELINE:
                spinnerStyle.setSelection(1);
                break;
            case LOG:
                spinnerStyle.setSelection(2);
                break;
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
        Category[] categories = categoryCheckBoxListAdapter.getSelectedCategories();

        PdfExportConfig config = new PdfExportConfig(
            getContext(),
            this,
            dateStart,
            dateEnd,
            categories,
            getStyle(),
            checkBoxHeader.isChecked(),
            checkBoxFooter.isChecked(),
            checkBoxNotes.isChecked(),
            checkBoxTags.isChecked(),
            categoryCheckBoxListAdapter.exportFood(),
            categoryCheckBoxListAdapter.splitInsulin(),
            categoryCheckBoxListAdapter.highlightLimits()
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
            Log.e(TAG, exception.getMessage());
            ViewUtils.showSnackbar(getView(), getString(R.string.error_no_app));
        }
    }

    private void openHistory() {
        showFragment(new ExportHistoryFragment());
    }

    @Override
    public void onProgress(String message) {
        progressComponent.setMessage(message);
    }

    @Override
    public void onSuccess(File file, String mimeType) {
        progressComponent.dismiss();
        if (file != null) {
            Toast.makeText(getContext(), String.format(getString(R.string.export_complete), file.getAbsolutePath()), Toast.LENGTH_LONG).show();
            openFile(file);
        } else {
            onError();
        }
    }

    @Override
    public void onError() {
        progressComponent.dismiss();
        Toast.makeText(getContext(), getString(R.string.error_unexpected), Toast.LENGTH_LONG).show();
    }

    @Override
    public MainButtonProperties getMainButtonProperties() {
        return MainButtonProperties.confirmButton(view -> exportIfInputIsValid(), false);
    }

    @OnClick(R.id.date_start_button)
    void showStartDatePicker() {
        DatePickerFragment.newInstance(dateStart, null, dateEnd, dateTime -> {
            if (dateTime != null) {
                dateStart = dateTime;
                buttonDateStart.setText(Helper.getDateFormat().print(dateStart));
            }
        }).show(getFragmentManager());
    }

    @OnClick(R.id.date_end_button)
    void showEndDatePicker() {
        DatePickerFragment.newInstance(dateEnd, dateStart, null, dateTime -> {
            if (dateTime != null) {
                dateEnd = dateTime;
                buttonDateEnd.setText(Helper.getDateFormat().print(dateEnd));
            }
        }).show(getFragmentManager());
    }

    @OnClick(R.id.date_more_button)
    void openDateRangePicker(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.export_date_range, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            DateTime now = DateTime.now();
            switch (menuItem.getItemId()) {
                case R.id.action_today:
                    pickDateRange(now, now);
                    break;
                case R.id.action_week:
                    pickDateRange(now.withDayOfWeek(1), now);
                    break;
                case R.id.action_weeks_two:
                    pickDateRange(now.withDayOfWeek(1).minusWeeks(1), now);
                    break;
                case R.id.action_weeks_four:
                    pickDateRange(now.withDayOfWeek(1).minusWeeks(3), now);
                    break;
                case R.id.action_month:
                    pickDateRange(now.withDayOfMonth(1), now);
                    break;
                case R.id.action_quarter:
                    pickDateRange(DateTimeUtils.withStartOfQuarter(now), now);
                    break;
            }
            return true;
        });
        popupMenu.show();
    }

    private void pickDateRange(DateTime start, DateTime end) {
        dateStart = start;
        buttonDateStart.setText(Helper.getDateFormat().print(dateStart));
        dateEnd = end;
        buttonDateEnd.setText(Helper.getDateFormat().print(dateEnd));
    }

    @OnItemSelected(R.id.format_spinner)
    void onFormatSelected() {
        setFormat(getFormat());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionResponseEvent event) {
        if (event.context == Permission.WRITE_EXTERNAL_STORAGE &&
            event.useCase == PermissionUseCase.EXPORT &&
            event.isGranted
        ) {
            export();
        }
    }
}
