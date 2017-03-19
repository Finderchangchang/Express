package wai.express.ui;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;

import net.tsz.afinal.view.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import me.iwf.photopicker.PhotoPicker;
import wai.express.BaseActivity;
import wai.express.R;
import wai.express.method.CommonAdapter;
import wai.express.method.CommonViewHolder;
import wai.express.method.Utils;
import wai.express.model.AdvertModel;

/**
 * Created by Administrator on 2017/3/11.
 */

public class WenZiListActivity extends BaseActivity {
    @Bind(R.id.wenzi_lv)
    ListView wenzi_lv;
    List<AdvertModel> advertModelList;
    CommonAdapter<AdvertModel> commonAdapter;
    TitleBar titleBar;
    private String myType = "1";

    @Override
    public int setLayout() {
        return R.layout.ac_wenzi_list;
    }

    @Override
    public void initViews() {
        titleBar = (TitleBar) findViewById(R.id.toolbar);
        myType = getIntent().getStringExtra("Type");
        titleBar.setLeftClick(() -> finish());
        titleBar.setRightClick(() -> {
            //跳转到添加界面
            switch (myType) {
                case "1":
                    Intent intent = new Intent(WenZiListActivity.this, AddWenziActivity.class);
                    startActivityForResult(intent, 10);
                    break;
                default:
                    PhotoPicker.builder()
                            .setPhotoCount(1)
                            .setShowCamera(true)
                            .setShowGif(true)
                            .setPreviewEnabled(false)
                            .start(this, PhotoPicker.REQUEST_CODE);
                    break;
            }

        });
        advertModelList = new ArrayList<>();
        commonAdapter = new CommonAdapter<AdvertModel>(this, advertModelList, R.layout.item_wenzi) {
            @Override
            public void convert(CommonViewHolder holder, AdvertModel advertModel, int position) {

                if (myType.equals("1")) {
                    if (advertModel.getContent().length() > 16) {
                        advertModel.setContent(advertModel.getContent().substring(0, 16) + "...");
                    }
                    holder.setVisible(R.id.item_wenzi, true);
                    holder.setVisible(R.id.item_wenzi_picture, false);
                    holder.setText(R.id.item_wenzi, advertModel.getContent());
                } else {
                    holder.setVisible(R.id.item_wenzi_picture, true);
                    holder.setVisible(R.id.item_wenzi, false);
                    System.out.println("h::" + advertModel.getContent());
                    holder.setGliImage(R.id.item_wenzi_picture, advertModel.getContent());
                }
                holder.setText(R.id.wenzi_time, advertModel.getUpdatedAt());

                holder.setOnClickListener(R.id.item_btn_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(WenZiListActivity.this);
                        builder.setMessage("确定要删除吗？");
                        builder.setNegativeButton("确定", (dialogInterface, i) -> {
                            //删除文字、图片
                            DeleteData(advertModel.getObjectId());
                        });
                        builder.setPositiveButton("取消", null);
                        builder.show();
                    }
                });
            }
        };
        wenzi_lv.setAdapter(commonAdapter);
    }

    @Override
    public void initEvents() {
        SearchData();
    }

    //删除指定数据
    private void DeleteData(String id) {
        AdvertModel advertModel = new AdvertModel();
        advertModel.setObjectId(id);
        advertModel.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastShort("删除成功");
                    SearchData();
                } else if (e.getErrorCode() == 9010) {
                    ToastShort(getResources().getString(R.string.chaoshi));

                } else if (e.getErrorCode() == 9016) {
                    ToastShort(getResources().getString(R.string.wuwang));

                } else {
                    ToastShort(getResources().getString(R.string.neibu));

                }
            }
        });
    }

    //从数据库查询数据文字
    private void SearchData() {
        BmobQuery<AdvertModel> query = new BmobQuery<>();
        query.addWhereEqualTo("type", myType);
        query.findObjects(new FindListener<AdvertModel>() {
            @Override
            public void done(List<AdvertModel> list, BmobException e) {
                if (e == null) {
                    advertModelList.removeAll(advertModelList);
                    advertModelList.addAll(list);
                    commonAdapter.notifyDataSetChanged();
                } else if (e.getErrorCode() == 9010) {
                    ToastShort(getResources().getString(R.string.chaoshi));
                } else if (e.getErrorCode() == 9016) {
                    ToastShort(getResources().getString(R.string.wuwang));
                } else {
                    ToastShort(getResources().getString(R.string.neibu));
                }
            }
        });
    }

    String path;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 11) {
            SearchData();
        }
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                path = photos.get(0);
                BmobFile bmobFile = new BmobFile(new File(path));
                bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            path = bmobFile.getFileUrl();
                            AdvertModel model = new AdvertModel();
                            model.setContent(path);
                            model.setType("2");
                            model.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        SearchData();
                                        ToastShort("添加成功");
                                    } else {
                                        ToastShort("添加失败");
                                    }
                                }
                            });
                        } else {
                            ToastShort("上传文件失败：" + e.getMessage());
                        }
                    }

                    @Override
                    public void onProgress(Integer value) {
                        // 返回的上传进度（百分比）
                    }
                });
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
