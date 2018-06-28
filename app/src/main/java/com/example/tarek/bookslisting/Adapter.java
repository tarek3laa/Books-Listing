package com.example.tarek.bookslisting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends ArrayAdapter<BookSrc> {

    Context context;
    public Adapter(@NonNull Context context, ArrayList<BookSrc>resource) {
        super(context, 0,resource);

        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view1 =convertView;
        if (view1 == null) {
            view1 = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView title = view1.findViewById(R.id.title);
        TextView description=view1.findViewById(R.id.author);
        ImageView image = view1.findViewById(R.id.image);
        BookSrc bookSrc = (BookSrc) getItem(position);

        title.setText(bookSrc.getTitle());
        description.setText(bookSrc.getAuthors());
        if(bookSrc.getImg()!=null){
        String []url=bookSrc.getImg().split("http");
         Picasso.get().load("https"+url[1])//.resize(5,5)
        . into(image);}
        else
        {
            image.setImageResource(R.drawable.not_found);


        }
        return view1;
    }
}
