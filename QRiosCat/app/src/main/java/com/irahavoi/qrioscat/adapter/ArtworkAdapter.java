package com.irahavoi.qrioscat.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.irahavoi.qrioscat.R;

/**
 * Exposes a list of artworks.
 * from {@link android.database.Cursor} to  {@link android.support.v7.widget.RecyclerView}
 */
public class ArtworkAdapter extends RecyclerView.ViewHolder{

    Cursor mCursor;

    public ArtworkAdapter(View itemView) {
        super(itemView);
    }

    public class ArtworkAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView mIconView;
        public final TextView mName;
        public final TextView mAuthor;

        public ArtworkAdapterViewHolder(View itemView) {
            super(itemView);
            mIconView = (ImageView) itemView.findViewById(R.id.list_icon);
            mName = (TextView) itemView.findViewById(R.id.artwork_name);
            mAuthor = (TextView) itemView.findViewById(R.id.artwork_author);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mCursor.moveToPosition(getAdapterPosition());
            //TODO: implement me
        }
    }
}
