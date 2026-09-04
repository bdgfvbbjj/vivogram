package org.telegram.ui.vivogram;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class IOSHeaderCell extends FrameLayout implements Theme.Colorable {

    private final TextView textView;
    private final Theme.ResourcesProvider resourcesProvider;

    public IOSHeaderCell(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.resourcesProvider = resourcesProvider;

        textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setSingleLine(true);
        textView.setAllCaps(true);

        int left = LocaleController.isRTL ? 16 : 32;
        int right = LocaleController.isRTL ? 32 : 16;
        addView(textView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM, left, 16, right, 6));

        updateColors();
    }

    public void setText(CharSequence text) {
        textView.setText(text);
    }

    @Override
    public void updateColors() {
        textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText, resourcesProvider));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44), MeasureSpec.EXACTLY)
        );
    }
}
