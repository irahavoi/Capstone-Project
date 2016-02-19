package com.irahavoi.qrioscat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.irahavoi.qrioscat.R;
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
    public View getView(int position, View convetView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rootView = inflater.inflate(R.layout.comment_item, parent, false);
        TextView comment = (TextView) rootView.findViewById(R.id.comment);
        comment.setText(values.get(position).getComment());

        return rootView;
    }



}
