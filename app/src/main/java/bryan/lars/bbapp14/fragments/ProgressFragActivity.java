package bryan.lars.bbapp14.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
 * Created by Goblom on 12/10/2014.
 */
public class ProgressFragActivity extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        String msg = getArguments().getString("msg");

        final ProgressDialog progress = new ProgressDialog(getActivity());
                             progress.setTitle("Contacting Web Service...");
                             progress.setMessage(msg);
                             progress.setIndeterminate(true);
                             progress.setCancelable(false);

        return progress;
    }
}
