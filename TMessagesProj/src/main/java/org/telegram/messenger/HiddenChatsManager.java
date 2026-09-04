package org.telegram.messenger;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.PasscodeActivity;

import java.util.HashSet;
import java.util.Set;

public class HiddenChatsManager {

    private static volatile HiddenChatsManager[] Instance = new HiddenChatsManager[UserConfig.MAX_ACCOUNT_COUNT];
    private final int currentAccount;
    private boolean isUnlocked = false;
    private final Set<Long> hiddenDialogIds = new HashSet<>();
    private static final String PREF_NAME = "vivogram_hidden_chats_";
    private static final String KEY_HIDDEN_IDS = "hidden_ids";

    public static HiddenChatsManager getInstance(int account) {
        HiddenChatsManager localInstance = Instance[account];
        if (localInstance == null) {
            synchronized (HiddenChatsManager.class) {
                localInstance = Instance[account];
                if (localInstance == null) {
                    Instance[account] = localInstance = new HiddenChatsManager(account);
                }
            }
        }
        return localInstance;
    }

    public static HiddenChatsManager getInstance() {
        return getInstance(UserConfig.selectedAccount);
    }

    private HiddenChatsManager(int account) {
        this.currentAccount = account;
        loadHiddenChats();
    }

    private void loadHiddenChats() {
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences(PREF_NAME + currentAccount, Context.MODE_PRIVATE);
        Set<String> set = preferences.getStringSet(KEY_HIDDEN_IDS, null);
        hiddenDialogIds.clear();
        if (set != null) {
            for (String str : set) {
                try {
                    hiddenDialogIds.add(Long.parseLong(str));
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void saveHiddenChats() {
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences(PREF_NAME + currentAccount, Context.MODE_PRIVATE);
        Set<String> set = new HashSet<>();
        for (Long id : hiddenDialogIds) {
            set.add(String.valueOf(id));
        }
        preferences.edit().putStringSet(KEY_HIDDEN_IDS, set).apply();
    }

    public boolean isSessionUnlocked() {
        return isUnlocked;
    }

    public void unlockSession() {
        this.isUnlocked = true;
    }

    public void lockSession() {
        if (this.isUnlocked) {
            this.isUnlocked = false;
            NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
        }
    }

    public boolean isDialogHidden(long dialogId) {
        return hiddenDialogIds.contains(dialogId);
    }

    public void setDialogHidden(long dialogId, boolean hidden) {
        if (hidden) {
            hiddenDialogIds.add(dialogId);
        } else {
            hiddenDialogIds.remove(dialogId);
        }
        saveHiddenChats();
        NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload);
    }

    public Set<Long> getHiddenDialogIds() {
        return new HashSet<>(hiddenDialogIds);
    }

    public int getHiddenCount() {
        return hiddenDialogIds.size();
    }

    public void openHiddenChatsWithAuth(Activity activity, BaseFragment fragment, Runnable onSuccess) {
        if (activity == null || fragment == null) {
            return;
        }
        if (isUnlocked) {
            if (onSuccess != null) {
                onSuccess.run();
            }
            return;
        }

        // Если в приложении включен код-пароль, запрашиваем его
        if (SharedConfig.passcodeHash.length() > 0) {
            PasscodeActivity passcodeActivity = new PasscodeActivity(PasscodeActivity.TYPE_PASSCODE_CHECK);
            passcodeActivity.setDelegate(new PasscodeActivity.PasscodeActivityDelegate() {
                @Override
                public void didAcceptedPassword(PasscodeActivity activity) {
                    isUnlocked = true;
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                }
            });
            fragment.presentFragment(passcodeActivity);
        } else {
            // Если код-пароль не установлен, разблокируем напрямую
            isUnlocked = true;
            if (onSuccess != null) {
                onSuccess.run();
            }
        }
    }
}
