package com.conges.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;

public class ZoomImage {

	public static void setupDisplay(Activity a, int displaywidth, int displayheight) {
		DisplayMetrics metric = new DisplayMetrics();
		a.getWindowManager().getDefaultDisplay().getMetrics(metric);
		displaywidth = metric.widthPixels;
		displayheight = metric.heightPixels;
	}

	public static Bitmap readBitmapFromResource(Context context,
			int resourceId, float width, float height) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resourceId);

		Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
		int bmpWidth = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();

		float scaleWidth = (float) width / bmpWidth;
		float scaleHeight = (float) height / bmpHeight;

		Matrix matrix = new Matrix();

		matrix.postScale(scaleWidth, scaleHeight);

		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix,
				false);
		return bitmap;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	public static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	static public byte[] getBytes(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		byte[] by = new byte[1024];
		int len = 0;
		try {
			while ((len = is.read(by, 0, 1024)) != -1) {

				baos.write(by, 0, len);
				baos.flush();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte bytes[] = baos.toByteArray();
		return bytes;
	}

	static public Bitmap loadWebImage(final String url, int displaywidth,
			int displayheight) {
		Bitmap bm = null;
		try {
			URL myURL = new URL(url);
			URLConnection ucon = myURL.openConnection();
			ucon.setConnectTimeout(10000);
			ucon.setReadTimeout(20000);
			InputStream is = ucon.getInputStream();
			byte bytes[] = getBytes(is);

			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);

			Log.i("¿´bytes[]", bytes.length + "");
			opts.inSampleSize = computeSampleSize(opts, -1, displaywidth * 3
					/ 4 * displayheight / 4);
			opts.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);

			int bmpWidth = bm.getWidth();
			int bmpHeight = bm.getHeight();

			float scaleWidth = (float) ((float) displaywidth * (2.36) / 5 / bmpWidth);
			float scaleHeight = (float) displayheight / 6 / bmpHeight;

			Matrix matrix = new Matrix();

			matrix.postScale(scaleWidth, scaleHeight);

			bm = Bitmap.createBitmap(bm, 0, 0, bmpWidth, bmpHeight, matrix,
					false);

			bm = (new SoftReference<Bitmap>(bm)).get();

		} catch (Exception e) {

		}

		return bm;
	}
}
