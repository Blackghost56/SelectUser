package com.selectuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.selectuser.ui.main.MainFragment;
import com.selectuser.ui.main.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private MainViewModel mViewModel;

    MenuItem mSearchItem;
    MenuItem mAddItem;
    MenuItem mEditItem;
    MenuItem mDeleteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, MainFragment.newInstance()).commitNow();
        }


        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.getStateChange().observe(this, state -> MainActivity.this.invalidateOptionsMenu());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.list_manager_menu, menu);

        mSearchItem = menu.findItem(R.id.app_bar_search);
        mAddItem = menu.findItem(R.id.app_bar_add);
        mEditItem = menu.findItem(R.id.app_bar_edit);
        mDeleteItem = menu.findItem(R.id.app_bar_delete);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu");

        switch (mViewModel.mState){
            case MAIN_IDLE:
                mSearchItem.setVisible(true);
                mAddItem.setVisible(true);
                mEditItem.setVisible(false);
                mDeleteItem.setVisible(false);
                break;
            case SELECT:
                mSearchItem.setVisible(true);
                mAddItem.setVisible(true);
                mEditItem.setVisible(true);
                mDeleteItem.setVisible(true);
                break;
            case EDIT:
                mSearchItem.setVisible(false);
                mAddItem.setVisible(false);
                mEditItem.setVisible(false);
                mDeleteItem.setVisible(false);
                break;
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

        return super.onOptionsItemSelected(item);
    }


    //    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.d(TAG, "onStart");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d(TAG, "onResume");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d(TAG, "onPause");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.d(TAG, "onStop");
//    }
}