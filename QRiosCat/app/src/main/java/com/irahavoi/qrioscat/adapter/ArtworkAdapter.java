package com.irahavoi.qrioscat.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.irahavoi.qrioscat.R;
import com.irahavoi.qrioscat.data.ArtworkProvider;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

/**
 * Exposes a list of artworks.
 * from {@link android.database.Cursor} to  {@link android.support.v7.widget.RecyclerView}
 */
public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkAdapter.ArtworkAdapterViewHolder>{

    private Cursor mCursor;
    final private Context mContext;
    final private ArtworkAdapterOnClickHandler mClickHandler;
    final private View mEmptyView;

    public class ArtworkAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public Long id;
        public final CircularImageView mImageView;
        public final TextView mName;
        public final TextView mAuthor;
        public final ImageView mDelete;

        public ArtworkAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = (CircularImageView) itemView.findViewById(R.id.list_icon);
            mName = (TextView) itemView.findViewById(R.id.artwork_name);
            mAuthor = (TextView) itemView.findViewById(R.id.artwork_author);
            mDelete = (ImageView) itemView.findViewById(R.id.delete_artwork);

            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.getContentResolver().delete(ArtworkProvider.CONTENT_URI, "_ID = ?", new String[]{String.valueOf(id)});
                    Cursor cursor = mContext.getContentResolver().query(ArtworkProvider.CONTENT_URI, null, null, null, null);
                    ArtworkAdapter.this.swapCursor(cursor);
                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onClick(mCursor.getLong(ArtworkProvider.COL_ID), this);
        }
    }

    public interface ArtworkAdapterOnClickHandler{
        void onClick(Long id, ArtworkAdapterViewHolder vh);
    }

    public ArtworkAdapter(Context context, ArtworkAdapterOnClickHandler clickHandler, View emptyView){
        mContext = context;
        mClickHandler = clickHandler;
        mEmptyView = emptyView;
    }

    @Override
    public ArtworkAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewGroup instanceof RecyclerView){
            int layoutId = R.layout.list_item_artwork;

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            view.setFocusable(true);

            return new ArtworkAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to Recycler View");
        }
    }

    @Override
    public void onBindViewHolder(ArtworkAdapterViewHolder artworkAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);

        artworkAdapterViewHolder.id = mCursor.getLong(ArtworkProvider.COL_ID);
        artworkAdapterViewHolder.mAuthor.setText(mCursor.getString(ArtworkProvider.COL_AUTHOR));
        artworkAdapterViewHolder.mName.setText(mCursor.getString(ArtworkProvider.COL_NAME));

        Picasso.with(mContext).load(mCursor.getString(ArtworkProvider.COL_IMAGE_URL))
                .into(artworkAdapterViewHolder.mImageView);

    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public Cursor getCursor() {
        return mCursor;
    }

}
