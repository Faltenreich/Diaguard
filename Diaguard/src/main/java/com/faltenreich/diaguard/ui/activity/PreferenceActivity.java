package com.faltenreich.diaguard.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.faltenreich.diaguard.R;
import com.faltenreich.diaguard.ui.fragment.PreferenceFragment;
import com.faltenreich.diaguard.util.SystemUtils;
import com.faltenreich.diaguard.util.event.Events;
import com.faltenreich.diaguard.util.event.PermissionDeniedEvent;
import com.faltenreich.diaguard.util.event.PermissionGrantedEvent;

/**
 * Created by Filip on 26.10.13.
 */
public class PreferenceActivity extends BaseActivity {
    
    public PreferenceActivity() {
        super(R.layout.activity_preferences);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(R.id.content, new PreferenceFragment()).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case SystemUtils.PERMISSION_WRITE_EXTERNAL_STORAGE: {
                boolean permissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                Events.post(permissionGranted ?
                        new PermissionGrantedEvent(Manifest.permission.WRITE_EXTERNAL_STORAGE) :
                        new PermissionDeniedEvent(Manifest.permission.WRITE_EXTERNAL_STORAGE));
            }
        }
    }
}