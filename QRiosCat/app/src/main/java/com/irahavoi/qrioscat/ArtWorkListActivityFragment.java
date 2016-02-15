package com.irahavoi.qrioscat;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.irahavoi.qrioscat.adapter.ArtworkAdapter;
import com.irahavoi.qrioscat.data.ArtworkProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArtWorkListActivityFragment extends Fragment {
    private ArtworkAdapter mArtworkAdapter;
    private  RecyclerView mRecyclerView;
    private FloatingActionButton mStartScanButton;


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

        mArtworkAdapter = new ArtworkAdapter(getActivity(), new ArtworkAdapter.ArtworkAdapterOnClickHandler() {
            @Override
            public void onClick() {
                //TODO;
            }
        }, emptyPlaceholder);

        mRecyclerView.setAdapter(mArtworkAdapter);

        //We know that the size of the recycler view is not going to change. The following improves performance.
        mRecyclerView.setHasFixedSize(true);


        mStartScanButton =  (FloatingActionButton) rootView.findViewById(R.id.start_scan_button);

        mStartScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent captureBarcode = new Intent(getActivity(), BarcodeCaptureActivity.class);
                startActivityForResult(captureBarcode, BarcodeCaptureActivity.CAPTURE_BARCODE_REQUEST);
            }
        });

        getArtworks();


        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BarcodeCaptureActivity.CAPTURE_BARCODE_REQUEST && CommonStatusCodes.SUCCESS == resultCode && data != null){
            Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

            GetArtworkDataTask task = new GetArtworkDataTask();
            task.execute(barcode.rawValue);

        } else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public class GetArtworkDataTask extends AsyncTask<String, Void, List<Artwork>> {
        private final String BASE_URL = "http://162.243.247.234:3000";
        private Retrofit retrofit;
        private QriosWebService webService;

        @Override
        protected void onPreExecute(){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            webService = retrofit.create(QriosWebService.class);
        }

        @Override
        protected void onPostExecute(List<Artwork> artworks){
            Toast.makeText(getActivity(), artworks.get(0).getName(), Toast.LENGTH_LONG).show();
            getArtworks();
        }

        @Override
        protected List<Artwork> doInBackground(String... artworkIds) {
            List<Artwork> artworks =  new ArrayList<>();

            for(String artworkId : artworkIds){
                Call<Artwork> call = webService.getArtwork(artworkId);

                try{
                    Artwork artwork = call.execute().body();
                    saveArtwork(artwork);
                    artworks.add(artwork);

                } catch (IOException e){
                    Toast.makeText(getActivity(), "Error occurred when trying to contact the server", Toast.LENGTH_LONG).show();
                }
            }

            return artworks;
        }
    }

    private void saveArtwork(Artwork artwork){
        ContentValues contentValues = new ContentValues();

        contentValues.put(ArtworkProvider.NAME, artwork.getName());
        contentValues.put(ArtworkProvider.AUTHOR, artwork.getAuthor());
        contentValues.put(ArtworkProvider.DESCRIPTION, artwork.getDescription());
        contentValues.put(ArtworkProvider.IMAGE_URL, artwork.getImageUrl());

        Uri uri = getActivity().getContentResolver().insert(ArtworkProvider.CONTENT_URI, contentValues);

    }

    private void getArtworks(){
        Cursor cursor = getActivity().getContentResolver().query(ArtworkProvider.CONTENT_URI, null, null, null, null);

        mArtworkAdapter.swapCursor(cursor);
    }
}
