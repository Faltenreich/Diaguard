package com.faltenreich.diaguard.shared.view.edittext;

import android.text.InputType;

public class LocalizedNumberEditTextUtils {

    public static int countOccurrences(String target, Character... characters) {
        int count = 0;
        for (Character targetCharacter : target.toCharArray()) {
            for (Character character : characters) {
                if (character.equals(targetCharacter)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int firstIndexOfOrLastIndex(String text, Character character) {
        int index = text.indexOf(character);
        return index >= 0 ? index : text.length() - 1;
    }

    public static boolean isInputTypeNumberDecimal(int inputType) {
        return (inputType & InputType.TYPE_NUMBER_FLAG_DECIMAL) == InputType.TYPE_NUMBER_FLAG_DECIMAL;
    }
}
