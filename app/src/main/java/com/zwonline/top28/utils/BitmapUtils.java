package com.zwonline.top28.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片压缩
 * Created by sdh on 2018/3/16.
 */

public class BitmapUtils {
    private ImageView image;

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            //首先获取原图高度和宽度的一半
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            //循环，如果halfHeight和halfWidth同时大于最小宽度和最小高度时，inSampleSize乘2，
            //最后得到的宽或者高都是最接近最小宽度或者最小高度的
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 根据Resources压缩图片
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeResource(res, resId, options);
        return src;
    }

    /**
     * 根据地址压缩图片
     *
     * @param pathName
     * @param reqWidth  最小宽度
     * @param reqHeight 最小高度
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFd(String pathName, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 若要对图片进行压缩，必须先设置Option的inJustDecodeBounds为true才能进行Option的修改
        options.inJustDecodeBounds = true;
        // 第一次decodeFile是获取到options.outHeight和options.outWidth
        BitmapFactory.decodeFile(pathName, options);
        // options.inSampleSize是图片的压缩比，例如原来大小是100*100，options.inSampleSize为1，则不变，
        // options.inSampleSize为2，则压缩成50*50
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 重新设置options.inJustDecodeBounds为false，不能修改option
        options.inJustDecodeBounds = false;
        // 根据options重新加载图片
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return src;
    }


    //图片的二次采样
    public static Bitmap getBitmap(String filePath, int destWidth, int destHeight) {
        //第一次采样
        BitmapFactory.Options options = new BitmapFactory.Options();
        //加载边框
        options.inJustDecodeBounds = true;
        //返回
        BitmapFactory.decodeFile(filePath, options);
        //获得原图的宽和高
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        //定义缩放比例
        int sampleSize = 1;
        while (outHeight / sampleSize > destHeight || outWidth / sampleSize > destWidth) {
            //如果宽高的任意一方的缩放比例没有达到要求，都继续增大缩放比例
            //sampleSize应该为2的n次幂，如果给sampleSize设置的数字不是2的n次幂，那么系统会就近取值
            sampleSize *= 2;//计算新的sampleSize大小
        }
        /********************************************************************************************/
        //至此，第一次采样已经结束，我们已经成功的计算出了sampleSize的大小
        /********************************************************************************************/
        //二次采样开始
        //二次采样时我需要将图片加载出来显示，不能只加载图片的框架，因此inJustDecodeBounds属性要设置为false
        options.inJustDecodeBounds = false;
        //设置缩放比例
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //加载图片并返回
        return BitmapFactory.decodeFile(filePath, options);

    }

    /**
     * 质量压缩
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        //循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 200) {
            //重置baos即清空baos
            baos.reset();
            //这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            //每次都减少10
            options -= 10;
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }




    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）
     *
     * @param srcPath
     * @return
     */
    private static Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1080f;// 这里设置高度为800f
        float ww = 720f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 将压缩的bitmap保存到SDCard卡临时文件夹，用于上传
     *
     * @param filename
     * @param bit
     * @return
     */
    private static String saveMyBitmap(String filename, Bitmap bit) {
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/chdd/";
        String filePath = baseDir + filename;
        File dir = new File(baseDir);
        if (!dir.exists()) {
            dir.mkdir();
        }

        File f = new File(filePath);
        try {
            f.createNewFile();
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(f);
            bit.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return filePath;
    }

    /**
     * 压缩上传路径
     *
     * @param path
     * @return
     */
    public static String compressImageUpload(String path) {
        String filename = path.substring(path.lastIndexOf("/") + 1);
        Bitmap image = getImage(path);
        return saveMyBitmap(filename, image);
    }
}
