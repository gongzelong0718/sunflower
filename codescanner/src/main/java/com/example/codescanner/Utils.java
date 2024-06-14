/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.codescanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.alipay.mobile.common.logging.api.LoggerFactory;

import java.io.InputStream;

/**
 * Created by xingcheng on 2018/8/10.
 */

public class Utils {

    private static String TAG = "Utils";

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static Bitmap changeBitmapColor(Bitmap bitmap, int color) {
        int bitmap_w = bitmap.getWidth();
        int bitmap_h = bitmap.getHeight();
        int[] arrayColor = new int[bitmap_w * bitmap_h];

        int count = 0;
        for (int i = 0; i < bitmap_h; i++) {
            for (int j = 0; j < bitmap_w; j++) {

                int originColor = bitmap.getPixel(j, i);
                // 非透明区域
                if (originColor != 0) {
                    originColor = color;
                }

                arrayColor[count] = originColor;
                count++;
            }
        }
        return Bitmap.createBitmap(arrayColor, bitmap_w, bitmap_h, Bitmap.Config.ARGB_8888);
    }

    public static Bitmap uri2Bitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        InputStream in;
        try {
            in = context.getContentResolver().openInputStream(uri);
            if (in != null) {
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            }
        } catch (Exception e) {
//            LoggerFactory.getTraceLogger().error(TAG, "uri2Bitmap: Exception " + e.getMessage());
            Log.e(TAG, "uri2Bitmap: Exception " + e.getMessage());
        }
        return bitmap;
    }
}
