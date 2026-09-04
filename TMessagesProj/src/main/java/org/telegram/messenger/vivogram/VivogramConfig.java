package org.telegram.messenger.vivogram;

import android.content.Context;
import android.content.SharedPreferences;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.Utilities;

import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;

public class VivogramConfig {

    private static final String PREFS_NAME = "vivogram_config";
    private static SharedPreferences preferences;

    // Keys - Ghost Mode
    private static final String KEY_GHOST_MODE = "ghost_mode";
    private static final String KEY_GHOST_READ_HISTORY = "ghost_read_history";
    private static final String KEY_GHOST_TYPING = "ghost_typing";
    private static final String KEY_GHOST_ONLINE = "ghost_online";
    private static final String KEY_GHOST_READ_VOICE = "ghost_read_voice";

    // Keys - Messages & History
    private static final String KEY_SAVE_DELETED = "save_deleted_messages";
    private static final String KEY_SAVE_EDITS = "save_edited_messages";

    // Keys - Anti-Restrictions & Media
    private static final String KEY_ALLOW_SCREENSHOTS = "allow_screenshots";
    private static final String KEY_SAVE_RESTRICTED_MEDIA = "save_restricted_media";
    private static final String KEY_INFINITE_VIEW_ONCE = "infinite_view_once";
    private static final String KEY_AUTO_SAVE_EXPIRING_MEDIA = "auto_save_expiring_media";
    private static final String KEY_FAST_DOWNLOAD = "pref_fast_download";

    // Keys - Ads & Filters
    private static final String KEY_BLOCK_ADS = "block_ads";
    private static final String KEY_HIDE_REACTIONS = "hide_reactions";
    private static final String KEY_SPAM_FILTER_ENABLED = "spam_filter_enabled";
    private static final String KEY_SPAM_FILTER_KEYWORDS = "spam_filter_keywords";

    // Keys - QoL & UI
    private static final String KEY_FORWARD_NO_AUTHOR = "forward_no_author";
    private static final String KEY_SHOW_SECONDS = "show_seconds";
    private static final String KEY_CONFIRM_SEND_MEDIA = "confirm_send_media";
    private static final String KEY_DISABLE_STORIES = "disable_stories";
    private static final String KEY_SHOW_ID_DC_PROFILE = "show_id_dc_profile";

    // Keys - Hidden Chats
    private static final String KEY_HIDDEN_CHATS = "hidden_chats_set";
    private static final String KEY_HIDDEN_PASSCODE_HASH = "hidden_passcode_hash";

    // Runtime state (not saved)
    private static boolean showingHiddenChats = false;

    private static SharedPreferences getPreferences() {
        if (preferences == null && ApplicationLoader.applicationContext != null) {
            preferences = ApplicationLoader.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
        return preferences;
    }

    // Ghost Mode
    public static boolean isGhostMode() {
        SharedPreferences p = getPreferences();
        return p != null && p.getBoolean(KEY_GHOST_MODE, false);
    }

    public static void setGhostMode(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_GHOST_MODE, value).apply();
    }

    public static boolean isGhostReadHistory() {
        if (isGhostMode()) return true;
        SharedPreferences p = getPreferences();
        return p != null && p.getBoolean(KEY_GHOST_READ_HISTORY, false);
    }

