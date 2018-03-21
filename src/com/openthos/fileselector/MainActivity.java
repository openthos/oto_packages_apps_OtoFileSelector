package com.openthos.fileselector;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.openthos.fileselector.adapter.DeviceAdapter;
import com.openthos.fileselector.adapter.FileAdapter;
import com.openthos.fileselector.app.Constants;
import com.openthos.fileselector.bean.DeviceEntity;
import com.openthos.fileselector.bean.OperateType;
import com.openthos.fileselector.dialog.SaveFileDialog;
import com.openthos.fileselector.utils.DeviceUtils;
import com.openthos.fileselector.utils.ToastUtils;
import com.openthos.fileselector.view.CustomListView;

import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private OperateType mOperateType;
    private Handler mHandler;

    private List<DeviceEntity> mDeviceDatas;
    private List<File> mFileDatas;

    private DeviceAdapter mDeviceAdapter;
    private FileAdapter mFileAdapter;
    private UsbConnectReceiver mUsbConnectReceiver;

    private LinearLayout mNameLayout;
    private EditText mFileName;
    private TextView mSelectPath;
    private TextView mSave;
    private TextView mCancel;
    private ImageView mBack;
    private ImageView mNewFile;
    private CustomListView mDeviceList;
    private ListView mFileList;
    private View mLastDeviceView;

    private TextView mDesktop;
    private TextView mMusic;
    private TextView mVideo;
    private TextView mPicture;
    private TextView mDocument;
    private TextView mDownload;
    private TextView mRecycle;
    private TextView mComputer;
    private View[] mLeftBarView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mNameLayout = (LinearLayout) findViewById(R.id.name_layout);
        mFileName = (EditText) findViewById(R.id.file_name);
        mSelectPath = (TextView) findViewById(R.id.select_path);
        mSave = (TextView) findViewById(R.id.save);
        mCancel = (TextView) findViewById(R.id.cancel);
        mBack = (ImageView) findViewById(R.id.back);
        mNewFile = (ImageView) findViewById(R.id.new_file);
        mDeviceList = (CustomListView) findViewById(R.id.device_list);
        mFileList = (ListView) findViewById(R.id.file_list);

        mDesktop = (TextView) findViewById(R.id.tv_desk);
        mMusic = (TextView) findViewById(R.id.tv_music);
        mVideo = (TextView) findViewById(R.id.tv_video);
        mPicture = (TextView) findViewById(R.id.tv_picture);
        mDocument = (TextView) findViewById(R.id.tv_document);
        mDownload = (TextView) findViewById(R.id.tv_download);
        mRecycle = (TextView) findViewById(R.id.tv_recycle);
        mComputer = (TextView) findViewById(R.id.tv_computer);
    }

    @Override
    public void initData() {
        mHandler = new Handler();
        mDeviceDatas = new ArrayList<>();
        mFileDatas = new ArrayList<>();
        mLeftBarView = new View[]{
                mDesktop, mMusic, mVideo, mPicture, mDocument, mDownload, mRecycle, mComputer};
        mDeviceAdapter = new DeviceAdapter(this, mDeviceDatas);
        mFileAdapter = new FileAdapter(this, mFileDatas);
        mDeviceList.setAdapter(mDeviceAdapter);
        mFileList.setAdapter(mFileAdapter);
        mUsbConnectReceiver = new UsbConnectReceiver(this);

        setCurrentPath(Constants.ROOT_PATH);
        setSelectDeviceView(mComputer);

        loadDeviceInfos();
        loadFileInfos();

        String type = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(type) && type.equals("save")) {
            mOperateType = OperateType.SAVE;
            mNameLayout.setVisibility(View.VISIBLE);
            mSave.setText(getString(R.string.save));
        } else {
            mOperateType = OperateType.OPEN;
            mNameLayout.setVisibility(View.GONE);
            mSave.setText(getString(R.string.open));
        }
    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        mNewFile.setOnClickListener(this);
        mSave.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        for (int i = 0; i < mLeftBarView.length; i++) {
            mLeftBarView[i].setOnClickListener(this);
        }
        mFileAdapter.setOnFileClick(new OnFileClick() {
            @Override
            public void onClick(View view) {
                File file = (File) view.getTag();
                if (file.isFile()) {
                    mFileName.setText(file.getName());
                }
            }

            @Override
            public void onDoubleClick(View view) {
                File file = (File) view.getTag();
                if (file.isDirectory()) {
                    setCurrentPath(file.getAbsolutePath());
                }
                if (mOperateType == OperateType.OPEN && file.isFile()) {
                    setForResult(file);
                } else if (mOperateType == OperateType.SAVE && file.isFile()) {
                    ToastUtils.showToast(MainActivity.this,
                            getString(R.string.only_select_folders));
                }
            }
        });

        mDeviceAdapter.setOnFileClick(new OnFileClick() {
            @Override
            public void onClick(View view) {
                setCurrentPath((String) view.getTag());
                setSelectDeviceView(view);
            }

            @Override
            public void onDoubleClick(View view) {
                setCurrentPath((String) view.getTag());
                setSelectDeviceView(view);
            }
        });
    }

    public void setCurrentPath(String currentPath) {
        super.setCurrentPath(currentPath);
        loadFileInfos();
        mSelectPath.setText(currentPath);
    }

    private void loadDeviceInfos() {
        mDeviceDatas.clear();
        mDeviceDatas.addAll(DeviceUtils.getUsbList());
        mDeviceAdapter.refresh();
    }

    private void loadFileInfos() {
        mFileDatas.clear();
        List<File> files = getFileList(getCurrentPath());
        if (files != null) {
            mFileDatas.addAll(files);
            mFileAdapter.refresh();
        }
    }

    private List<File> getFileList(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists() || file.isFile()) {
            return null;
        }
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isHidden()) {
                    return false;
                }
                return true;
            }
        });
        return files == null ? null : Arrays.asList(files);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                setCurrentPath(getLastPath());
                break;
            case R.id.new_file:
                SaveFileDialog dialog = SaveFileDialog.getInstance(this);
                dialog.showDialog(getString(R.string.new_dir), new SaveFileDialog.OnSaveFileDialogListener() {
                    @Override
                    public void cancel(Dialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void save(Dialog dialog, String dirName) {
                        if (TextUtils.isEmpty(dirName)) {
                            ToastUtils.showToast(MainActivity.
                                    this, getString(R.string.new_file_name_can_not_null));
                        } else {
                            new File(getCurrentPath(), dirName).mkdir();
                            dialog.dismiss();
                            loadFileInfos();
                        }
                    }
                });
                break;
            case R.id.cancel:
                ToastUtils.showToast(MainActivity.this, getString(R.string.cancel));
                setResult(Activity.RESULT_OK);
                finish();
                break;
            case R.id.save:
                if (mOperateType == OperateType.SAVE) {
                    File selectFile = mFileAdapter.getSelectFile();
                    String fileName = mFileName.getText().toString();
                    if (TextUtils.isEmpty(fileName)) {
                        ToastUtils.showToast(MainActivity.this,
                                getString(R.string.please_enter_the_name_of_the_file));
                    } else if (selectFile == null) {
                        setForResult(new File(getCurrentPath(), fileName));
                    } else if (selectFile.isDirectory()) {
                        setForResult(new File(selectFile, fileName));
                    } else {
                        ToastUtils.showToast(MainActivity.this,
                                getString(R.string.only_select_folders));
                    }
                } else if (mOperateType == OperateType.OPEN) {
                    File selectFile = mFileAdapter.getSelectFile();
                    if (selectFile == null) {
                        ToastUtils.showToast(MainActivity.this,
                                getString(R.string.please_select_a_file));
                    } else if (selectFile.isFile()) {
                        setForResult(selectFile);
                    } else {
                        setCurrentPath(selectFile.getAbsolutePath());
                    }
                }
                break;
            case R.id.tv_desk:
                setCurrentPath(Constants.DESKTOP_PATH);
                setSelectDeviceView(mDesktop);
                break;
            case R.id.tv_music:
                setCurrentPath(Constants.MUSIC_PATH);
                setSelectDeviceView(mMusic);
                break;
            case R.id.tv_video:
                setCurrentPath(Constants.VIDEOS_PATH);
                setSelectDeviceView(mVideo);
                break;
            case R.id.tv_picture:
                setCurrentPath(Constants.PICTURES_PATH);
                setSelectDeviceView(mPicture);
                break;
            case R.id.tv_document:
                setCurrentPath(Constants.DOCUMENT_PATH);
                setSelectDeviceView(mDocument);
                break;
            case R.id.tv_download:
                setCurrentPath(Constants.DOWNLOAD_PATH);
                setSelectDeviceView(mDownload);
                break;
            case R.id.tv_recycle:
                setCurrentPath(Constants.RECYCLE_PATH);
                setSelectDeviceView(mRecycle);
                break;
            case R.id.tv_computer:
                setCurrentPath(Constants.ROOT_PATH);
                setSelectDeviceView(mComputer);
                break;
        }

    }

    private void setForResult(File file) {
        Intent intent = null;
        try {
            intent = Intent.parseUri(Uri.fromFile(file).toString(), 0);
            intent.putExtra("path", file.getAbsolutePath());
            setResult(Activity.RESULT_OK, intent);
            finish();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void setSelectDeviceView(View currentView) {
        if (mLastDeviceView != null && mLastDeviceView != currentView) {
            mLastDeviceView.setSelected(false);
        }
        currentView.setSelected(true);
        mLastDeviceView = currentView;
    }

    @Override
    protected void onStart() {
        mUsbConnectReceiver.registerReceiver();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        if (mUsbConnectReceiver != null) {
            mUsbConnectReceiver.unregisterReceiver();
        }
        super.onDestroy();
    }

    public class UsbConnectReceiver extends BroadcastReceiver {
        private MainActivity mActivity;
        private IntentFilter filter;

        public UsbConnectReceiver(Context context) {
            mActivity = (MainActivity) context;
            filter = new IntentFilter();
            filter.addAction(Intent.ACTION_MEDIA_CHECKING);
            filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            filter.addAction(Intent.ACTION_MEDIA_EJECT);
            filter.addAction(Intent.ACTION_MEDIA_REMOVED);
            filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);

            filter.addDataScheme("file");
        }

        public Intent registerReceiver() {
            return mActivity.registerReceiver(this, this.filter);
        }

        public void unregisterReceiver() {
            mActivity.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadDeviceInfos();
                }
            }, Constants.USB_REFRESH_TIME);
        }
    }
}
