package com.selectuser;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.selectuser.dialog.PinDialog;
import com.selectuser.ui.main.MainFragment;
import com.selectuser.ui.main.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private MainViewModel mViewModel;

    ActionBar mActionBar;
    MenuItem mSearchItem;
    MenuItem mAddItem;
    MenuItem mEditItem;
    MenuItem mDeleteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Show the Up button in the action bar.
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            setActionBarTitle(R.string.msg_list_manager_title);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, MainFragment.newInstance()).commitNow();
        }

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.getStateChange().observe(this, state -> MainActivity.this.invalidateOptionsMenu());

        mViewModel.getOpenFragment().observe(this, fragment -> getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit());


        mViewModel.getPopBackStack().observe(this, aVoid -> getSupportFragmentManager().popBackStack());

        mViewModel.getRequestPin().observe(this, aVoid -> {
            PinDialog pinDialog = new PinDialog();
            pinDialog.show(getSupportFragmentManager(), "pinDialog");
        });

        mViewModel.getBackPressed().observe(this, aVoid -> onBackPressed());
    }

    public void setActionBarTitle(@StringRes int res){
        if (mActionBar != null)
            mActionBar.setTitle(res);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_manager_menu, menu);

        mSearchItem = menu.findItem(R.id.app_bar_search);
        mAddItem = menu.findItem(R.id.app_bar_add);
        mEditItem = menu.findItem(R.id.app_bar_edit);
        mDeleteItem = menu.findItem(R.id.app_bar_delete);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MainViewModel.State state = mViewModel.getStateChange().getValue();
        if (state != null) {
            switch (state) {
                case MAIN_IDLE:
                    setActionBarTitle(R.string.msg_list_manager_title);
                    mSearchItem.setVisible(true);
                    mAddItem.setVisible(true);
                    mEditItem.setVisible(false);
                    mDeleteItem.setVisible(false);
                    break;
                case SELECT:
                    setActionBarTitle(R.string.msg_list_manager_title);
                    mSearchItem.setVisible(true);
                    mAddItem.setVisible(true);
                    mEditItem.setVisible(true);
                    mDeleteItem.setVisible(true);
                    break;
                case EDIT:
                    setActionBarTitle(R.string.msg_list_manager_title_edit);
                    mSearchItem.setVisible(false);
                    mAddItem.setVisible(false);
                    mEditItem.setVisible(false);
                    mDeleteItem.setVisible(false);
                    break;
                case ADD:
                    setActionBarTitle(R.string.msg_list_manager_title_add);
                    mSearchItem.setVisible(false);
                    mAddItem.setVisible(false);
                    mEditItem.setVisible(false);
                    mDeleteItem.setVisible(false);
                    break;
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item == mAddItem){
            mViewModel.onAddPressed();
            return true;
        } else if (item == mEditItem){
            mViewModel.onEditPressed();
            return true;
        } else if (item == mDeleteItem){
            mViewModel.onDeletePressed();
            return true;
        }

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        mViewModel.onBackPressed(backStackEntryCount);
        super.onBackPressed();
    }
}