package com.example.administrator.practice.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.example.administrator.practice.R;
import com.example.administrator.practice.adapter.SectionsPagerAdapter;
import com.example.administrator.practice.bean.BottomPopupWindow;
import com.example.administrator.practice.fragment.FindFragment;
import com.example.administrator.practice.fragment.NoteFragment;
import com.example.administrator.practice.fragment.RecommendFragment;
import com.example.administrator.practice.utils.BarUtils;
import com.example.administrator.practice.utils.PrefUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends SlidingFragmentActivity implements BottomNavigationBar.OnTabSelectedListener
        , ViewPager.OnPageChangeListener {
    private static int TAG_TAKE_PHOTO = 0;//标记
    private ViewPager viewPager;
    private BottomNavigationBar bottomNavigationBar;
    private List<Fragment> listFrgments;
    private SlidingMenu slidingMenu;
    private TextView mTextView_ours;
    private TextView mTextview_year;
    private TextView tv_statusBar_slide;
    private TextView tv_statusBar_mian;
    private CircleImageView mCircleImageView;
    private BottomPopupWindow mBottomPopupWindow;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    // 定义一个变量，标识是否退出
    private static boolean isExit = false;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    private Uri mImageUri;
    private Bitmap mBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.side_item);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        tv_statusBar_slide = (TextView) findViewById(R.id.tv_statusBar_slide);
        tv_statusBar_mian = (TextView) findViewById(R.id.tv_statusBar_mian);
        boolVersion();
        //  setColor(this, R.color.bar);
        //加载侧滑菜单界面  
        initSlidingMenu();
        initId();
        OnClickListener();
        initView();
    }

    private void boolVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 改变titlebar的高度
            int statusbarHeight = BarUtils.getStatusBarHeight(this);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv_statusBar_slide.getLayoutParams();
            layoutParams.height = statusbarHeight;
            tv_statusBar_slide.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) tv_statusBar_mian.getLayoutParams();
            layoutParams1.height = statusbarHeight;
            tv_statusBar_mian.setLayoutParams(layoutParams1);
        } else {
            tv_statusBar_mian.setVisibility(View.GONE);
            tv_statusBar_slide.setVisibility(View.GONE);
        }
    }

    private void initSlidingMenu() {
        //slidingMenu = new SlidingMenu(this);
        slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);//左/右侧滑出  
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏触摸监听  
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //设置滑动时菜单的是否淡入淡出  
        slidingMenu.setFadeEnabled(false);
        //设置淡入淡出的比例  
        slidingMenu.setFadeDegree(0.8f);
        //设置滑动时拖拽效果:即slidingmenu的遮盖滑出效果  
        slidingMenu.setBehindScrollScale(0);

    }

    private void initView() {
        initBottomNavigationBar();
        initViewPager();
    }

    private void initViewPager() {
        listFrgments = new ArrayList<>();
        listFrgments.add(new NoteFragment(slidingMenu));
        listFrgments.add(new RecommendFragment());
        listFrgments.add(new FindFragment());
        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), listFrgments));
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);

    }

    private void initBottomNavigationBar() {
        bottomNavigationBar.setTabSelectedListener(this);

        bottomNavigationBar.clearAll();
//        点击的时候有水波纹效果
        //bottomNavigationBar.setBackgroundStyle(BACKGROUND_STYLE_RIPPLE);
        bottomNavigationBar.setMode(bottomNavigationBar.MODE_DEFAULT);
//        换挡模式，未选中的Item不会显示文字，选中的会显示文字。在切换的时候会有一个像换挡的动画
        //bottomNavigationBar.setMode(MODE_SHIFTING);
        bottomNavigationBar.setFitsSystemWindows(true);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.button_note, "便签").setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.drawable.button_recommend, "推荐").setActiveColorResource(R.color.teal))
                .addItem(new BottomNavigationItem(R.drawable.button_find, "发现"))
                .initialise();
    }

    private void initId() {
        viewPager = (ViewPager) findViewById(R.id.view_pager_home);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.buttonbar);
        mTextView_ours = (TextView) findViewById(R.id.textView);
        mTextview_year = (TextView) findViewById(R.id.textview_year);

        mCircleImageView = (CircleImageView) findViewById(R.id.circleImageView);
        //第一步:取出字符串形式的Bitmap
        String photo = null;
        if (TAG_TAKE_PHOTO == 1) {
            photo = PrefUtils.getString(MainActivity.this, "image", "");//照相机
        } else if (TAG_TAKE_PHOTO == 2) {
            photo = PrefUtils.getString(MainActivity.this, "image_take", "");//相册
        } else {
            Glide.with(this).load(R.drawable.hp).into(mCircleImageView);
        }
        if (photo != null) {
            //第二步:利用Base64将字符串转换为ByteArrayInputStream
            byte[] byteArray = Base64.decode(photo, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            //第三步:利用ByteArrayInputStream生成Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
            mCircleImageView.setImageBitmap(bitmap);
        }
    }

    private void OnClickListener() {
        mTextView_ours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OursActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                // Toast.makeText(MainActivity.this, "关于我们", Toast.LENGTH_SHORT).show();
            }
        });
        mTextview_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TodaysActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                Toast.makeText(MainActivity.this, "那年今日", Toast.LENGTH_SHORT).show();
            }
        });
        //Popupwindow从底部弹出
        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化Popupwindow
                mBottomPopupWindow = new BottomPopupWindow(MainActivity.this, itemsOnClick);
                // 设置Popupwindow显示位置（从底部弹出）
                mBottomPopupWindow.showAtLocation(MainActivity.this.findViewById(R.id.main)
                        , Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    //创建file对象，用于储存拍照后的图片
                    File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                    if (outputImage.exists()) {
                        outputImage.delete();
                    } else {
                        try {
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >= 24) {
                            mImageUri = FileProvider.getUriForFile(MainActivity.this
                                    , "com.example.administrator.practice.view.fileprovider", outputImage);
                        } else {
                            mImageUri = Uri.fromFile(outputImage);
                        }
                    }
                    //启动相机程序
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, TAKE_PHOTO);
                    mBottomPopupWindow.dismiss();
                    break;
                case R.id.btn_select_photo:
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        openAlbum();
                    }
                    mBottomPopupWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);//打开相机
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "哈哈哈", Toast.LENGTH_SHORT).show();
                }
                break;
            default:

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //将拍摄的照片设置为头像
                    try {
                        mBitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(mImageUri));
                        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        mBitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
                        PrefUtils.setString(MainActivity.this, "image", imageString);
                        TAG_TAKE_PHOTO = 1;
                        mCircleImageView.setImageBitmap(mBitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        hanleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitkat(data);
                    }
                }
            default:
                break;
        }
    }

    private void handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);//根据图片路径显示图片

    }

    @TargetApi(19)
    private void hanleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri 则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri 则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri 直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
            PrefUtils.setString(MainActivity.this, "image_take", imageString);
            TAG_TAKE_PHOTO = 2;
            mCircleImageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "出错了", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex
                        (MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;

    }

    @Override
    public void onTabSelected(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationBar.selectTab(position);
        if (position != 0) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "记得经常打开我喔", Toast.LENGTH_SHORT).show();
    }
}
