package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class IOSGroupedCardHelper {

    public enum Position {
        NORMAL,
        TOP,
        MIDDLE,
        BOTTOM,
        SINGLE
    }

    private final View parentView;
    private Position position = Position.NORMAL;
    private final Path clipPath = new Path();
    private final RectF bounds = new RectF();
    private final Paint dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean drawDivider = true;
    private float dividerLeftMargin = 58f;
    private boolean iosThemeEnabled = true;

    public IOSGroupedCardHelper(View view) {
        this.parentView = view;
        dividerPaint.setStrokeWidth(AndroidUtilities.dp(0.5f));
    }

    public void setPosition(Position pos, boolean drawDivider) {
        this.position = pos;
        this.drawDivider = drawDivider;
        updateMargins();
        parentView.invalidate();
    }

    public void setDividerLeftMargin(float dp) {
        this.dividerLeftMargin = dp;
    }

    public void setIosThemeEnabled(boolean enabled) {
        this.iosThemeEnabled = enabled;
        updateMargins();
        parentView.invalidate();
    }

    public void updateMargins() {
        if (!iosThemeEnabled || position == Position.NORMAL) {
            return;
        }
        ViewGroup.LayoutParams params = parentView.getLayoutParams();
        if (params instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) params;
            lp.leftMargin = AndroidUtilities.dp(16);
            lp.rightMargin = AndroidUtilities.dp(16);
            lp.topMargin = (position == Position.TOP || position == Position.SINGLE) ? AndroidUtilities.dp(8) : 0;
            lp.bottomMargin = (position == Position.BOTTOM || position == Position.SINGLE) ? AndroidUtilities.dp(8) : 0;
            parentView.setLayoutParams(lp);
        }
    }

    public void onSizeChanged(int w, int h) {
        if (!iosThemeEnabled || position == Position.NORMAL) {
            return;
        }
        bounds.set(0, 0, w, h);
        clipPath.reset();
        float r = AndroidUtilities.dp(12);
        float[] radii;
        switch (position) {
            case TOP:
                radii = new float[]{r, r, r, r, 0, 0, 0, 0};
                break;
            case BOTTOM:
                radii = new float[]{0, 0, 0, 0, r, r, r, r};
                break;
            case SINGLE:
                radii = new float[]{r, r, r, r, r, r, r, r};
                break;
            case MIDDLE:
            default:
                radii = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
                break;
        }
        clipPath.addRoundRect(bounds, radii, Path.Direction.CW);
    }

    public void beforeDraw(Canvas canvas) {
        if (iosThemeEnabled && position != Position.NORMAL) {
            canvas.save();
            canvas.clipPath(clipPath);
            canvas.drawColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        }
    }

    public void afterDraw(Canvas canvas) {
        if (iosThemeEnabled && position != Position.NORMAL) {
            if (drawDivider && position != Position.BOTTOM && position != Position.SINGLE) {
                dividerPaint.setColor(Theme.getColor(Theme.key_divider));
                float lineLeft = AndroidUtilities.dp(dividerLeftMargin);
                float lineY = parentView.getHeight() - AndroidUtilities.dp(0.5f);
                canvas.drawLine(lineLeft, lineY, parentView.getWidth(), lineY, dividerPaint);
            }
            canvas.restore();
        }
    }

    public boolean handleTouchEvent(MotionEvent event) {
        if (!iosThemeEnabled || position == Position.NORMAL) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                parentView.animate().scaleX(0.985f).scaleY(0.985f).alpha(0.85f).setDuration(120).start();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                parentView.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(180).start();
                break;
        }
        return false;
    }
}
