package bryan.lars.bbapp14.utils.http;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import bryan.lars.bbapp14.data.Player;
import bryan.lars.bbapp14.impl.HttpSuccessListener;

/**
 * Created by Goblom on 12/3/2014.
 */
public class PutHttp extends AsyncTask<Void, Void, String> {

    HttpSuccessListener listener;
    private String url;
    private JSONObject json;

    public void setPlayer(Player player) {
        json = new JSONObject();

        try {
            json.put("Id", player.getId());
            json.put("Name", player.getName());
            json.put("Rank", player.getRank());
            json.put("Position", player.getPosition());
            json.put("HireDate", player.getHireDate());
            json.put("Salary", player.getSalary());
        } catch (Exception e) {
            Log.d("Final", e.getMessage());
        }
    }
    public void setListener(HttpSuccessListener listener) {
        this.listener = listener;
    }

    public void setURL(String URL) {
        this.url = URL;
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = "";
        URL url;

        try {
            url = new URL(this.url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                              con.setRequestMethod("GET");
                              con.setDoOutput(true);
                              con.setRequestProperty("Content-Type", "application/json");
                              con.connect();

            BufferedOutputStream bos = new BufferedOutputStream(con.getOutputStream());
                                 bos.write(json.toString().getBytes());
                                 bos.flush();
                                 bos.close();

            result = con.getResponseMessage();
            con.disconnect();

            Log.d("PROJECT", "status = " + result);
        } catch (Exception e) {
            Log.d("PROJECT", "Exception Caught while trying to PUT", e);
        }

        return result;
    }

    @Override
    public void onPostExecute(String str) {
        listener.onHttpSuccess(HttpSuccessListener.Type.PUT, str);
    }
}
