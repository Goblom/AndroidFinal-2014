package bryan.lars.bbapp14.utils;

import android.app.Activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import bryan.lars.bbapp14.data.DateParts;
import bryan.lars.bbapp14.data.Player;
import bryan.lars.bbapp14.fragments.AlertDialogFrag;

/**
 * Created by Goblom on 12/10/2014.
 */
public class DataUtils {

    public static DateParts getDateParts(String date) {
        date = date.trim();
        DateParts parts = new DateParts();

        if (date.length() > 0) {
            String[] a = date.split("/");
            parts.setYear(Integer.parseInt(a[2]));
            parts.setMonth(Integer.parseInt(a[0]) - 1);
            parts.setDay(Integer.parseInt(a[1]));
        } else {
            Calendar cal = Calendar.getInstance();
            parts.setYear(cal.get(Calendar.YEAR));
            parts.setMonth(cal.get(Calendar.MONTH));
            parts.setDay(cal.get(Calendar.DAY_OF_MONTH));
        }

        return parts;
    }

    public static boolean hasData(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static int stringToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) { }

        return 0;
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        return sdf.format(cal.getTime());
    }

    public static boolean isPlayerValid(Activity activity, Player player) {
        String msg = "";

        if (!hasData(player.getName())) {
            msg = "Name Required \n";
        }

        if (player.getSalary() <= 0) {
            msg += "Salary Required";
        }

        if (player.getSalary() > 50000) {
            msg += "Salary cannot be higher then 50,000";
        }

        if (!msg.isEmpty()) {
            AlertDialogFrag frag = new AlertDialogFrag();
                            frag.setTitle("Please Check...");
                            frag.setMessage(msg);
                            frag.show(activity.getFragmentManager(), "dialog");
            return false;
        }

        return true;
    }
}
