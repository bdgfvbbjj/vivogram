package org.telegram.ui.vivogram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.messenger.vivogram.VivogramHistoryStorage;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

import java.util.ArrayList;

public class VivogramEditHistorySheet extends BottomSheet {

    private ArrayList<VivogramHistoryStorage.MessageEdit> edits;
    private MessageObject currentMessage;

    public VivogramEditHistorySheet(Context context, MessageObject messageObject) {
        super(context, false);
        this.currentMessage = messageObject;
        this.edits = VivogramHistoryStorage.getInstance(messageObject.currentAccount).getMessageEdits(messageObject.getDialogId(), messageObject.getId());

        FrameLayout container = new FrameLayout(context);

        TextView titleView = new TextView(context);
        titleView.setText("История изменений сообщения");
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        titleView.setTypeface(AndroidUtilities.bold());
        titleView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        titleView.setGravity(Gravity.CENTER_VERTICAL);
        container.addView(titleView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 48, Gravity.TOP | Gravity.LEFT, 20, 10, 20, 0));

        RecyclerListView listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context));
        listView.setAdapter(new ListAdapter(context));
        listView.setClipToPadding(false);
        listView.setPadding(0, 0, 0, AndroidUtilities.dp(16));
        container.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.TOP | Gravity.LEFT, 0, 58, 0, 0));

        setCustomView(container);
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            return false;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = new EditItemView(mContext);
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            EditItemView cell = (EditItemView) holder.itemView;
            if (position < edits.size()) {
                VivogramHistoryStorage.MessageEdit edit = edits.get(position);
                cell.setData(position + 1, edit.editDate, edit.text, false);
            } else {
                // Current version
                int date = currentMessage.messageOwner != null ? currentMessage.messageOwner.edit_date : 0;
                if (date == 0 && currentMessage.messageOwner != null) {
                    date = currentMessage.messageOwner.date;
                }
                CharSequence text = currentMessage.messageText;
                cell.setData(edits.size() + 1, date, text != null ? text.toString() : "", true);
            }
        }

        @Override
        public int getItemCount() {
            return edits.size() + 1; // edits + current version
        }
    }

    private class EditItemView extends FrameLayout {
        private TextView versionView;
        private TextView dateView;
        private TextView messageView;

        public EditItemView(Context context) {
            super(context);

            FrameLayout topLayout = new FrameLayout(context);
            addView(topLayout, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.TOP | Gravity.LEFT, 20, 10, 20, 0));

            versionView = new TextView(context);
            versionView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            versionView.setTypeface(AndroidUtilities.bold());
            versionView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueHeader));
            topLayout.addView(versionView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.LEFT | Gravity.CENTER_VERTICAL));

            dateView = new TextView(context);
            dateView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            dateView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2));
            topLayout.addView(dateView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL));

            messageView = new TextView(context);
            messageView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            messageView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            messageView.setTextIsSelectable(true);
            addView(messageView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.TOP | Gravity.LEFT, 20, 32, 20, 10));
        }

        public void setData(int version, int date, String text, boolean isCurrent) {
            if (isCurrent) {
                versionView.setText("Версия " + version + " (текущая)");
            } else {
                versionView.setText("Версия " + version);
            }
            dateView.setText(LocaleController.formatDateTime(date));
            messageView.setText(text);
        }
    }
}
