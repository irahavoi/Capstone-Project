package com.irahavoi.qrioscat.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.irahavoi.qrioscat.R;
import com.irahavoi.qrioscat.data.ArtworkProvider;
import com.irahavoi.qrioscat.domain.Artwork;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UpdateWidgetService extends IntentService{
    public UpdateWidgetService(){
        super("UpdateWidgetService");
    }

    public UpdateWidgetService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Artwork artwork = getLastArtworks();

        if(artwork != null){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                    .getApplicationContext());

            int[] allWidgetIds = intent
                    .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

            for (int widgetId : allWidgetIds) {
                final RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget_layout);

                remoteViews.setImageViewBitmap(R.id.artwork_image, getImageBitmap(artwork.getImageUrl()));
                remoteViews.setTextViewText(R.id.widget_artwork_name, artwork.getName());
                appWidgetManager.updateAppWidget(widgetId, remoteViews);

            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Artwork getLastArtworks(){

        Cursor cursor = getContentResolver().query(ArtworkProvider.CONTENT_URI_ARTWORK,
                null,
                null,
                null,
                ArtworkProvider._ID + " DESC limit 1" );

        Artwork artwork = null;

        if(cursor.getCount() == 1){
            cursor.moveToFirst();
            artwork = new Artwork();
            artwork.setId(cursor.getLong(ArtworkProvider.COL_ID));
            artwork.setName(cursor.getString(ArtworkProvider.COL_ARTWORK_NAME));
            artwork.setAuthor(cursor.getString(ArtworkProvider.COL_ARTWORK_AUTHOR));
            artwork.setDescription(cursor.getString(ArtworkProvider.COL_ARTWORK_DESCRIPTION));
            artwork.setImageUrl(cursor.getString(ArtworkProvider.COL_ARTWORK_IMAGE_URL));
        }

        return artwork;

    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (Exception e) {
            Log.e(UpdateWidgetService.class.getName(), "Error getting bitmap", e);
        }
        return bm;
    }


}
