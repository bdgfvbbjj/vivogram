package org.telegram.ui.vivogram;

import android.content.Context;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.vivogram.VivogramConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class VivogramSettingsActivity extends BaseFragment {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ROW = 1;
    private static final int TYPE_SHADOW = 2;

    private RecyclerListView listView;
    private ListAdapter listAdapter;

    private int rowCount;

    // Ghost section
    private int ghostHeaderRow;
    private int ghostModeRow;
    private int ghostReadHistoryRow;
    private int ghostTypingRow;
    private int ghostOnlineRow;
    private int ghostReadVoiceRow;
    private int ghostShadowRow;

    // Messages section
    private int messagesHeaderRow;
    private int saveDeletedRow;
    private int saveEditsRow;
    private int messagesShadowRow;

    // Anti-Restrictions section
    private int antiHeaderRow;
    private int allowScreenshotsRow;
    private int saveRestrictedMediaRow;
    private int infiniteViewOnceRow;
    private int autoSaveExpiringMediaRow;
    private int antiShadowRow;

    // Ads & Filters section
    private int adsHeaderRow;
    private int blockAdsRow;
    private int hideReactionsRow;
    private int spamFilterRow;
    private int spamKeywordsRow;
    private int adsShadowRow;

    // QoL & UI Tweaks section
    private int uiHeaderRow;
    private int forwardNoAuthorRow;
    private int showSecondsRow;
    private int confirmSendMediaRow;
    private int disableStoriesRow;
    private int showIdDcRow;
    private int uiShadowRow;

    // Privacy & Hidden section
    private int privacyHeaderRow;
    private int hiddenPasscodeRow;
    private int privacyShadowRow;

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        updateRows();
        return true;
    }

    private void updateRows() {
        rowCount = 0;

        // Ghost Section (5 items)
        ghostHeaderRow = rowCount++;
        ghostModeRow = rowCount++;
        ghostReadHistoryRow = rowCount++;
        ghostTypingRow = rowCount++;
        ghostOnlineRow = rowCount++;
        ghostReadVoiceRow = rowCount++;
        ghostShadowRow = rowCount++;

        // Messages Section (2 items)
        messagesHeaderRow = rowCount++;
        saveDeletedRow = rowCount++;
        saveEditsRow = rowCount++;
        messagesShadowRow = rowCount++;

        // Anti-Restrictions Section (4 items)
        antiHeaderRow = rowCount++;
        allowScreenshotsRow = rowCount++;
        saveRestrictedMediaRow = rowCount++;
        infiniteViewOnceRow = rowCount++;
        autoSaveExpiringMediaRow = rowCount++;
        antiShadowRow = rowCount++;

        // Ads & Filters Section (4 items)
        adsHeaderRow = rowCount++;
        blockAdsRow = rowCount++;
        hideReactionsRow = rowCount++;
        spamFilterRow = rowCount++;
        spamKeywordsRow = rowCount++;
        adsShadowRow = rowCount++;

        // QoL & UI Section (5 items)
        uiHeaderRow = rowCount++;
        forwardNoAuthorRow = rowCount++;
        showSecondsRow = rowCount++;
        confirmSendMediaRow = rowCount++;
        disableStoriesRow = rowCount++;
        showIdDcRow = rowCount++;
        uiShadowRow = rowCount++;

        // Privacy Section (1 item)
        privacyHeaderRow = rowCount++;
        hiddenPasscodeRow = rowCount++;
        privacyShadowRow = rowCount++;
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setTitle(LocaleController.getString(R.string.VivogramSettings));
        actionBar.setAllowOverlayTitle(true);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        fragmentView = new FrameLayout(context);
        fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout = (FrameLayout) fragmentView;

        listView = new RecyclerListView(context);
        listView.setVerticalScrollBarEnabled(false);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        listAdapter = new ListAdapter(context);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener((view, position) -> {
            if (!(view instanceof IOSSettingRowCell)) {
                return;
            }
            IOSSettingRowCell rowCell = (IOSSettingRowCell) view;

            if (position == ghostModeRow) {
                boolean next = !VivogramConfig.isGhostMode();
                VivogramConfig.setGhostMode(next);
                rowCell.setChecked(next);
                listAdapter.notifyItemRangeChanged(ghostReadHistoryRow, 4);
            } else if (position == ghostReadHistoryRow) {
                boolean next = !VivogramConfig.isGhostReadHistory();
                VivogramConfig.setGhostReadHistory(next);
                rowCell.setChecked(next);
            } else if (position == ghostTypingRow) {
                boolean next = !VivogramConfig.isGhostTyping();
                VivogramConfig.setGhostTyping(next);
                rowCell.setChecked(next);
            } else if (position == ghostOnlineRow) {
                boolean next = !VivogramConfig.isGhostOnline();
                VivogramConfig.setGhostOnline(next);
                rowCell.setChecked(next);
            } else if (position == ghostReadVoiceRow) {
                boolean next = !VivogramConfig.isGhostReadVoice();
                VivogramConfig.setGhostReadVoice(next);
                rowCell.setChecked(next);
            } else if (position == saveDeletedRow) {
                boolean next = !VivogramConfig.isSaveDeleted();
                VivogramConfig.setSaveDeleted(next);
                rowCell.setChecked(next);
            } else if (position == saveEditsRow) {
                boolean next = !VivogramConfig.isSaveEdits();
                VivogramConfig.setSaveEdits(next);
                rowCell.setChecked(next);
            } else if (position == allowScreenshotsRow) {
                boolean next = !VivogramConfig.isAllowScreenshots();
                VivogramConfig.setAllowScreenshots(next);
                rowCell.setChecked(next);
            } else if (position == saveRestrictedMediaRow) {
                boolean next = !VivogramConfig.isSaveRestrictedMedia();
                VivogramConfig.setSaveRestrictedMedia(next);
                rowCell.setChecked(next);
            } else if (position == infiniteViewOnceRow) {
                boolean next = !VivogramConfig.isInfiniteViewOnce();
                VivogramConfig.setInfiniteViewOnce(next);
                rowCell.setChecked(next);
            } else if (position == autoSaveExpiringMediaRow) {
                boolean next = !VivogramConfig.isAutoSaveExpiringMedia();
                VivogramConfig.setAutoSaveExpiringMedia(next);
                rowCell.setChecked(next);
            } else if (position == blockAdsRow) {
                boolean next = !VivogramConfig.isBlockTelegramAds();
                VivogramConfig.setBlockTelegramAds(next);
                rowCell.setChecked(next);
            } else if (position == hideReactionsRow) {
                boolean next = !VivogramConfig.isHideReactions();
                VivogramConfig.setHideReactions(next);
                rowCell.setChecked(next);
            } else if (position == spamFilterRow) {
                boolean next = !VivogramConfig.isSpamFilterEnabled();
                VivogramConfig.setSpamFilterEnabled(next);
                rowCell.setChecked(next);
            } else if (position == spamKeywordsRow) {
                promptSpamKeywords(context);
            } else if (position == forwardNoAuthorRow) {
                boolean next = !VivogramConfig.isForwardNoAuthor();
                VivogramConfig.setForwardNoAuthor(next);
                rowCell.setChecked(next);
            } else if (position == showSecondsRow) {
                boolean next = !VivogramConfig.isShowSeconds();
                VivogramConfig.setShowSeconds(next);
                rowCell.setChecked(next);
            } else if (position == confirmSendMediaRow) {
                boolean next = !VivogramConfig.isConfirmSendMedia();
                VivogramConfig.setConfirmSendMedia(next);
                rowCell.setChecked(next);
            } else if (position == disableStoriesRow) {
                boolean next = !VivogramConfig.isDisableStories();
                VivogramConfig.setDisableStories(next);
                rowCell.setChecked(next);
                BulletinFactory.of(this).createSimpleBulletin(R.drawable.msg_settings, LocaleController.getString(R.string.VivogramRestartNotice)).show();
            } else if (position == showIdDcRow) {
                boolean next = !VivogramConfig.isShowIdDcProfile();
                VivogramConfig.setShowIdDcProfile(next);
                rowCell.setChecked(next);
            } else if (position == hiddenPasscodeRow) {
                promptPasscodeChange(context);
            }
        });

        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setDurations(350);
        itemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        itemAnimator.setDelayAnimations(false);
        itemAnimator.setSupportsChangeAnimations(false);
        listView.setItemAnimator(itemAnimator);

        return fragmentView;
    }

    private void promptSpamKeywords(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(LocaleController.getString(R.string.VivogramSpamKeywords));

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setHint(LocaleController.getString(R.string.VivogramSpamKeywordsHint));
        input.setText(VivogramConfig.getSpamFilterKeywords());
        input.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        input.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        input.setHintTextColor(Theme.getColor(Theme.key_dialogTextHint));

        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setPadding(AndroidUtilities.dp(24), AndroidUtilities.dp(8), AndroidUtilities.dp(24), 0);
        frameLayout.addView(input, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
        builder.setView(frameLayout);

        builder.setPositiveButton(LocaleController.getString(R.string.Save), (dialog, which) -> {
            String text = input.getText().toString().trim();
            VivogramConfig.setSpamFilterKeywords(text);
            BulletinFactory.of(this).createSimpleBulletin(R.drawable.msg_customize, LocaleController.getString(R.string.VivogramSpamKeywordsSaved)).show();
            if (listAdapter != null) listAdapter.notifyItemChanged(spamKeywordsRow);
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        showDialog(builder.create());
    }

    private void promptPasscodeChange(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(VivogramConfig.hasHiddenPasscode() ? LocaleController.getString(R.string.VivogramChangePasscode) : LocaleController.getString(R.string.VivogramSetPasscode));

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        input.setHint(LocaleController.getString(R.string.VivogramEnterPasscodeHint));
        input.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        input.setTextColor(Theme.getColor(Theme.key_dialogTextBlack));
        input.setHintTextColor(Theme.getColor(Theme.key_dialogTextHint));

        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setPadding(AndroidUtilities.dp(24), AndroidUtilities.dp(8), AndroidUtilities.dp(24), 0);
        frameLayout.addView(input, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
        builder.setView(frameLayout);

        builder.setPositiveButton(LocaleController.getString(R.string.Save), (dialog, which) -> {
            String code = input.getText().toString().trim();
            if (!code.isEmpty()) {
                VivogramConfig.setHiddenPasscode(code);
                BulletinFactory.of(this).createSimpleBulletin(R.drawable.msg_secret, LocaleController.getString(R.string.VivogramPasscodeSaved)).show();
                if (listAdapter != null) listAdapter.notifyItemChanged(hiddenPasscodeRow);
            }
        });

        if (VivogramConfig.hasHiddenPasscode()) {
            builder.setNeutralButton(LocaleController.getString(R.string.Delete), (dialog, which) -> {
                VivogramConfig.setHiddenPasscode(null);
                BulletinFactory.of(this).createSimpleBulletin(R.drawable.msg_delete, LocaleController.getString(R.string.VivogramPasscodeDeleted)).show();
                if (listAdapter != null) listAdapter.notifyItemChanged(hiddenPasscodeRow);
            });
        }
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        showDialog(builder.create());
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {

        private final Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == ghostHeaderRow || position == messagesHeaderRow ||
                    position == antiHeaderRow || position == adsHeaderRow ||
                    position == uiHeaderRow || position == privacyHeaderRow) {
                return TYPE_HEADER;
            } else if (position == ghostShadowRow || position == messagesShadowRow ||
                    position == antiShadowRow || position == adsShadowRow ||
                    position == uiShadowRow || position == privacyShadowRow) {
                return TYPE_SHADOW;
            }
            return TYPE_ROW;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            return holder.getItemViewType() == TYPE_ROW;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (viewType == TYPE_HEADER) {
                view = new IOSHeaderCell(mContext, resourceProvider);
            } else if (viewType == TYPE_SHADOW) {
                view = new ShadowSectionCell(mContext);
            } else {
                view = new IOSSettingRowCell(mContext, resourceProvider);
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            int viewType = holder.getItemViewType();

            if (viewType == TYPE_HEADER) {
                IOSHeaderCell cell = (IOSHeaderCell) holder.itemView;
                if (position == ghostHeaderRow) {
                    cell.setText(LocaleController.getString(R.string.VivogramGhostMode));
                } else if (position == messagesHeaderRow) {
                    cell.setText(LocaleController.getString(R.string.VivogramMessagesAndHistory));
                } else if (position == antiHeaderRow) {
                    cell.setText(LocaleController.getString(R.string.VivogramAntiRestrictions));
                } else if (position == adsHeaderRow) {
                    cell.setText(LocaleController.getString(R.string.VivogramAdsAndFilters));
                } else if (position == uiHeaderRow) {
                    cell.setText(LocaleController.getString(R.string.VivogramQoLAndUI));
                } else if (position == privacyHeaderRow) {
                    cell.setText(LocaleController.getString(R.string.VivogramPrivacyAndSecurity));
                }
            } else if (viewType == TYPE_ROW) {
                IOSSettingRowCell cell = (IOSSettingRowCell) holder.itemView;

                if (position == ghostModeRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_TOP);
                    cell.set(0xFF8E44AD, 0xFF6C3483, R.drawable.msg_secret,
                            LocaleController.getString(R.string.VivogramGhostModeTitle),
                            LocaleController.getString(R.string.VivogramGhostModeDesc),
                            true, VivogramConfig.isGhostMode(), null);
                } else if (position == ghostReadHistoryRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_MIDDLE);
                    cell.set(0xFF007AFF, 0xFF0051A8, R.drawable.msg_seen,
                            LocaleController.getString(R.string.VivogramGhostReadHistory),
                            LocaleController.getString(R.string.VivogramGhostReadHistoryDesc),
                            true, VivogramConfig.isGhostReadHistory(), null);
                } else if (position == ghostTypingRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_MIDDLE);
                    cell.set(0xFFFF9500, 0xFFC97600, R.drawable.msg_discussion,
                            LocaleController.getString(R.string.VivogramGhostTyping),
                            LocaleController.getString(R.string.VivogramGhostTypingDesc),
                            true, VivogramConfig.isGhostTyping(), null);
                } else if (position == ghostOnlineRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_MIDDLE);
                    cell.set(0xFF34C759, 0xFF248A3D, R.drawable.msg_edit,
                            LocaleController.getString(R.string.VivogramGhostOnline),
                            LocaleController.getString(R.string.VivogramGhostOnlineDesc),
                            true, VivogramConfig.isGhostOnline(), null);
                } else if (position == ghostReadVoiceRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_BOTTOM);
                    cell.set(0xFF30B0C7, 0xFF1D7887, R.drawable.msg_played,
                            LocaleController.getString(R.string.VivogramGhostReadVoice),
                            LocaleController.getString(R.string.VivogramGhostReadVoiceDesc),
                            true, VivogramConfig.isGhostReadVoice(), null);
                } else if (position == saveDeletedRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_TOP);
                    cell.set(0xFFFF3B30, 0xFFB82820, R.drawable.msg_delete,
                            LocaleController.getString(R.string.VivogramSaveDeleted),
                            LocaleController.getString(R.string.VivogramSaveDeletedDesc),
                            true, VivogramConfig.isSaveDeleted(), null);
                } else if (position == saveEditsRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_BOTTOM);
                    cell.set(0xFFFF9F0A, 0xFFC27400, R.drawable.msg_edit,
                            LocaleController.getString(R.string.VivogramSaveEdits),
                            LocaleController.getString(R.string.VivogramSaveEditsDesc),
                            true, VivogramConfig.isSaveEdits(), null);
                } else if (position == allowScreenshotsRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_TOP);
                    cell.set(0xFF5856D6, 0xFF3634A3, R.drawable.msg_channel,
                            LocaleController.getString(R.string.VivogramAllowScreenshots),
                            LocaleController.getString(R.string.VivogramAllowScreenshotsDesc),
                            true, VivogramConfig.isAllowScreenshots(), null);
                } else if (position == saveRestrictedMediaRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_MIDDLE);
                    cell.set(0xFF007AFF, 0xFF0051A8, R.drawable.msg_download,
                            LocaleController.getString(R.string.VivogramSaveRestrictedMedia),
                            LocaleController.getString(R.string.VivogramSaveRestrictedMediaDesc),
                            true, VivogramConfig.isSaveRestrictedMedia(), null);
                } else if (position == infiniteViewOnceRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_MIDDLE);
                    cell.set(0xFFFF2D55, 0xFFB31835, R.drawable.msg_secret,
                            LocaleController.getString(R.string.VivogramInfiniteViewOnce),
                            LocaleController.getString(R.string.VivogramInfiniteViewOnceDesc),
                            true, VivogramConfig.isInfiniteViewOnce(), null);
                } else if (position == autoSaveExpiringMediaRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_BOTTOM);
                    cell.set(0xFF34C759, 0xFF248A3D, R.drawable.msg_photos,
                            LocaleController.getString(R.string.VivogramAutoSaveExpiringMedia),
                            LocaleController.getString(R.string.VivogramAutoSaveExpiringMediaDesc),
                            true, VivogramConfig.isAutoSaveExpiringMedia(), null);
                } else if (position == blockAdsRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_TOP);
                    cell.set(0xFFFF9500, 0xFFC97600, R.drawable.msg_block2,
                            LocaleController.getString(R.string.VivogramBlockAds),
                            LocaleController.getString(R.string.VivogramBlockAdsDesc),
                            true, VivogramConfig.isBlockTelegramAds(), null);
                } else if (position == hideReactionsRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_MIDDLE);
                    cell.set(0xFFFF2D55, 0xFFB31835, R.drawable.msg_reactions,
                            LocaleController.getString(R.string.VivogramHideReactions),
                            LocaleController.getString(R.string.VivogramHideReactionsDesc),
                            true, VivogramConfig.isHideReactions(), null);
                } else if (position == spamFilterRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_MIDDLE);
                    cell.set(0xFFFF3B30, 0xFFB82820, R.drawable.msg_secret,
                            LocaleController.getString(R.string.VivogramSpamFilter),
                            LocaleController.getString(R.string.VivogramSpamFilterDesc),
                            true, VivogramConfig.isSpamFilterEnabled(), null);
                } else if (position == spamKeywordsRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_BOTTOM);
                    cell.set(0xFF8E44AD, 0xFF6C3483, R.drawable.msg_customize,
                            LocaleController.getString(R.string.VivogramSpamKeywords),
                            LocaleController.getString(R.string.VivogramSpamKeywordsHint),
                            false, false, VivogramConfig.getSpamFilterKeywords().isEmpty() ? LocaleController.getString(R.string.VivogramPasscodeNotSet) : "...");
                } else if (position == forwardNoAuthorRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_TOP);
                    cell.set(0xFF007AFF, 0xFF0051A8, R.drawable.msg_share,
                            LocaleController.getString(R.string.VivogramForwardNoAuthor),
                            LocaleController.getString(R.string.VivogramForwardNoAuthorDesc),
                            true, VivogramConfig.isForwardNoAuthor(), null);
                } else if (position == showSecondsRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_MIDDLE);
                    cell.set(0xFF5856D6, 0xFF3634A3, R.drawable.msg_calendar,
                            LocaleController.getString(R.string.VivogramShowSeconds),
                            LocaleController.getString(R.string.VivogramShowSecondsDesc),
                            true, VivogramConfig.isShowSeconds(), null);
                } else if (position == confirmSendMediaRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_MIDDLE);
                    cell.set(0xFFFF9500, 0xFFC97600, R.drawable.msg_sendfile,
                            LocaleController.getString(R.string.VivogramConfirmSendMedia),
                            LocaleController.getString(R.string.VivogramConfirmSendMediaDesc),
                            true, VivogramConfig.isConfirmSendMedia(), null);
                } else if (position == disableStoriesRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_MIDDLE);
                    cell.set(0xFFFF2D55, 0xFFB31835, R.drawable.msg_stories_archive,
                            LocaleController.getString(R.string.VivogramDisableStories),
                            LocaleController.getString(R.string.VivogramDisableStoriesDesc),
                            true, VivogramConfig.isDisableStories(), null);
                } else if (position == showIdDcRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_BOTTOM);
                    cell.set(0xFF34C759, 0xFF248A3D, R.drawable.msg_info,
                            LocaleController.getString(R.string.VivogramShowIdDc),
                            LocaleController.getString(R.string.VivogramShowIdDcDesc),
                            true, VivogramConfig.isShowIdDcProfile(), null);
                } else if (position == hiddenPasscodeRow) {
                    cell.setPositionInGroup(IOSSettingRowCell.POSITION_SINGLE);
                    cell.set(0xFF5856D6, 0xFF3634A3, R.drawable.msg_secret,
                            LocaleController.getString(R.string.VivogramHiddenPasscode),
                            LocaleController.getString(R.string.VivogramHiddenPasscodeDesc),
                            false, false, VivogramConfig.hasHiddenPasscode() ? LocaleController.getString(R.string.VivogramPasscodeEnabled) : LocaleController.getString(R.string.VivogramPasscodeNotSet));
                }
            }
        }
    }
}
