package com.example.android.istanbulnews;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by omar on 7/3/16.
 */

public class NewsFetcher extends AsyncTask<String, Void, ArrayList<Article>> {

    private String LOG = NewsFetcher.class.getSimpleName();

    private ArticleAdapter adaptor;

    public NewsFetcher(ArticleAdapter adapter) {
        this.adaptor = adapter;
    }

    @Override
    protected ArrayList<Article> doInBackground(String... params) {

        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String jsonString;

        try {

            final String BASE_URL = "http://content.guardianapis.com/search?";

            final String QUERY = "q";
            final String QUERY_VALUE = "istanbul";

            final String ORDER = "order-by";
            final String ORDER_VALUE = "newest";

            final String FIELDS = "show-fields";
            final String FIELDS_VALUE = "thumbnail";

            final String API = "api-key";
            final String API_VALUE = "5cf59865-25fe-4231-b20d-993ad3fcd7dc";

            final Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY, QUERY_VALUE)
                    .appendQueryParameter(ORDER, ORDER_VALUE)
                    .appendQueryParameter(FIELDS, FIELDS_VALUE)
                    .appendQueryParameter(API, API_VALUE)
                    .build();

            System.out.println(uri);

            URL url = new URL(uri.toString());

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();

            Log.d(LOG, "Status Code is: " + responseCode);

            System.out.println(responseCode);

            if (responseCode < 200 || responseCode >= 300) {
                return null;
            }

            InputStream is = conn.getInputStream();
            StringBuilder sb = new StringBuilder();

            if (is == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            jsonString = sb.toString();

        } catch (IOException e) {
            Log.d(LOG, "Error: ", e);
            return null;

        } finally {
            if (conn != null)
                conn.disconnect();

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.d(LOG, "Error closing reader: ", e);
                }
            }
        }

        return parseJSON(jsonString);
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articles) {

        if (articles != null && adaptor != null) {
            adaptor.clear();

            for (Article article: articles) {
                adaptor.add(article);
            }
        }

        super.onPostExecute(articles);
    }


    private ArrayList<Article> parseJSON(String jsonString) {

        ArrayList<Article> articles = new ArrayList<>();

        try {

            JSONObject json = new JSONObject(jsonString);
            JSONObject response = json.getJSONObject("response");

            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {

                JSONObject item = results.getJSONObject(i);

                String title = item.getString("webTitle");
                String date = item.getString("webPublicationDate");
                String url = item.getString("webUrl");

                String thumbURL = null;
                if (item.has("fields")) {
                    JSONObject fields = item.getJSONObject("fields");
                    thumbURL = fields.getString("thumbnail");
                }


                Article article = new Article(title, date, url, thumbURL);
                articles.add(article);

            }

        } catch (JSONException e) {
            Log.d(LOG, "Error Parsing JSON: ", e);
        }

        return articles;
    }

}
