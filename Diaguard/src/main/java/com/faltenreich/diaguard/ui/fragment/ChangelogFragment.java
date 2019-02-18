package com.faltenreich.diaguard.ui.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.data.PreferenceHelper;
import com.faltenreich.diaguard.util.ResourceUtils;
import com.faltenreich.diaguard.util.WebUtils;
import com.faltenreich.diaguard.util.theme.Theme;
import com.faltenreich.diaguard.util.theme.ThemeUtils;

/**
 * Created by Faltenreich on 26.03.2017
 */

public class ChangelogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_changelog, null);
        view.findViewById(R.id.lightButton).setOnClickListener(button -> setTheme(Theme.LIGHT));
        view.findViewById(R.id.darkButton).setOnClickListener(button -> setTheme(Theme.DARK));

        WebView webView = view.findViewById(R.id.changelog);
        String changelog = WebUtils.loadHtml(getContext(), R.raw.changelog);
        String textColorHex = Integer.toHexString(ResourceUtils.getTextColorSecondary(getContext()) & 0x00ffffff);
        String html = "<html><head>"
                + "<style type=\"text/css\">body{color: #" + textColorHex + "}"
                + "</style></head>"
                + "<body>"
                + changelog
                + "</body></html>";
        webView.loadData(html, "text/html", "uft-8");
        webView.setBackgroundColor(Color.TRANSPARENT);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.changelog)
                .setView(view)
                .setPositiveButton(R.string.ok, (dialog, which) -> {});
        return builder.create();
    }

    private void setTheme(Theme theme) {
        // TODO: Keep dialog instead of re-creating it with activity
        PreferenceHelper.getInstance().setTheme(theme);
        ThemeUtils.invalidateTheme(true);
    }
}
