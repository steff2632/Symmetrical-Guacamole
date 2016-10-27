package com.example.symmetrical_guacamole;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Riad on 20/05/2015.
 */
public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();

    public static void getImage(Context context, String url, ImageView imageView) {

        if(isExternalStorageReadable()) {
            File externalFilesDir = context.getExternalFilesDir(null);

            File image = new File(externalFilesDir, url.substring(url.lastIndexOf('/')));



            try {

                if(image.exists()) {
                    InputStream is = new FileInputStream(image);

                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                } else {
                    Intent dlImages = new Intent(context, InternetService.class);
                    dlImages.setData(Uri.parse(InternetService.Actions.GET_BYTES));
                    dlImages.putExtra(InternetService.Extras.URL_EXTRA, url);
//                    dlImages.putExtra(InternetService.Extras.ITEM_EXTRA, myItem);

                    context.startService(dlImages);
                }

                OutputStream os = new FileOutputStream(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public ImageLoader() { /**/ }

    public static Bitmap convertToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    private static void setImageView(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    public class BitmapBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra(InternetService.Extras.BYTES_EXTRA)) {
                byte[] bytes = intent.getByteArrayExtra(InternetService.Extras.BYTES_EXTRA);


            }
        }
    }
}
