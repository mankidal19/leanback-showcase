package nurulaiman.sony.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItemListResponse;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class VideoUtils {

    private final static String TAG = VideoUtils.class.getSimpleName();

    /** Application name. */
    private static final String APPLICATION_NAME = "API Sample";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/java-youtube-api-tests");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    private static final Collection<String> SCOPES = Arrays.asList("YouTubeScopes.https://www.googleapis.com/auth/youtube.force-ssl YouTubeScopes.https://www.googleapis.com/auth/youtubepartner");

    private static final String baseUrl= "https://youtu.be/";
    private static final String apiKey = "AIzaSyCEfmvLPr2q4esyG2ow0XQRTaYGmApjxMQ";
    private String anotherUrl;
    private String title = "no title";
    private ArrayList<String> titles = new ArrayList<String>();

    public String getVideoTitle(String videoId) {


        try {
            if (videoId != null) {
                /*URL embededURL = new URL("http://www.youtube.com/oembed?url=" +
                        baseUrl + videoId + "&format=json"
                );*/
                anotherUrl="https://www.googleapis.com/youtube/v3/videos?part=id%2C+snippet&id="+videoId+"&key="+apiKey;
                Log.d(TAG,anotherUrl);

                //title = new JSONObject(getJSON(anotherUrl)).getString("title");
                //Log.d(TAG,title);
                JsonParse jsonParse = new JsonParse();
                jsonParse.execute();

                return title;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getVideoTitles(String playlistId) {


        try {
            if (playlistId != null) {
                /*URL embededURL = new URL("http://www.youtube.com/oembed?url=" +
                        baseUrl + videoId + "&format=json"
                );*/
                anotherUrl="https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=20&playlistId="+playlistId+"&key="+apiKey;
                Log.d(TAG,anotherUrl);

                JsonParse jsonParse = new JsonParse();
                jsonParse.execute();

                return titles;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getJSON(String url) {
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

    public static void getPlaylistItems()  throws IOException {
        YouTube youtube = getYouTubeService();
        try {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("part", "snippet,contentDetails");
            parameters.put("maxResults", "25");
            parameters.put("playlistId", "PLV71sdBgCmhSQwckYsNtNn7e5CGUnldJk");

            YouTube.PlaylistItems.List playlistItemsListByPlaylistIdRequest = youtube.playlistItems().list(parameters.get("part").toString());
            if (parameters.containsKey("maxResults")) {
                playlistItemsListByPlaylistIdRequest.setMaxResults(Long.parseLong(parameters.get("maxResults").toString()));
            }

            if (parameters.containsKey("playlistId") && parameters.get("playlistId") != "") {
                playlistItemsListByPlaylistIdRequest.setPlaylistId(parameters.get("playlistId").toString());
            }

            PlaylistItemListResponse response = playlistItemsListByPlaylistIdRequest.execute();
            Log.d(TAG,response.toString());
            System.out.println(response);
        }

        catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }


    }

    public static YouTube getYouTubeService() throws IOException {
        Credential credential = authorize();
        return new YouTube.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = VideoUtils.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader( in ));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    private class JsonParse extends AsyncTask<String, Void, ArrayList<String>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {

            try{
                JSONObject jsonObject = new JSONObject(getJSON(anotherUrl));
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

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            //super.onPostExecute(strings);
            titles=strings;
        }
    }


}
