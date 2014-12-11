package bryan.lars.bbapp14.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import bryan.lars.bbapp14.data.DateParts;
import bryan.lars.bbapp14.utils.DataUtils;

/**
 * Created by Goblom on 12/10/2014.
 */
public class TimeFragActivity extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private boolean fired = false;

    public static interface IDateSetListener {
        void onDateSet(String dateString);
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        String hireDate = getArguments().getString("date", "");
        DateParts dateParts = DataUtils.getDateParts(hireDate);
        fired = false;

        return new DatePickerDialog(getActivity(), this, dateParts.getYear(), dateParts.getMonth(), dateParts.getDay());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (!fired) {
            IDateSetListener listener = (IDateSetListener) getActivity();
                             listener.onDateSet(String.format("%02d/%02d/%02d", month + 1, day, year));
        }

        fired = true;
    }
}
