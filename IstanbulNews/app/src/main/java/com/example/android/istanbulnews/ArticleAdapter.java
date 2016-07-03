package com.example.android.istanbulnews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 *
 * Created by omar on 7/3/16.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    Context context;

    public ArticleAdapter(Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final Article article = getItem(position);

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView titleTextView = (TextView) row.findViewById(R.id.titleTextView);
        TextView dateTextView = (TextView) row.findViewById(R.id.dateTextView);
        ImageView imageView = (ImageView) row.findViewById(R.id.imageView);

        titleTextView.setText(article.getTitle());
        dateTextView.setText(article.getDate());

        if (article.getThumbURL() == null) {
            imageView.setVisibility(View.GONE);
        }


        ImageDownloader img = new ImageDownloader();

        try {
            Bitmap image = img.execute(article.getThumbURL()).get();
            imageView.setImageBitmap(image);

        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
                context.startActivity(browserIntent);

            }
        });





        return row;
    }

    private class ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        ImageView imageView;

        public ViewHolder(View view) {
            this.titleTextView = (TextView) view.findViewById(R.id.titleTextView);
            this.dateTextView = (TextView) view.findViewById(R.id.dateTextView);
            this.imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}

