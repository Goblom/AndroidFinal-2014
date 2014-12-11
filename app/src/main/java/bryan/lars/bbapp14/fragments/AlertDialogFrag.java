package bryan.lars.bbapp14.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import bryan.lars.bbapp14.R;

/**
 * Created by Goblom on 12/10/2014.
 */
public class AlertDialogFrag extends DialogFragment {
    String title, message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setIcon(R.drawable.ic_launcher);
                            builder.setTitle(title);
                            builder.setMessage(message);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Do nothing
                                }
                            });

        return builder.create();
    }
}
