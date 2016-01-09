package com.yike.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import com.yike.utils.GeneralTool;
import com.yike.utils.LocalCacheUtil;
import com.yike.utils.MediaUtil;

import java.io.*;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Administrator
 * @destribe 2014-4-9
 */
public class PictureTool {
    public static int WINDOW_WIDTH = 640;
    public static int WINDOW_HEIGHT = 850;
    private static Map<String, String> chcheFileData = new Hashtable<String, String>();
    // 本地压缩过的图片缓存
    private static HashSet<String> values = new HashSet();
    private static String tag = "PictureTool";

    static {
        // 检测本地缓存
        String[] copresList = LocalCacheUtil.cacheFilePath.list();
        if (null != copresList && copresList.length != 0) {
            for (String string : copresList) {
                values.add(string);
            }
        }
    }

    /**
     * 计算图片的缩放值
     *
     * @param options   屏幕的 高度 和宽度
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        // 取一个较小的值
        // int temp;
        // if(reqWidth>reqHeight){
        // temp=reqHeight;
        // reqHeight=reqWidth;
        // reqWidth=temp;
        // }
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 根据路径获得并压缩返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        if (isSmallPicture(filePath)) {
            return BitmapFactory.decodeFile(filePath);
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 100, 70);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 根据路径获得并压缩返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int width, int height) {
        if (isSmallPicture(filePath)) {
            return BitmapFactory.decodeFile(filePath);
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 根据路径获得并压缩返回bitmap用于显示
     *
     * @param filePath 图片的原路径
     * @return 得到小于 50Kb 的Bitmap
     */
    public static Bitmap getCompressedBitmap(String filePath) {
        if (GeneralTool.isEmpty(filePath)) {
            return null;
        }
        if (isSmallPicture(filePath)) {
            return BitmapFactory.decodeFile(filePath);
        }
        // 检测并加载 已有数据
        String name = filePath.substring(filePath.lastIndexOf("/") + 1);
        if (values.contains(name)) {
            String cahcheFilename = MediaUtil.createCacehFile() + name;
            chcheFileData.put(filePath, cahcheFilename);
            return BitmapFactory.decodeFile(chcheFileData.get(filePath));
        }

        if (chcheFileData.containsKey(filePath)) {
            return BitmapFactory.decodeFile(chcheFileData.get(filePath));
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 100, 150);
        options.inJustDecodeBounds = false;
        Bitmap btp = BitmapFactory.decodeFile(filePath, options);
        return compressImage(btp, filePath);
    }

    private static boolean isSmallPicture(String filePath) {
        InputStream staram = null;
        File tempFile = new File(filePath);
        // 判断文件是否存在
        if (!tempFile.exists()) {
            return true;
        }
        try {
            staram = new FileInputStream(filePath);
            if (staram.available() < 51200) {
                staram.close();
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 将图片压缩到 500K--1000K之间 上传daofuwuq
     *
     * @param file
     */
    public void scaleLargePicture(String file) {

    }

    /**
     * @param image
     * @param filePath
     * @return
     */
    private static Bitmap compressImage(Bitmap image, String filePath) {
        // 如果用户选择 的不是 图片
        String name = filePath.substring(filePath.lastIndexOf("/") + 1);
        String cahcheFilename = MediaUtil.createCacehFile() + name;
        File tempFile = new File(filePath);
        FileOutputStream baos = null;
        InputStream staram = null;
        try {
            // 判断文件是否存在
            if (!tempFile.exists()) {
                return null;
            }
            staram = new FileInputStream(filePath);
            baos = new FileOutputStream(cahcheFilename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int options = 90;
        try {
            while (staram.available() / 1024 > 100) {
                // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;// 每次都减少10
                staram.reset();
            }
            staram.close();
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chcheFileData.put(filePath, cahcheFilename);
        return BitmapFactory.decodeFile(cahcheFilename);
    }

    /**
     * 添加到图库
     */
    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 将图片存到本地
     *
     * @param is
     * @param targetFile
     * @return
     */
    public static String savePicture(InputStream is, String targetFile) {
        OutputStream os = null;
        byte[] bt = new byte[1024];
        int len;
        File file = new File(targetFile);
        if (!file.isFile()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os = new FileOutputStream(file);
            while ((len = is.read(bt)) != -1) {
                os.write(bt, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return targetFile;
    }

    public static boolean copy(String fileFrom, String fileTo) {
        try {
            FileInputStream in = new java.io.FileInputStream(fileFrom);
            FileOutputStream out = new FileOutputStream(fileTo);
            byte[] bt = new byte[1024];
            int count;
            while ((count = in.read(bt)) > 0) {
                out.write(bt, 0, count);
            }
            in.close();
            out.flush();
            out.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * 根据绝对路径
     *
     * @param path
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static String storagePicture(Bundle extras) {
        /** 储存拍照留下来的图片 **/
        String fileName = null;
        Bitmap bitmap = (Bitmap) extras.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
        FileOutputStream b = null;
        fileName = MediaUtil.createPictureFile();
        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * @param context
     * @param uri     调用系统的 相册有时候是 数据库的 id 有时候是.jpg
     * @return 图片的路径String
     */
    public static String getPicturePath(Activity context, Uri uri) {
        String temp = uri.getPath();
        if (GeneralTool.isEmpty(temp))
            return null;
        // .jpg
        if (temp.endsWith(".jpg") || temp.endsWith(".png")
                || temp.equals(".jpeg")) {
            return temp;
        }
        // 如果用户选择 的不是 图片
        String name = temp.substring(temp.lastIndexOf("/") + 1);
        // 匹配文字
        if (name.matches("\\d+")) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex;
            columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            temp = cursor.getString(columnIndex);
            cursor.close();
            Log.e("getPicturePath_:", temp);
            return temp;
        }
        return null;
    }

    /**
     * @param filePath
     * @param kb       不需要 压缩的 最低值
     * @return 返回 压缩之后的图片 地址 根据当前手机的 高度和宽度 压缩
     */
    public static String compressAndSaveImage(String filePath, int kb) {
        // 如果用户选择 的不是 图片
        String name = filePath.substring(filePath.lastIndexOf("/") + 1);
        String cacheFilename = MediaUtil.createCacehFile() + name;
        File tempFile = new File(filePath);
        if (tempFile.length() < kb * 1024 && tempFile.length() > 0) {
            Log.d(tag, "图片不需要压缩" + filePath);
            return filePath;
        }
        final BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, option);
        option.inSampleSize = calculateInSampleSize(option, WINDOW_WIDTH,
                WINDOW_HEIGHT);
        option.inJustDecodeBounds = false;
        Bitmap image = BitmapFactory.decodeFile(filePath, option);

        FileOutputStream baos = null;
        try {
            // 判断文件是否存在
            if (!tempFile.exists()) {
                return null;
            }
            baos = new FileOutputStream(cacheFilename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int options = Math.round(100 / option.inSampleSize);
        try {
            if (image.getWidth() > 480 || image.getHeight() > 600) {
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            }
            baos.flush();
            baos.close();
            image.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chcheFileData.put(filePath, cacheFilename);
        return cacheFilename;
    }

}
