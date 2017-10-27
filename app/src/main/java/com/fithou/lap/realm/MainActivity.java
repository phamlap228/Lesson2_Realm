package com.fithou.lap.realm;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm mRealm;
    private ListView mListViewName;
    private ArrayAdapter<Task> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListViewName = findViewById(R.id.listViewName);
        //đăng ký contextmenu
        registerForContextMenu(mListViewName);
        //khởi tạo để tạo database realm.
        Realm.init(this);
        //dùng nó để tạo bảng truy vấn dữ liệu
        //thêm sửa xóa.
        mRealm = Realm.getDefaultInstance();
        //đổ dl lên listview
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getData());
        mListViewName.setAdapter(mAdapter);
        getSupportActionBar().setTitle("Quản lý công việc");
    }

    private RealmResults<Task> getData() {
        return mRealm.where(Task.class).findAll();
//        RealmResults<Task> listData= mRealm.where(Task.class).findAll();
//        return listData;
    }

    private void reLoadDB() {
        ArrayAdapter<Task> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getData());
        mListViewName.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnAdd) {
            //thêm dl vào realm
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    Random mID = new Random();

                    Task mMytask = new Task(mID.nextInt(220), "Code tính năng của: " + mID.nextInt(200));
                    mRealm.copyToRealmOrUpdate(mMytask);
                    reLoadDB();
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //lấy vị trí item click
        AdapterView.AdapterContextMenuInfo mMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Task mSelectedItem = mAdapter.getItem(mMenuInfo.position);
        //truy vấn để lấy ra đối tượng từ Realm
        final Task mMytask = mRealm.where(Task.class).equalTo("mID", mSelectedItem.getID()).findFirst();
        if (mMytask != null) {
            switch (item.getItemId()) {
                case R.id.mnUpdate:
                    //mMytask.deleteFromRealm();
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            Random rd = new Random();
                            mMytask.setName("Update dự án: "+rd.nextInt(20));
                            reLoadDB();
                        }
                    });
                    break;
                case R.id.mnDelete:
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            mMytask.deleteFromRealm();
                            reLoadDB();
                        }
                    });
                    break;
            }
        }
        return super.onContextItemSelected(item);
    }
}
