package com.irahavoi.qrioscat;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.irahavoi.qrioscat.data.ArtworkProvider;
import com.irahavoi.qrioscat.domain.Artwork;

/**
 * A placeholder fragment containing a simple view.
 */
public class CommentActivityFragment extends Fragment {
    private EditText mComment;
    private ImageView mPostComment;
    private Artwork mArtwork;

    public CommentActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mArtwork = getActivity().getIntent().getParcelableExtra("artwork");

        View layout = inflater.inflate(R.layout.fragment_comment, container, false);

        mComment =  (EditText) layout.findViewById(R.id.comment);
        mComment.requestFocus();

        mPostComment = (ImageView) layout.findViewById(R.id.post_comment);

        mPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mComment.getText().length() == 0){
                    return;
                }

                Uri.Builder builder = new Uri.Builder();

                Uri uri = builder.scheme("content")
                    .authority(ArtworkProvider.PROVIDER_NAME)
                        .appendPath("artworks")
                        .appendPath(String.valueOf(mArtwork.getId()))
                        .appendPath("comments")
                        .build();

                ContentValues values = new ContentValues();
                values.put(ArtworkProvider._ARTWORK_ID, mArtwork.getId());
                values.put(ArtworkProvider.COMMENT, mComment.getText().toString());

                getActivity().getContentResolver()
                        .insert(uri, values);

                CommentActivityFragment.this.getActivity().finish();
            }
        });

        return layout;
    }
}