    public static void setGhostReadHistory(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_GHOST_READ_HISTORY, value).apply();
    }

    public static boolean isGhostTyping() {
        if (isGhostMode()) return true;
        SharedPreferences p = getPreferences();
        return p != null && p.getBoolean(KEY_GHOST_TYPING, false);
    }

    public static void setGhostTyping(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_GHOST_TYPING, value).apply();
    }

    public static boolean isGhostOnline() {
        if (isGhostMode()) return true;
        SharedPreferences p = getPreferences();
        return p != null && p.getBoolean(KEY_GHOST_ONLINE, false);
    }

    public static void setGhostOnline(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_GHOST_ONLINE, value).apply();
    }

    public static boolean isGhostReadVoice() {
        if (isGhostMode()) return true;
        SharedPreferences p = getPreferences();
        return p != null && p.getBoolean(KEY_GHOST_READ_VOICE, false);
    }

    public static void setGhostReadVoice(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_GHOST_READ_VOICE, value).apply();
    }

    // Messages & History
    public static boolean isSaveDeleted() {
        SharedPreferences p = getPreferences();
        return p == null || p.getBoolean(KEY_SAVE_DELETED, true);
    }

    public static void setSaveDeleted(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_SAVE_DELETED, value).apply();
    }

    public static boolean isSaveEdits() {
        SharedPreferences p = getPreferences();
        return p == null || p.getBoolean(KEY_SAVE_EDITS, true);
    }

    public static void setSaveEdits(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_SAVE_EDITS, value).apply();
    }

    // Anti-Restrictions & Media
    public static boolean isAllowScreenshots() {
        SharedPreferences p = getPreferences();
        return p == null || p.getBoolean(KEY_ALLOW_SCREENSHOTS, true);
    }

    public static void setAllowScreenshots(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_ALLOW_SCREENSHOTS, value).apply();
    }

    public static boolean isSaveRestrictedMedia() {
        SharedPreferences p = getPreferences();
        return p == null || p.getBoolean(KEY_SAVE_RESTRICTED_MEDIA, true);
    }

    public static void setSaveRestrictedMedia(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_SAVE_RESTRICTED_MEDIA, value).apply();
    }

    public static boolean isInfiniteViewOnce() {
        SharedPreferences p = getPreferences();
        return p == null || p.getBoolean(KEY_INFINITE_VIEW_ONCE, true);
    }

    public static void setInfiniteViewOnce(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_INFINITE_VIEW_ONCE, value).apply();
    }

    public static boolean isAutoSaveExpiringMedia() {
        SharedPreferences p = getPreferences();
        return p != null && p.getBoolean(KEY_AUTO_SAVE_EXPIRING_MEDIA, false);
    }

    public static void setAutoSaveExpiringMedia(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_AUTO_SAVE_EXPIRING_MEDIA, value).apply();
    }

    public static boolean isFastDownload() {
        SharedPreferences p = getPreferences();
        return p != null && p.getBoolean(KEY_FAST_DOWNLOAD, true);
    }

    public static void setFastDownload(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_FAST_DOWNLOAD, value).apply();
    }

    // Ads & Filters
    public static boolean isBlockTelegramAds() {
        SharedPreferences p = getPreferences();
        return p == null || p.getBoolean(KEY_BLOCK_ADS, true);
    }

    public static void setBlockTelegramAds(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_BLOCK_ADS, value).apply();
    }

    public static boolean isHideReactions() {
        SharedPreferences p = getPreferences();
        return p != null && p.getBoolean(KEY_HIDE_REACTIONS, false);
    }

    public static void setHideReactions(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_HIDE_REACTIONS, value).apply();
    }

    public static boolean isSpamFilterEnabled() {
        SharedPreferences p = getPreferences();
        return p != null && p.getBoolean(KEY_SPAM_FILTER_ENABLED, false);
    }

    public static void setSpamFilterEnabled(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_SPAM_FILTER_ENABLED, value).apply();
    }

    public static String getSpamFilterKeywords() {
        SharedPreferences p = getPreferences();
        return p != null ? p.getString(KEY_SPAM_FILTER_KEYWORDS, "") : "";
    }

    public static void setSpamFilterKeywords(String value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putString(KEY_SPAM_FILTER_KEYWORDS, value).apply();
    }

    public static boolean checkIsSpam(String text) {
        if (!isSpamFilterEnabled() || text == null || text.isEmpty()) {
            return false;
        }
        String keywords = getSpamFilterKeywords();
        if (keywords == null || keywords.trim().isEmpty()) {
            return false;
        }
        String lowerText = text.toLowerCase();
        String[] rules = keywords.split("[,\n]");
        for (String rule : rules) {
            String cleanRule = rule.trim();
            if (cleanRule.isEmpty()) continue;
            try {
                if (cleanRule.startsWith("r:") || cleanRule.startsWith("regex:")) {
                    String pattern = cleanRule.substring(cleanRule.indexOf(':') + 1).trim();
                    if (text.matches("(?i).*" + pattern + ".*")) {
                        return true;
                    }
                } else {
                    if (lowerText.contains(cleanRule.toLowerCase())) {
                        return true;
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    // QoL & UI
    public static boolean isForwardNoAuthor() {
        SharedPreferences p = getPreferences();
        return p != null && p.getBoolean(KEY_FORWARD_NO_AUTHOR, false);
    }

    public static void setForwardNoAuthor(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_FORWARD_NO_AUTHOR, value).apply();
    }

    public static boolean isShowSeconds() {
        SharedPreferences p = getPreferences();
        return p != null && p.getBoolean(KEY_SHOW_SECONDS, false);
    }

    public static void setShowSeconds(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_SHOW_SECONDS, value).apply();
    }

    public static boolean isConfirmSendMedia() {
        SharedPreferences p = getPreferences();
        return p != null && p.getBoolean(KEY_CONFIRM_SEND_MEDIA, false);
    }

    public static void setConfirmSendMedia(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_CONFIRM_SEND_MEDIA, value).apply();
    }

    public static boolean isDisableStories() {
        SharedPreferences p = getPreferences();
        return p != null && p.getBoolean(KEY_DISABLE_STORIES, false);
    }

    public static void setDisableStories(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_DISABLE_STORIES, value).apply();
    }

    public static boolean isShowIdDcProfile() {
        SharedPreferences p = getPreferences();
        return p == null || p.getBoolean(KEY_SHOW_ID_DC_PROFILE, true);
    }

    public static void setShowIdDcProfile(boolean value) {
        SharedPreferences p = getPreferences();
        if (p != null) p.edit().putBoolean(KEY_SHOW_ID_DC_PROFILE, value).apply();
    }

    // Hidden Chats
    public static boolean isDialogHidden(long dialogId) {
        SharedPreferences p = getPreferences();
        if (p == null) return false;
        Set<String> set = p.getStringSet(KEY_HIDDEN_CHATS, null);
        if (set == null) {
            return false;
        }
        return set.contains(String.valueOf(dialogId));
    }

    public static void setDialogHidden(long dialogId, boolean hidden) {
        SharedPreferences p = getPreferences();
        if (p == null) return;
        Set<String> oldSet = p.getStringSet(KEY_HIDDEN_CHATS, null);
        Set<String> newSet = new HashSet<>(oldSet != null ? oldSet : new HashSet<>());
        if (hidden) {
            newSet.add(String.valueOf(dialogId));
        } else {
            newSet.remove(String.valueOf(dialogId));
        }
        p.edit().putStringSet(KEY_HIDDEN_CHATS, newSet).apply();
    }

    public static boolean hasHiddenChats() {
        SharedPreferences p = getPreferences();
        if (p == null) return false;
        Set<String> set = p.getStringSet(KEY_HIDDEN_CHATS, null);
        return set != null && !set.isEmpty();
    }

    public static boolean isShowingHiddenChats() {
        return showingHiddenChats;
    }

    public static void setShowingHiddenChats(boolean value) {
        showingHiddenChats = value;
    }

    public static boolean hasHiddenPasscode() {
        SharedPreferences p = getPreferences();
        if (p == null) return false;
        String hash = p.getString(KEY_HIDDEN_PASSCODE_HASH, "");
        return hash != null && !hash.isEmpty();
    }

    public static boolean checkHiddenPasscode(String passcode) {
        if (!hasHiddenPasscode()) {
            return true;
        }
        SharedPreferences p = getPreferences();
        if (p == null) return true;
        String storedHash = p.getString(KEY_HIDDEN_PASSCODE_HASH, "");
        String inputHash = hashPasscode(passcode);
        return storedHash.equals(inputHash);
    }

    public static void setHiddenPasscode(String passcode) {
        SharedPreferences p = getPreferences();
        if (p == null) return;
        if (passcode == null || passcode.isEmpty()) {
            p.edit().remove(KEY_HIDDEN_PASSCODE_HASH).apply();
        } else {
            p.edit().putString(KEY_HIDDEN_PASSCODE_HASH, hashPasscode(passcode)).apply();
        }
    }

    private static String hashPasscode(String passcode) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(passcode.getBytes("UTF-8"));
            return Utilities.bytesToHex(hash);
        } catch (Exception e) {
            return passcode;
        }
    }
}
