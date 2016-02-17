package com.irahavoi.qrioscat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.irahavoi.qrioscat.domain.Artwork;
import com.squareup.picasso.Picasso;

public class ArtworkDetailActivityFragment extends Fragment {
    private Artwork mArtwork;
    private TextView mHeader;
    private TextView mDescriptionText;
    private ImageView mImage;

    public ArtworkDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        mArtwork = intent.getParcelableExtra("artwork");

        View layout = inflater.inflate(R.layout.fragment_artwork_detail, container, false);

        mHeader = (TextView) layout.findViewById(R.id.artwork_header);
        mDescriptionText = (TextView) layout.findViewById(R.id.description);
        mImage = (ImageView) layout.findViewById(R.id.image);

        mDescriptionText.setText(mArtwork.getDescription());
        mHeader.setText(mArtwork.getName() + ", " + mArtwork.getAuthor());

        Picasso.with(getActivity()).load(mArtwork.getImageUrl())
                .into(mImage);


        return layout;
    }
}
