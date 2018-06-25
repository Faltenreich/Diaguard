package com.faltenreich.diaguard;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.InputType;

import com.faltenreich.diaguard.ui.view.LocalizedNumberEditText;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DecimalFormatSymbols;

@RunWith(AndroidJUnit4.class)
public class LocalizedNumberEditTextTest {

    private LocalizedNumberEditText localizedNumberEditText;
    private char localizedSeparator = DecimalFormatSymbols.getInstance().getDecimalSeparator();

    @Before
    public void setup() {
        localizedNumberEditText = new LocalizedNumberEditText(InstrumentationRegistry.getTargetContext());
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
