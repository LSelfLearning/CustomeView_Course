package com.lewish.start.roundprogressbartest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Utils extends BaseUtils {

    public static String getDecimalFormat(String str) {
        DecimalFormat fmt = new DecimalFormat("0.00");
        String outStr = null;
        double d;
        if (StringUtil.isEmpty(str)) {
            return "";
        }
        try {
            d = Double.parseDouble(str) / 100;
            outStr = fmt.format(d);
        } catch (Exception e) {
        }
        return outStr;
    }

    public static String checkNull(String check) {
        if (StringUtil.isNotEmpty(check)) {
            return getDecimalFormat(check) + "元";
        } else {
            return "";
        }
    }

    public static String checkNull(Object check) {

        String str = String.valueOf(check);
        if (StringUtil.isNotEmpty(str)) {
            return getDecimalFormat(str) + "元";
        } else {
            return "";
        }
    }

    public static String checkNullExclusionUnit(Object check) {

        String str = String.valueOf(check);
        if (StringUtil.isNotEmpty(str)) {
            return getDecimalFormat(str);
        } else {
            return "";
        }
    }

    public static String checkNullPercent(String check) {
        if (StringUtil.isNotEmpty(check)) {
            return getDecimalFormat(check) + "%";
        } else {
            return "";
        }
    }

    public static String formatDouble(double f) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (f < 1) {
            return "0" + df.format(f);
        } else {
            return df.format(f);
        }
    }

    public static String formatDouble(String str) {
        DecimalFormat fmt = new DecimalFormat("0.00");
        String outStr = null;
        double d;
        if (StringUtil.isEmpty(str)) {
            return "";
        }
        try {
            d = Double.parseDouble(str);
            outStr = fmt.format(d);
        } catch (Exception e) {
        }
        return outStr;
    }

    /**
     * @param value
     * @param defaultValue
     * @return integer
     * @throws
     * @Title: convertToInt
     * @Description: 对象转化为整数数字类型
     */
    public final static int convertToInt(Object value, int defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }


    /**
     * 输入的字符是否是汉字
     *
     * @param a char
     * @return boolean
     */
    private static boolean isChinese(char a) {
        int v = (int) a;
        return (v >= 19968 && v <= 171941);
    }

    public static boolean containsChinese(String s) {
        if (null == s || "".equals(s.trim())) return false;
        for (int i = 0; i < s.length(); i++) {
            if (isChinese(s.charAt(i))) return true;
        }
        return false;
    }

    /**
     * 获得屏幕高度
     */
    public static int getScreenWidth(Context context) {

        WeakReference<Context> contextWeakReference = new WeakReference<>(context);

        WindowManager wm = (WindowManager) contextWeakReference.get().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     */
    public static int getScreenHeigth(Context context) {

        WeakReference<Context> contextWeakReference = new WeakReference<>(context);

        WindowManager wm = (WindowManager) contextWeakReference.get().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getMetrics(Context context) {

        WeakReference<Context> contextWeakReference = new WeakReference<>(context);

        DisplayMetrics dm = contextWeakReference.get().getResources().getDisplayMetrics();
        return dm.densityDpi;
    }

    /**
     * @param activity
     * @param intent
     * @return
     */
    public static boolean isIntentSafe(Activity activity, Intent intent) {

        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);

        PackageManager packageManager = activityWeakReference.get().getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(
                intent, 0);
        return activities.size() > 0;
    }

    public static String getPicPathFromUri(Uri uri, Activity activity) {

        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);

        String value = uri.getPath();

        if (value.startsWith("/external")) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = activityWeakReference.get().managedQuery(uri, proj, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else {
            return value;
        }
    }

    /**
     * 判断图片旋转情况
     *
     * @param path
     * @return 判断图片旋转情况
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {

        WeakReference<Context> contextWeakReference = new WeakReference<>(context);

        final float scale = contextWeakReference.get().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context activity, float spValue) {

        WeakReference<Context> contextWeakReference = new WeakReference<>(activity);

        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, contextWeakReference.get().getResources().getDisplayMetrics());
        return px;
    }

    public static String getVersionName(Context context) {

        WeakReference<Context> contextWeakReference = new WeakReference<>(context);

        String pkName = contextWeakReference.get().getPackageName();
        String versionName = null;
        try {
            versionName = contextWeakReference.get().getPackageManager().getPackageInfo(pkName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static int getVersionCode(Context context) {

        WeakReference<Context> contextWeakReference = new WeakReference<>(context);

        String pkName = contextWeakReference.get().getPackageName();
        int versionCode = -1;
        try {
            versionCode = contextWeakReference.get().getPackageManager().getPackageInfo(pkName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String readRaw(Context c, int id) {

        WeakReference<Context> contextWeakReference = new WeakReference<>(c);

        String rtn = null;
        try {
            Resources res = contextWeakReference.get().getResources();
            InputStream is = res.openRawResource(id);

            byte buffer[] = new byte[is.available()];
            is.read(buffer);

            rtn = new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;
    }



    public static String getStringDate(long millisecond) {
        Date currentTime = new Date(millisecond);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static String Md5(String str) {
        if (str != null && !str.equals("")) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
                byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < md5Byte.length; i++) {
                    sb.append(HEX[(int) (md5Byte[i] & 0xff) / 16]);
                    sb.append(HEX[(int) (md5Byte[i] & 0xff) % 16]);
                }
                str = sb.toString();
            } catch (NoSuchAlgorithmException e) {
            } catch (Exception e) {
            }
        }
        return str;
    }

    /**
     * 创建 缓存目录
     *
     * @param context
     * @return
     */


    public static String getDiskCacheDirPath(Context context) {

        WeakReference<Context> contextWeakReference = new WeakReference<>(context);

        File appCacheDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && contextWeakReference.get().checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            appCacheDir = getExternalCacheDir(contextWeakReference.get()); //扩展sdCard
        } else {
            appCacheDir = contextWeakReference.get().getFilesDir();
        }
        if (null == appCacheDir) {
            throw new RuntimeException("无法找到文件");
        }
        return appCacheDir.getPath() + File.separator;
    }


    private static File getExternalCacheDir(Context context) {

        WeakReference<Context> contextWeakReference = new WeakReference<>(context);

        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, contextWeakReference.get().getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                throw new RuntimeException("无法创建文件 ");
            }
        }
        return appCacheDir;
    }

}
