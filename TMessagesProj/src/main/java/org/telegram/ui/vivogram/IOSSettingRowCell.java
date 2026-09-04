package org.telegram.ui.vivogram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Switch;

public class IOSSettingRowCell extends FrameLayout implements Theme.Colorable {

    public static final int POSITION_SINGLE = 0;
    public static final int POSITION_TOP = 1;
    public static final int POSITION_MIDDLE = 2;
    public static final int POSITION_BOTTOM = 3;

    private final Theme.ResourcesProvider resourcesProvider;
    private final FrameLayout cardContainer;
    private final FrameLayout iconLayout;
    private final IconBackground iconBackground;
    private final ImageView iconView;
    private final LinearLayout textLayout;
    private final TextView titleView;
    private final TextView subtitleView;
    private final TextView valueView;
    private final ImageView chevronView;
    private final Switch switchView;
    private final View dividerView;

    private int positionInGroup = POSITION_SINGLE;
    private boolean isSwitchCell = false;

    public IOSSettingRowCell(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.resourcesProvider = resourcesProvider;

        setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);

        cardContainer = new FrameLayout(context);
        addView(cardContainer, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        // Icon badge (29x29 dp, 7dp squircle)
        iconLayout = new FrameLayout(context);
        iconLayout.setBackground(iconBackground = new IconBackground());

        iconView = new ImageView(context);
        iconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iconView.setColorFilter(0xFFFFFFFF);
        iconLayout.addView(iconView, LayoutHelper.createFrame(18, 18, Gravity.CENTER));

        cardContainer.addView(iconLayout, LayoutHelper.createFrame(29, 29, Gravity.CENTER_VERTICAL | (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT), LocaleController.isRTL ? 0 : 14, 0, LocaleController.isRTL ? 14 : 0, 0));

        // Text
        textLayout = new LinearLayout(context);
        textLayout.setOrientation(LinearLayout.VERTICAL);

        titleView = new TextView(context);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        titleView.setSingleLine(true);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        textLayout.addView(titleView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        subtitleView = new TextView(context);
        subtitleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        subtitleView.setVisibility(GONE);
        textLayout.addView(subtitleView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 2, 0, 0));

        if (LocaleController.isRTL) {
            cardContainer.addView(textLayout, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL, 60, 0, 56, 0));
        } else {
            cardContainer.addView(textLayout, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL, 56, 0, 60, 0));
        }

        // Value / Right text
        valueView = new TextView(context);
        valueView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        valueView.setVisibility(GONE);
        cardContainer.addView(valueView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL | (LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT), LocaleController.isRTL ? 36 : 0, 0, LocaleController.isRTL ? 0 : 36, 0));

        // Chevron
        chevronView = new ImageView(context);
        chevronView.setImageResource(R.drawable.arrow_more);
        chevronView.setColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon, resourcesProvider));
        chevronView.setVisibility(GONE);
        cardContainer.addView(chevronView, LayoutHelper.createFrame(16, 16, Gravity.CENTER_VERTICAL | (LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT), LocaleController.isRTL ? 14 : 0, 0, LocaleController.isRTL ? 0 : 14, 0));

        // Switch
        switchView = new Switch(context);
        switchView.setVisibility(GONE);
        cardContainer.addView(switchView, LayoutHelper.createFrame(37, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL | (LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT), LocaleController.isRTL ? 14 : 0, 0, LocaleController.isRTL ? 0 : 14, 0));

        // Divider
        dividerView = new View(context);
        int dividerColor = Theme.getColor(Theme.key_divider, resourcesProvider);
        dividerView.setBackgroundColor(dividerColor != 0 ? dividerColor : 0x1A000000);
        cardContainer.addView(dividerView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 0.6f, Gravity.BOTTOM, LocaleController.isRTL ? 14 : 56, 0, LocaleController.isRTL ? 56 : 14, 0));

        updateColors();
    }

    public void setPositionInGroup(int position) {
        this.positionInGroup = position;
        updateBackground();
        dividerView.setVisibility(position == POSITION_MIDDLE || position == POSITION_TOP ? VISIBLE : GONE);
    }

    public void set(int iconColorTop, int iconColorBottom, int iconRes, CharSequence title, CharSequence subtitle, boolean isSwitch, boolean isChecked, CharSequence value) {
        this.isSwitchCell = isSwitch;
        iconBackground.setColor(iconColorTop, iconColorBottom);
        iconView.setImageResource(iconRes);
        titleView.setText(title);

        if (!TextUtils.isEmpty(subtitle)) {
            subtitleView.setVisibility(VISIBLE);
            subtitleView.setText(subtitle);
        } else {
            subtitleView.setVisibility(GONE);
        }

        if (isSwitch) {
            switchView.setVisibility(VISIBLE);
            switchView.setChecked(isChecked, false);
            valueView.setVisibility(GONE);
            chevronView.setVisibility(GONE);
        } else {
            switchView.setVisibility(GONE);
            if (!TextUtils.isEmpty(value)) {
                valueView.setVisibility(VISIBLE);
                valueView.setText(value);
                chevronView.setVisibility(VISIBLE);
            } else {
                valueView.setVisibility(GONE);
                chevronView.setVisibility(VISIBLE);
            }
        }
    }

    public void setChecked(boolean checked) {
        if (isSwitchCell) {
            switchView.setChecked(checked, true);
        }
    }

    public boolean isChecked() {
        return switchView.isChecked();
    }

    public Switch getSwitchView() {
        return switchView;
    }

    private void updateBackground() {
        int bgColor = Theme.getColor(Theme.key_windowBackgroundWhite, resourcesProvider);
        int topRad = (positionInGroup == POSITION_TOP || positionInGroup == POSITION_SINGLE) ? 12 : 0;
        int botRad = (positionInGroup == POSITION_BOTTOM || positionInGroup == POSITION_SINGLE) ? 12 : 0;
        int selectorColor = Theme.getColor(Theme.key_listSelector, resourcesProvider);
        cardContainer.setBackground(Theme.createRadSelectorDrawable(bgColor, selectorColor, topRad, botRad));
    }

    @Override
    public void updateColors() {
        titleView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
        subtitleView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText, resourcesProvider));
        valueView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteValueText, resourcesProvider));
        chevronView.setColorFilter(Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon, resourcesProvider));
        int dividerColor = Theme.getColor(Theme.key_divider, resourcesProvider);
        dividerView.setBackgroundColor(dividerColor != 0 ? dividerColor : 0x1A000000);
        updateBackground();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = subtitleView.getVisibility() == VISIBLE ? 60 : 48;
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(height), MeasureSpec.EXACTLY)
        );
    }

    public static class IconBackground extends Drawable {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private LinearGradient gradient;
        private final RectF rect = new RectF();

        public void setColor(int topColor, int bottomColor) {
            gradient = new LinearGradient(0, 0, 0, AndroidUtilities.dp(29), new int[]{topColor, bottomColor}, new float[]{0, 1}, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            invalidateSelf();
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            rect.set(getBounds());
            float r = AndroidUtilities.dpf2(7f);
            canvas.drawRoundRect(rect, r, r, paint);
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {
            paint.setColorFilter(colorFilter);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }
}
