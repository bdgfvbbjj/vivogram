package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class IOSFrostedBlurView extends FrameLayout {

    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean drawTopBorder = true;
    private boolean drawBottomBorder = false;

    public IOSFrostedBlurView(@NonNull Context context) {
        this(context, null);
    }

    public IOSFrostedBlurView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        init();
    }

    private void init() {
        borderPaint.setStrokeWidth(AndroidUtilities.dp(0.5f));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                RenderEffect blurEffect = RenderEffect.createBlurEffect(25f, 25f, Shader.TileMode.CLAMP);
                setRenderEffect(blurEffect);
            } catch (Throwable ignored) {
            }
        }
    }

    public void setDrawBorders(boolean top, boolean bottom) {
        this.drawTopBorder = top;
        this.drawBottomBorder = bottom;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        boolean isDark = Theme.isCurrentThemeDark();
        int glassColor = isDark ? 0xCC1C1C1E : 0xBFFFFFFF;
        canvas.drawColor(glassColor);

        int borderColor = isDark ? 0x26FFFFFF : 0x26000000;
        borderPaint.setColor(borderColor);

        if (drawTopBorder) {
            canvas.drawLine(0, 0, getWidth(), 0, borderPaint);
        }
        if (drawBottomBorder) {
            canvas.drawLine(0, getHeight(), getWidth(), getHeight(), borderPaint);
        }

        super.onDraw(canvas);
    }
}
