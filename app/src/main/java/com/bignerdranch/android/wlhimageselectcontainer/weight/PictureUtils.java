package com.bignerdranch.android.wlhimageselectcontainer.weight;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/7/17/017.
 */

public class PictureUtils {
    private static Bitmap getScaleBitmap(String path, int destWidth, int destHeight) {
        // Read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        //allowing the caller to query
        //允许调用者
        //the bitmap without having to allocate the memory for its pixels.
        //在外面去分配一个bitmap的pixels内存大小
        //（adv副词修饰动词bitmap修饰having to）
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        //it will be the width of the input image without any accounting for scaling.
        //那将是输入的不包括任何缩放比例叙述的图片
        int width = options.outWidth;
        int height = options.outHeight;
        int scale = 1;
        if (destWidth < width || width < height) {
            float widthScale = options.outWidth / destWidth;
            float heightScale = options.outHeight / destHeight;
            //Returns the closest {@code int} to the argument, with ties rounding to positive infinity.
            //返回输入参数最接近的整数，整数支持约束到把正无穷变成整数
            scale = Math.round(Math.max(widthScale, heightScale));
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = scale;
        return BitmapFactory.decodeFile(path, options);
    }

    public static void showImages(final String path, final ImageView view) {
        ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getScaleBitmap(path, view.getMaxWidth(), view.getMaxHeight());
            }
        });
    }
}
