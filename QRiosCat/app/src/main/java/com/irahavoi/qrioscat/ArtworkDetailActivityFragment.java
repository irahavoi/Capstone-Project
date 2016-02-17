package com.irahavoi.qrioscat;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irahavoi.qrioscat.data.ArtworkProvider;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArtworkDetailActivityFragment extends Fragment {

    public ArtworkDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        Long id = intent.getLongExtra("artworkId", -1L);

        Cursor cursor = getActivity().getContentResolver().query(ArtworkProvider.CONTENT_URI, null, "_ID = ?", new String[]{String.valueOf(id)}, "");
        cursor.moveToFirst();

        return inflater.inflate(R.layout.fragment_artwork_detail, container, false);
    }
}
