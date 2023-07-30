package com.galih.noteappcompose.ui.component

import androidx.compose.runtime.Composable
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.LocalDate

@Composable
fun DatePickerDialog(dialogState: MaterialDialogState, onDateChanged: (LocalDate) -> Unit) {
    val currentDate = LocalDate.now()
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(text = "Ok")
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick due date",
            allowedDateValidator = {
                !it.isBefore(currentDate)
            },
            onDateChange = onDateChanged
        )
    }
}