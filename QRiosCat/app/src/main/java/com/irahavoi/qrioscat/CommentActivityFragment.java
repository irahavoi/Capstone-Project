package com.irahavoi.qrioscat;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.irahavoi.qrioscat.data.ArtworkProvider;
import com.irahavoi.qrioscat.domain.Artwork;

/**
 * A placeholder fragment containing a simple view.
 */
public class CommentActivityFragment extends Fragment {
    EditText mComment;

    public CommentActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Artwork artwork = getActivity().getIntent().getParcelableExtra("artwork");

        Uri.Builder builder = new Uri.Builder();

        Uri uri = builder.scheme("content")
                .authority(ArtworkProvider.PROVIDER_NAME)
                .appendPath("artworks")
                .appendPath(String.valueOf(artwork.getId()))
                .appendPath("comments")
                .build();

        Cursor cursor = getActivity().getContentResolver()
                .query(uri, null, ArtworkProvider._ARTWORK_ID + " = ?", new String[]{String.valueOf(artwork.getId())}, null);

        View layout = inflater.inflate(R.layout.fragment_comment, container, false);

        mComment =  (EditText) layout.findViewById(R.id.comment);
        mComment.requestFocus();

        return layout;
    }
}
