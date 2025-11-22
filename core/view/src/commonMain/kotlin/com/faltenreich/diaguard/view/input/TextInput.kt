package com.faltenreich.diaguard.view.input

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TextInput(
    input: String,
    onInputChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    // Removes borders but leaves additional bottom padding
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        disabledBorderColor = Color.Transparent,
        errorBorderColor = Color.Transparent,
    ),
) {
    OutlinedTextField(
        value = input,
        onValueChange = onInputChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label?.let{ { Text(label) } },
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        maxLines = maxLines,
        singleLine = maxLines == 1,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = colors,
    )
}

@Preview
@Composable
private fun Preview() {
    TextInput(
        input = "Input",
        onInputChange = {},
    )
}