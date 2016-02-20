package com.irahavoi.qrioscat.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.irahavoi.qrioscat.R;
import com.irahavoi.qrioscat.Utility;
import com.irahavoi.qrioscat.data.ArtworkProvider;
import com.irahavoi.qrioscat.domain.Comment;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment>{
    private final Context context;
    private final List<Comment> values;

    public CommentAdapter(Context context, List<Comment> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rootView = inflater.inflate(R.layout.comment_item, parent, false);
        TextView comment = (TextView) rootView.findViewById(R.id.comment);
        comment.setText(values.get(position).getComment());

        ImageView delete = (ImageView) rootView.findViewById(R.id.delete_comment);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri.Builder builder = new Uri.Builder();
                Uri uri = builder.scheme("content")
                        .authority(ArtworkProvider.PROVIDER_NAME)
                        .appendPath("artworks")
                        .appendPath(String.valueOf(values.get(position).getArtworkId()))
                        .appendPath("comments")
                        .build();


                getContext().getContentResolver().delete(uri, "_ID = ?",
                        new String[]{String.valueOf(values.get(position).getId())});

                CommentAdapter.this.remove(values.get(position));
                Utility.setListViewHeightBasedOnChildren((ListView) parent);
            }
        });

        return rootView;
    }

    public List<Comment> getValues(){
        return values;
    }



}
