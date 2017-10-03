package com.caraquri.android_bookmanager;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;

public class ImageUtil {

    // Bitmap画像をBase64にエンコード
    public static String encodeToBase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream); // 画像は高圧縮にする(画像が大きすぎると保存失敗率大のため)
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

    // 書籍のサムネイルで規格が合うように設定(読込中や読込失敗時に表示する画像の表示もここで行う)
    public static RequestOptions getRequestOptionsOfBookThumbnail() {
        RequestOptions requestOptions = new RequestOptions()
                .override(175, 248)
                .centerCrop()
                .placeholder(R.raw.now_loading)
                .error(R.drawable.ic_load_error)
                .priority(Priority.HIGH);

        return requestOptions;
    }

}
