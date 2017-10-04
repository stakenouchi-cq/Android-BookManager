package com.caraquri.android_bookmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

    private static final String LOG_TAG = ImageUtil.class.getSimpleName();

    // Bitmap画像をBase64にエンコード
    public static String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream); // 画像は高圧縮にする(画像が大きすぎると保存失敗率大のため)
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String imageEncoded = Base64.encodeToString(bytes, Base64.NO_WRAP);
        return imageEncoded;
    }

    // ストレージが読み出し可能かどうかをチェック
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    // Assetsにある画像をbitmapへ変換
    public static Bitmap getBitmapFromAssets(Context context, String imageUrl) {
        try {
            InputStream inputStream = context.getAssets().open(imageUrl);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (IOException e) {
            Log.e(LOG_TAG, Constants.LogMessages.CONVERT_TO_BITMAP_FROM_ASSETS, e);
            return null;
        }
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

}
