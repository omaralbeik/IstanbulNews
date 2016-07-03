package com.example.android.istanbulnews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    private ArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ArrayList<Article> articles = new ArrayList<Article>();


        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArticleAdapter(MainActivity.this, articles);
        listView.setAdapter(adapter);


        if (isConnected()) {

            System.out.println("Connected");

            NewsFetcher fetcher = new NewsFetcher(adapter);
            fetcher.execute();
        } else {
            showToast("Not Connected, Please connect and try again!");
        }
    }


    // Helper methods
    public boolean isConnected() {
        ConnectivityManager connMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMngr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
