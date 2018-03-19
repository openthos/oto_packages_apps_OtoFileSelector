package com.openthos.fileselector;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.openthos.fileselector.adapter.DeviceAdapter;
import com.openthos.fileselector.adapter.FileAdapter;
import com.openthos.fileselector.bean.DeviceEntity;
import com.openthos.fileselector.bean.OperateType;
import com.openthos.fileselector.dialog.SaveFileDialog;
import com.openthos.fileselector.utils.ToastUtils;

import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private OperateType mOperateType;

    private List<DeviceEntity> mDeviceDatas;
    private List<File> mFileDatas;

    private DeviceAdapter mDeviceAdapter;
    private FileAdapter mFileAdapter;

    private LinearLayout mNameLayout;
    private EditText mFileName;
    private TextView mSelectPath;
    private TextView mSave;
    private TextView mCancel;
    private ImageView mBack;
    private ImageView mNewFile;
    private ListView mDeviceList;
    private ListView mFileList;

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
        mDeviceList = (ListView) findViewById(R.id.device_list);
        mFileList = (ListView) findViewById(R.id.file_list);
    }

    @Override
    public void initData() {
        mDeviceDatas = new ArrayList<>();
        mFileDatas = new ArrayList<>();
        mDeviceAdapter = new DeviceAdapter(this, mDeviceDatas);
        mFileAdapter = new FileAdapter(this, mFileDatas);
        mDeviceList.setAdapter(mDeviceAdapter);
        mFileList.setAdapter(mFileAdapter);
        loadDeviceInfos();
        loadFileInfos();
        mSelectPath.setText(getCurrentPath());
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
                    loadFileInfos();
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
                dirClick((String) view.getTag());
            }

            @Override
            public void onDoubleClick(View view) {
                dirClick((String) view.getTag());
            }
        });
    }

    private void dirClick(String path) {
        setCurrentPath(path);
        loadFileInfos();
    }

    public void setCurrentPath(String currentPath) {
        super.setCurrentPath(currentPath);
        mSelectPath.setText(currentPath);
    }

    private void loadDeviceInfos() {
        mDeviceDatas.clear();
        DeviceEntity entity = new DeviceEntity();
        entity.setName(getString(R.string.computer));
        entity.setDevicePath(ROOT_PATH);
        mDeviceDatas.add(entity);
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
                loadFileInfos();
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
                        loadFileInfos();
                    }
                }
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
}
