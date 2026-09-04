package org.telegram.ui;

import android.os.Bundle;
import android.view.WindowManager;

import org.telegram.messenger.HiddenChatsManager;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.Theme;

public class HiddenDialogsActivity extends DialogsActivity {

    public HiddenDialogsActivity() {
        this(new Bundle());
    }

    public HiddenDialogsActivity(Bundle args) {
        super(args);
    }

    @Override
    public boolean onFragmentCreate() {
        if (!HiddenChatsManager.getInstance(currentAccount).isSessionUnlocked()) {
            return false;
        }
        return super.onFragmentCreate();
    }

    @Override
    public ActionBar createActionBar(android.content.Context context) {
        ActionBar bar = super.createActionBar(context);

        if (bar != null) {
            bar.setTitle(LocaleController.getString("HiddenChatsTitle", R.string.HiddenChatsTitle));
            bar.setBackButtonImage(R.drawable.ic_ab_back);
            bar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
                @Override
                public void onItemClick(int id) {
                    if (id == -1) {
                        exitHiddenMode();
                    }
                }
            });
        }
        return bar;
    }

    public void exitHiddenMode() {
        HiddenChatsManager.getInstance(currentAccount).lockSession();
        finishFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getParentActivity() != null) {
            getParentActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
        if (!HiddenChatsManager.getInstance(currentAccount).isSessionUnlocked()) {
            finishFragment();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        HiddenChatsManager.getInstance(currentAccount).lockSession();
        if (getParentActivity() != null) {
            getParentActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    @Override
    public boolean onBackPressed() {
        exitHiddenMode();
        return false;
    }
}
