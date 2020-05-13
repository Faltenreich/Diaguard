package com.faltenreich.diaguard.shared.view.edittext;

import android.text.InputType;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.faltenreich.diaguard.test.junit.rule.CleanUpData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.text.DecimalFormatSymbols;

@RunWith(AndroidJUnit4.class)
public class LocalizedNumberEditTextTest {

    private LocalizedNumberEditText localizedNumberEditText;
    private char localizedSeparator = DecimalFormatSymbols.getInstance().getDecimalSeparator();

    @Rule public final TestRule dataCleanUp = new CleanUpData();

    @Before
    public void setup() {
        localizedNumberEditText = new LocalizedNumberEditText(ApplicationProvider.getApplicationContext());
        localizedNumberEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    @Test
    public void testLocalizedSeparator() {
        localizedNumberEditText.setText("1" + localizedSeparator + "2");
        Assert.assertEquals("1" + localizedSeparator + "2", localizedNumberEditText.getText().toString());
        Assert.assertEquals("1.2", localizedNumberEditText.getNonLocalizedText());
    }

    @Test
    public void testMultipleSeparators() {
        localizedNumberEditText.setText("...");
        Assert.assertEquals(".", localizedNumberEditText.getText().toString());
    }
}
