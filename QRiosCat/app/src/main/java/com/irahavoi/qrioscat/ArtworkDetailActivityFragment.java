package com.irahavoi.qrioscat;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.irahavoi.qrioscat.adapter.CommentAdapter;
import com.irahavoi.qrioscat.data.ArtworkProvider;
import com.irahavoi.qrioscat.domain.Artwork;
import com.irahavoi.qrioscat.domain.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ArtworkDetailActivityFragment extends Fragment {
    private Artwork mArtwork;
    private TextView mHeader;
    private TextView mDescriptionText;
    private ImageView mImage;
    private ImageView mComment;

    List<Comment> mComments;

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


        mComment = (ImageView) layout.findViewById(R.id.comment_btn);
        mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentActivityIntent = new Intent(getActivity(), CommentActivity.class);
                commentActivityIntent.putExtra("artwork", mArtwork);
                startActivity(commentActivityIntent);
            }
        });


        mComments = getComments();

        ListView commentsListView = (ListView) layout.findViewById(R.id.comment_listview);
        CommentAdapter commentAdapter = new CommentAdapter(getActivity(), mComments);
        commentsListView.setAdapter(commentAdapter);
        commentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "TODO:", Toast.LENGTH_LONG);
            }
        });


        return layout;
    }

    private List<Comment> getComments(){
        Uri.Builder builder = new Uri.Builder();
        Uri uri = builder.scheme("content")
                .authority(ArtworkProvider.PROVIDER_NAME)
                .appendPath("artworks")
                .appendPath(String.valueOf(mArtwork.getId()))
                .appendPath("comments")
                .build();

        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null, null);

        List<Comment> comments = new ArrayList<>();
        while(cursor.moveToNext()){
            Comment comment = new Comment();
            comment.setId(cursor.getLong(ArtworkProvider.COL_ID));
            comment.setArtworkId(cursor.getLong(ArtworkProvider.COL_ARTWORK_ID));
            comment.setComment(cursor.getString(ArtworkProvider.COL_COMMENT));
            comments.add(comment);
        }

        return comments;

    }
}
