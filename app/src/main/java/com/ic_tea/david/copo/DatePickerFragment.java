package com.ic_tea.david.copo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by david on 6/18/16.
 */
public class DatePickerFragment  extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) + 1; // Calendar returns 0 for january
        int year = c.get(Calendar.YEAR);

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), (EditEntry) getActivity(), year, month, day);
    }
}
