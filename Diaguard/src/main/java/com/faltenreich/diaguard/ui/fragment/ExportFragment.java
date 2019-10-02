package com.faltenreich.diaguard.ui.fragment;

import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.data.entity.Measurement;
import com.faltenreich.diaguard.data.event.Events;
import com.faltenreich.diaguard.data.event.PermissionRequestEvent;
import com.faltenreich.diaguard.data.event.PermissionResponseEvent;
import com.faltenreich.diaguard.ui.view.CategoryCheckBoxList;
import com.faltenreich.diaguard.ui.view.MainButton;
import com.faltenreich.diaguard.ui.view.MainButtonProperties;
import com.faltenreich.diaguard.util.FileUtils;
import com.faltenreich.diaguard.util.Helper;
import com.faltenreich.diaguard.util.ProgressComponent;
import com.faltenreich.diaguard.util.ViewUtils;
import com.faltenreich.diaguard.util.export.Export;
import com.faltenreich.diaguard.util.export.ExportConfig;
import com.faltenreich.diaguard.util.export.FileListener;
import com.faltenreich.diaguard.util.permission.Permission;
import com.faltenreich.diaguard.util.permission.PermissionUseCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class ExportFragment extends BaseFragment implements FileListener, MainButton {

    private static final String TAG = ExportFragment.class.getSimpleName();

    @BindView(R.id.date_start_button) Button buttonDateStart;
    @BindView(R.id.date_end_button) Button buttonDateEnd;
    @BindView(R.id.format_spinner) Spinner spinnerFormat;
    @BindView(R.id.style_table_radio) RadioButton radioButtonTableStyle;
    @BindView(R.id.style_timeline_radio) RadioButton radioButtonTimelineStyle;
    @BindView(R.id.style_log_radio) RadioButton radioButtonLogStyle;
    @BindView(R.id.note_checkbox) CheckBox checkBoxNotes;
    @BindView(R.id.tags_checkbox) CheckBox checkBoxTags;
    @BindView(R.id.categories_list) CategoryCheckBoxList categoryCheckBoxList;

    private ProgressComponent progressComponent = new ProgressComponent();

    private DateTime dateStart;
    private DateTime dateEnd;
    private ExportConfig.Style style;

    public ExportFragment() {
        super(R.layout.fragment_export, R.string.export);
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
        invalidateLayout();
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

    private void init() {
        dateStart = DateTime.now().withDayOfWeek(1);
        dateEnd = dateStart.withDayOfWeek(7);
        style = ExportConfig.Style.TABLE;
    }

    private void initLayout() {
        buttonDateStart.setText(Helper.getDateFormat().print(dateStart));
        buttonDateEnd.setText(Helper.getDateFormat().print(dateEnd));
        checkBoxNotes.setChecked(PreferenceHelper.getInstance().exportNotes());
        checkBoxNotes.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceHelper.getInstance().setExportNotes(isChecked));
        checkBoxTags.setChecked(PreferenceHelper.getInstance().exportTags());
        checkBoxTags.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceHelper.getInstance().setExportTags(isChecked));
    }

    private void invalidateLayout() {
        boolean isPdfFormat = spinnerFormat.getSelectedItemPosition() == 0;
        int visibility = isPdfFormat ? View.VISIBLE : View.GONE;

        radioButtonTableStyle.setChecked(style == ExportConfig.Style.TABLE);
        radioButtonTimelineStyle.setChecked(style == ExportConfig.Style.TIMELINE);
        radioButtonLogStyle.setChecked(style == ExportConfig.Style.LOG);
    }

    private boolean validate() {
        boolean isValid = true;

        if (categoryCheckBoxList.getSelectedCategories().length == 0) {
            ViewUtils.showSnackbar(getView(), getString(R.string.validator_value_empty_list));
            isValid = false;
        }

        return isValid;
    }

    private void exportIfInputIsValid() {
        if (validate()) {
            Events.post(new PermissionRequestEvent(Permission.WRITE_EXTERNAL_STORAGE, PermissionUseCase.EXPORT));
        }
    }

    private void export() {
        progressComponent.show(getContext());
        progressComponent.setMessage(getString(R.string.export_progress));

        Measurement.Category[] selectedCategories = categoryCheckBoxList.getSelectedCategories();
        PreferenceHelper.getInstance().setExportCategories(selectedCategories);

        ExportConfig config = new ExportConfig.Builder(getContext())
            .setDateStart(dateStart != null ? dateStart.withTimeAtStartOfDay() : null)
            .setDateEnd(dateEnd != null ? dateEnd.withTimeAtStartOfDay() : null) // FIXME: End of day?
            .setCategories(selectedCategories)
            .setExportNotes(PreferenceHelper.getInstance().exportNotes())
            .setExportTags(PreferenceHelper.getInstance().exportTags())
            .setExportFood(PreferenceHelper.getInstance().exportFood())
            .setSplitInsulin(PreferenceHelper.getInstance().exportInsulinSplit())
            .build();

        if (spinnerFormat.getSelectedItemPosition() == 0) {
            Export.exportPdf(config, this);
        } else if (spinnerFormat.getSelectedItemPosition() == 1) {
            Export.exportCsv(config, false, this);
        }
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
            openFile(file, mimeType);
        } else {
            onError();
        }
    }

    @Override
    public void onError() {
        progressComponent.dismiss();
        Toast.makeText(getContext(), getString(R.string.error_unexpected), Toast.LENGTH_LONG).show();
    }

    private void openFile(File file, String mimeType) {
        try {
            FileUtils.openFile(file, mimeType, getContext());
        } catch (ActivityNotFoundException exception) {
            Log.e(TAG, exception.getMessage());
            ViewUtils.showSnackbar(getView(), getString(R.string.error_no_app));
        }
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

    @OnClick(R.id.style_table_button)
    void onTableStyleButtonClick() {
        this.style = ExportConfig.Style.TABLE;
        invalidateLayout();
    }

    @OnClick(R.id.style_timeline_button)
    void onTimelineStyleButtonClick() {
        this.style = ExportConfig.Style.TIMELINE;
        invalidateLayout();
    }

    @OnClick(R.id.style_log_button)
    void onLogStyleButtonClick() {
        this.style = ExportConfig.Style.LOG;
        invalidateLayout();
    }

    @OnItemSelected(R.id.format_spinner)
    void onFormatSelected() {
        invalidateLayout();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PermissionResponseEvent event) {
        if (event.context == Permission.WRITE_EXTERNAL_STORAGE && event.useCase == PermissionUseCase.EXPORT && event.isGranted) {
            export();
        }
    }
}
