package com.faltenreich.diaguard.ui.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.ResourceUtils;
import com.faltenreich.diaguard.util.ViewUtils;
import com.faltenreich.diaguard.util.WebUtils;
import com.faltenreich.diaguard.util.theme.Theme;
import com.faltenreich.diaguard.util.theme.ThemeUtils;

/**
 * Created by Faltenreich on 26.03.2017
 */

public class ChangelogFragment extends DialogFragment {

    @BindView(R.id.lightButtonBackground) View lightButtonBackground;
    @BindView(R.id.darkButtonBackground) View darkButtonBackground;
    @BindView(R.id.lightButton) View lightButton;
    @BindView(R.id.darkButton) View darkButton;
    @BindView(R.id.lightLabel) TextView lightLabel;
    @BindView(R.id.darkLabel) TextView darkLabel;
    @BindView(R.id.changelog) WebView changelogView;
    private TextView titleView;

    private Theme temporaryTheme;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_changelog, null);
        ButterKnife.bind(this, view);

        titleView = new TextView(getContext(), null, android.R.attr.windowTitleStyle);
        int padding = (int) ResourceUtils.getDialogPreferredPadding(getContext());
        titleView.setPadding(padding, padding, padding, (int) getResources().getDimension(R.dimen.margin_between));
        titleView.setText(R.string.changelog);

        initLayout();
        invalidateLayout();

        return new AlertDialog.Builder(getContext())
                .setCustomTitle(titleView)
                .setView(view)
                .setPositiveButton(R.string.ok, (dlg, which) -> { })
                .create();
    }

    @Override
    public void onDestroy() {
        applyTheme();
        super.onDestroy();
    }

    private void init() {
        temporaryTheme = PreferenceHelper.getInstance().getTheme();
    }

    private void initLayout() {
        lightButton.setOnClickListener(button -> tryTheme(Theme.LIGHT));
        darkButton.setOnClickListener(button -> tryTheme(Theme.DARK));
    }

    // TODO: Style positive button
    private void invalidateLayout() {
        if (getContext() != null) {
            boolean isDark = temporaryTheme == Theme.DARK;
            int backgroundColor = ContextCompat.getColor(getContext(), isDark ? R.color.background_dark_primary : R.color.background_light_primary);
            int highlightColor = ContextCompat.getColor(getContext(), isDark ? R.color.background_dark_tertiary : R.color.background_light_tertiary);
            int textColor = isDark ? Color.WHITE : Color.BLACK;

            ViewUtils.setBackgroundColorAnimated(lightButtonBackground, isDark ? Color.TRANSPARENT : highlightColor);
            ViewUtils.setBackgroundColorAnimated(darkButtonBackground, isDark ? highlightColor : Color.TRANSPARENT);

            titleView.setTextColor(textColor);
            lightLabel.setTextColor(textColor);
            darkLabel.setTextColor(textColor);

            if (getDialog() != null && getDialog().getWindow() != null && getDialog().getWindow().getDecorView().getBackground() != null) {
                int oldBackgroundColor = ContextCompat.getColor(getContext(), isDark ? R.color.background_light_primary : R.color.background_dark_primary);
                ViewUtils.setColorFilterAnimated(getDialog().getWindow().getDecorView().getBackground(), oldBackgroundColor, backgroundColor);
            }

            String changelog = WebUtils.loadHtml(getContext(), R.raw.changelog);
            String html = getHtml(changelog, textColor);
            changelogView.loadDataWithBaseURL(null, html, "text/html", "uft-8", null);
            changelogView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private String getHtml(String body, @ColorInt int textColor) {
        String textColorHex = Integer.toHexString(textColor & 0x00ffffff);
        return  "<html><head>"
                + "<style type=\"text/css\">body{color: #" + textColorHex + "}"
                + "</style></head>"
                + "<body>"
                + body
                + "</body></html>";
    }

    private void tryTheme(Theme theme) {
        if (temporaryTheme != theme) {
            temporaryTheme = theme;
            invalidateLayout();
        }
    }

    private void applyTheme() {
        if (temporaryTheme != PreferenceHelper.getInstance().getTheme()) {
            PreferenceHelper.getInstance().setTheme(temporaryTheme);
            ThemeUtils.setTheme(true);
        }
    }
}
