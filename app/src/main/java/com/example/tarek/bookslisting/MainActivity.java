package com.example.tarek.bookslisting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button search;
    ListView listView;
    Adapter adapter;
   TextView mEmptyStateTextView ;
    View loadingIndicator;
    private static String url = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        adapter = new Adapter(this, new ArrayList<BookSrc>());
         BookAsyncTask task = new BookAsyncTask();
         loadingIndicator= findViewById(R.id.loading_indicator);
        editText = (EditText) findViewById(R.id.search_edit_text);
        search = (Button) findViewById(R.id.search_button);
        listView = findViewById(R.id.search_list);
        mEmptyStateTextView= (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);


        if (url.equals("https://www.googleapis.com/books/v1/volumes?q=")){
            url += "ابن كثير";
        task.execute(url);
        listView.setAdapter(adapter);

        }
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://www.googleapis.com/books/v1/volumes?q=";
                url += editText.getText();
                BookAsyncTask task = new BookAsyncTask();
                task.execute(url);
                loadingIndicator.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setText(R.string.loading);
                listView.setAdapter(adapter);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                BookSrc bookSrc = (BookSrc) adapter.getItem(i);
                
                Uri uri=Uri.parse(bookSrc.getBookUrl());

                Intent webSite = new Intent(Intent.ACTION_VIEW,uri);

                startActivity(webSite);
            }
        });



    }

    private class BookAsyncTask extends AsyncTask<String, Void, List<BookSrc>> {


        @Override
        protected List<BookSrc> doInBackground(String... urls) {

            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<BookSrc> result = QueryUtilities.fetchBookData(urls[0]);
            return result;
        }

        protected void onPostExecute(List<BookSrc> data) {
            loadingIndicator.setVisibility(View.GONE);
            ConnectivityManager cm =
                    (ConnectivityManager)MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnected();
            if(isConnected)
            mEmptyStateTextView.setText(R.string.no_book);
            else
                mEmptyStateTextView.setText(R.string.no_internet);
            adapter.clear();
            if (data != null && !data.isEmpty()) {
                adapter.addAll(data);
            }
        }


    }
}

