// Spencer Jones
// MDV3832-0 - 062024
// MainActivity.java

package com.example.jonesspencer_ce07;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private GridView gridView;
    private List<Book> bookList;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        progressBar = findViewById(R.id.progress_bar);
        gridView = findViewById(R.id.grid_view);

        // Initialize book list and adapter
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(this, bookList);
        gridView.setAdapter(bookAdapter);

        // Start AsyncTask to fetch book data
        new FetchBooksTask().execute();
    }

    // AsyncTask class for fetching book data from Google Books API
    private class FetchBooksTask extends AsyncTask<Void, Void, List<Book>> {

        // Show progress bar before executing background task
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        // Perform background operation of fetching book data
        @Override
        protected List<Book> doInBackground(Void... voids) {
            List<Book> result = new ArrayList<>();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                // Create URL object and open connection
                URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=android");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read input stream and buffer into StringBuilder
                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                // Convert buffered data to string and parse as JSON
                String jsonResponse = buffer.toString();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                JSONArray itemsArray = jsonObject.getJSONArray("items");

                // Loop through items array and extract book data
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject bookJson = itemsArray.getJSONObject(i).getJSONObject("volumeInfo");
                    String title = bookJson.getString("title");
                    String imageUrl = bookJson.getJSONObject("imageLinks").getString("thumbnail");

                    // Add book to result list
                    result.add(new Book(title, imageUrl));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Close connection and reader
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return result; // Return list of books
        }

        // Update UI with fetched book data
        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            progressBar.setVisibility(View.GONE);
            if (books != null && !books.isEmpty()) {
                bookList.clear();
                bookList.addAll(books);
                bookAdapter.notifyDataSetChanged();
            }
        }
    }
}