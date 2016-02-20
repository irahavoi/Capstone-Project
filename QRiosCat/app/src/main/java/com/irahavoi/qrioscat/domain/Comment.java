package com.irahavoi.qrioscat.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable{
    private Long id;
    private Long artworkId;
    private String comment;

    public Comment(){}

    protected Comment(Parcel in) {
        id = in.readLong();
        artworkId = in.readLong();
        comment = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArtworkId() {
        return artworkId;
    }

    public void setArtworkId(Long artworkId) {
        this.artworkId = artworkId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(artworkId);
        parcel.writeString(comment);

    }
}
