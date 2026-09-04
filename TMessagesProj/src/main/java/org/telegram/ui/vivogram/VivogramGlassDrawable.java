package org.telegram.ui.vivogram;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class VivogramGlassDrawable extends Drawable {

    private final Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();
    private float roundRadius;
    private boolean drawTopBorder = true;
    private boolean isDark;

    public VivogramGlassDrawable(boolean isDark, float roundRadius) {
        this.isDark = isDark;
        this.roundRadius = roundRadius;
        updateColors();
    }

    public void setDark(boolean dark) {
        if (this.isDark != dark) {
            this.isDark = dark;
            updateColors();
            invalidateSelf();
        }
    }

    public void setDrawTopBorder(boolean draw) {
        this.drawTopBorder = draw;
        invalidateSelf();
    }

    public void setRoundRadius(float radius) {
        this.roundRadius = radius;
        invalidateSelf();
    }

    public void updateColors() {
        if (isDark) {
            // rgba(28, 28, 30, 0.8)
            bgPaint.setColor(0xCC1C1C1E);
            // rgba(255, 255, 255, 0.15)
            borderPaint.setColor(0x26FFFFFF);
        } else {
            // rgba(255, 255, 255, 0.75)
            bgPaint.setColor(0xBFFFFFFF);
            // rgba(0, 0, 0, 0.15)
            borderPaint.setColor(0x26000000);
        }
        borderPaint.setStrokeWidth(AndroidUtilities.dpf2(0.5f));
        borderPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        rect.set(bounds);
        if (roundRadius > 0) {
            canvas.drawRoundRect(rect, roundRadius, roundRadius, bgPaint);
        } else {
            canvas.drawRect(rect, bgPaint);
        }

        if (drawTopBorder) {
            float y = bounds.top + AndroidUtilities.dpf2(0.5f) / 2f;
            canvas.drawLine(bounds.left, y, bounds.right, y, borderPaint);
        }
    }

    public static void applyGlassBlurToView(View view, float blurRadiusDp) {
        if (view == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                float radius = AndroidUtilities.dp(blurRadiusDp);
                view.setRenderEffect(RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.CLAMP));
            } catch (Throwable ignore) {}
        }
    }

    @Override
    public void setAlpha(int alpha) {
        bgPaint.setAlpha(alpha);
        borderPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        bgPaint.setColorFilter(colorFilter);
        borderPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
