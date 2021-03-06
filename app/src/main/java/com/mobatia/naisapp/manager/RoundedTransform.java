/**
 * 
 */
package com.mobatia.naisapp.manager;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

/**
 * @author Rijo K Jose
 * 
 */
public class RoundedTransform implements com.squareup.picasso.Transformation {
	private final int radius;
	private final int margin; // dp

	// radius is corner radii in dp
	// margin is the board in dp
	public RoundedTransform(final int radius, final int margin) {
		this.radius = radius;
		this.margin = margin;
	}

	@Override
	public Bitmap transform(final Bitmap source) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP));

		Bitmap output = Bitmap.createBitmap(source.getWidth(),
				source.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		canvas.drawRoundRect(new RectF(margin, margin, source.getWidth()
				- margin, source.getWidth() - margin), source.getWidth()/2, source.getWidth()/2, paint);

		if (source != output) {
			source.recycle();
		}

		return output;
	}

	@Override
	public String key() {
		return "rounded";
	}
}
