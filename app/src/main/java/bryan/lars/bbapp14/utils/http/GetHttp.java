package bryan.lars.bbapp14.utils.http;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import bryan.lars.bbapp14.impl.HttpSuccessListener;

/**
 * Created by Goblom on 12/3/2014.
 */
public class GetHttp extends AsyncTask<Void, Void, String> {

    HttpSuccessListener listener;

    public void setListener(HttpSuccessListener listener) {
        this.listener = listener;
    }

    private String URL;

    public void setURL(String URL) {
        this.URL = URL;
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = "";
        URL url;

        try {
            url = new URL(URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                              con.setRequestMethod("GET");
                              con.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            br.close();
            result = sb.toString();
        } catch (Exception e) { e.printStackTrace(); }

        return result;
    }

    @Override
    public void onPostExecute(String str) {
        listener.onHttpSuccess(HttpSuccessListener.Type.GET, str);
    }
}
