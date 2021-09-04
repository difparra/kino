package com.diegoparra.kino.ui._components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.diegoparra.kino.R
import com.diegoparra.kino.ui.theme.KinoTheme

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    hint: String = stringResource(id = R.string.label_search),
    keyboardType: KeyboardType = KeyboardType.Text,
    icon: ImageVector = Icons.Default.Search
) {
    Surface(modifier = modifier) {
        val focusManager = LocalFocusManager.current
        TextField(
            colors = TextFieldDefaults.outlinedTextFieldColors(),
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = onTextChanged,
            placeholder = { Text(text = hint, modifier = Modifier.fillMaxWidth()) },
            leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.clickable(onClick = onClear)
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    //  To close keyboard
                    focusManager.clearFocus()
                    /*
                        Another valid option to close the keyboard may be using:
                        val keyboardController = LocalSoftwareKeyboardController.current
                        keyboardController?.hide()

                        But focus Manager has the advantage of hiding the cursor (clearing focus)
                        as well.
                     */

                    //  Additional function
                    onSearch(text)
                }
            ),
            singleLine = true
        )
    }
}

@Preview
@Composable
fun SearchBarPreview() {
    KinoTheme {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = "query",
            onTextChanged = {},
            onSearch = {},
            onClear = {}
        )
    }
}