package com.irahavoi.qrioscat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.irahavoi.qrioscat.R;
import com.irahavoi.qrioscat.data.ArtworkProvider;
import com.irahavoi.qrioscat.domain.Artwork;
import com.irahavoi.qrioscat.view.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Exposes a list of artworks.
 * from {@link android.database.Cursor} to  {@link android.support.v7.widget.RecyclerView}
 */
public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkAdapter.ArtworkAdapterViewHolder>{

    private List<Artwork> mArtworks;
    //private Cursor mCursor;
    final private Context mContext;
    final private ArtworkAdapterOnClickHandler mClickHandler;
    final private View mEmptyView;

    public class ArtworkAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public Long id;
        public final RoundedImageView mImageView;
        public final TextView mName;
        public final TextView mAuthor;
        public final ImageView mDelete;

        public ArtworkAdapterViewHolder(final View itemView) {
            super(itemView);
            mImageView = (RoundedImageView) itemView.findViewById(R.id.list_icon);
            mName = (TextView) itemView.findViewById(R.id.artwork_name);
            mAuthor = (TextView) itemView.findViewById(R.id.artwork_author);
            mDelete = (ImageView) itemView.findViewById(R.id.delete_artwork);

            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.getContentResolver().delete(ArtworkProvider.CONTENT_URI, "_ID = ?", new String[]{String.valueOf(id)});
                    mArtworks.remove(ArtworkAdapterViewHolder.this.getLayoutPosition());
                    notifyItemRemoved(ArtworkAdapterViewHolder.this.getLayoutPosition());
                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mArtworks.get(adapterPosition), this);
        }
    }

    public interface ArtworkAdapterOnClickHandler{
        void onClick(Artwork artwork, ArtworkAdapterViewHolder vh);
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
        Artwork artwork = mArtworks.get(position);

        artworkAdapterViewHolder.id = artwork.getId();
        artworkAdapterViewHolder.mAuthor.setText(artwork.getAuthor());
        artworkAdapterViewHolder.mName.setText(artwork.getName());

        Picasso.with(mContext).load(artwork.getImageUrl())
                .into(artworkAdapterViewHolder.mImageView);

    }

    @Override
    public int getItemCount() {
        if ( null == mArtworks ) return 0;
        return mArtworks.size();
    }

    public void swapArtworks(List<Artwork> artworks) {
        mArtworks = artworks;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    public List<Artwork> getArtworks() {
        return mArtworks;
    }

}
