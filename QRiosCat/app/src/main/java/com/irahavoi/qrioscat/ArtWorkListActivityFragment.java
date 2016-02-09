package com.irahavoi.qrioscat;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArtWorkListActivityFragment extends Fragment {
    private  RecyclerView mRecyclerView;


    public ArtWorkListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_art_work_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_artworklist);

        //Setting the layout manager:
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        View emptyPlaceholder = rootView.findViewById(R.id.empty_artworks_list);

        //We know that the size of the recycler view is not going to change. The following improves performance.
        mRecyclerView.setHasFixedSize(true);




        return rootView;
    }
}
