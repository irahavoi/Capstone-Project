package com.irahavoi.qrioscat;


import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface QriosWebService {
    @GET("artworks/{artworkId}")
    Call<Artwork> getArtwork(@Path("artworkId") String artworkId);
}
