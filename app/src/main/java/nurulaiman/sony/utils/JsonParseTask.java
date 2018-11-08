package nurulaiman.sony.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class JsonParseTask extends AsyncTask<String, Void, ArrayList<String>> {

    private final static String TAG = JsonParseTask.class.getSimpleName();

    private static final String apiKey = "AIzaSyCEfmvLPr2q4esyG2ow0XQRTaYGmApjxMQ";
    private String playlistId;
    private String videoUrl;
    private ArrayList<String> titles = new ArrayList<String>();


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        playlistId = params[0];
        String videoUrl = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=20&playlistId="+playlistId+"&key="+apiKey;

        try{
            JSONObject jsonObject = new JSONObject(getJSON(videoUrl));
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            Log.d(TAG,"jsonObject- "+jsonObject.toString());
            Log.d(TAG,"jsonArray- "+jsonArray.toString());

            for(int i=0;i<jsonArray.length();i++){
                JSONObject snippetJsonObject = jsonArray.getJSONObject(i).getJSONObject("snippet");
                titles.add(snippetJsonObject.getString("title"));

                Log.d(TAG,titles.get(i));
            }

            return titles;

        }
        catch (Exception e){
            Log.d(TAG,"error! "+e.getMessage());
        }


        return null;
    }

    public interface AsyncResponse {
        void processFinish(ArrayList<String> output);
    }

    public AsyncResponse delegate = null;

    public JsonParseTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
        delegate.processFinish(strings);
    }

    protected String getJSON(String url) {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode(); // to check success and failure of API call

            con.connect();

            Log.d(TAG,"Response Code : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }
}
