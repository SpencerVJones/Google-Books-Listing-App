// Spencer Jones
// MDV3832-0 - 062024
// BookAdapter.java

package com.example.jonesspencer_ce07;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BookAdapter extends BaseAdapter {
    private Context context;
    private List<Book> books;

    // Constructor
    public BookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    // Method to return number of items in book list
    @Override
    public int getCount() {
        return books.size();
    }

    // Return book item at specific position
    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    // Return item ID for specific position
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Create and return view for specific position in GridView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        }

        // Get current book from list
        Book book = books.get(position);

        // Find TextView and ImageView in view
        TextView title = convertView.findViewById(R.id.book_title);
        ImageView image = convertView.findViewById(R.id.book_image);

        title.setText(book.getTitle()); // Set book title in TextView
        new ImageLoadTask(book.getImageUrl(), image).execute(); // Start AsyncTask to load book image

        return convertView;
    }


    // AsyncTask class for loading images from URL in background
    private class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private String url;
        private ImageView imageView;

        // Constructor
        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        // Background operation of downloading image
        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                // Create URL object and open connection
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();

                // Read input stream and decode into Bitmap
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // Update ImageView with downloaded Bitmap
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }
}