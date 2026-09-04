package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.widget.ImageView;

import androidx.annotation.ColorInt;

import org.telegram.messenger.AndroidUtilities;

public class IOSSettingIconView extends ImageView {

    private final Path squirclePath = new Path();
    private final RectF bounds = new RectF();
    private final Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public IOSSettingIconView(Context context) {
        super(context);
        setScaleType(ScaleType.CENTER_INSIDE);
    }

    public void setIcon(int resId, @ColorInt int backgroundColor) {
        setImageResource(resId);
        setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        bgPaint.setColor(backgroundColor);
        invalidate();
    }

    public void setIconColor(@ColorInt int backgroundColor) {
        bgPaint.setColor(backgroundColor);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = AndroidUtilities.dp(29);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bounds.set(0, 0, w, h);
        squirclePath.reset();
        float radius = AndroidUtilities.dp(7);
        squirclePath.addRoundRect(bounds, radius, radius, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(squirclePath);
        canvas.drawRect(bounds, bgPaint);
        super.onDraw(canvas);
        canvas.restore();
    }
}
