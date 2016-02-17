package com.irahavoi.qrioscat.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by irahavoi on 2016-02-12.
 */
public class Artwork implements Parcelable{
    private Long id;
    private String name;
    private String author;
    private String description;
    private String imageUrl;

    public Artwork(){}

    protected Artwork(Parcel in) {
        id = in.readLong();
        name = in.readString();
        author = in.readString();
        description = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Artwork> CREATOR = new Creator<Artwork>() {
        @Override
        public Artwork createFromParcel(Parcel in) {
            return new Artwork(in);
        }

        @Override
        public Artwork[] newArray(int size) {
            return new Artwork[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(author);
        parcel.writeString(description);
        parcel.writeString(imageUrl);
    }
}
