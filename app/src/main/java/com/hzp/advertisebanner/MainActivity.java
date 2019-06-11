package com.hzp.advertisebanner;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private ViewPager viewPager;

    private int[] imageResIds = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
    };

    private String[] descs = {
            "巩俐不低俗，我就不能低俗",
            "扑树又回来啦！再唱经典老歌引万人大合唱",
            "揭秘北京电影如何升级",
            "乐视网TV版大派送",
            "热血屌丝的反杀",
    };

    /**展示图片的imageView**/
    private ImageView[] imageViews = new ImageView[imageResIds.length];
    /**保存创建的点的操作**/
    private View[] dots = new View[imageResIds.length];

    /**当前选中的点**/
    private View currentselectview;

    private TextView mViewPagerText;

    private LinearLayout mLLRootDot;

    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            //实现切换界面操作
            switchPager();
        };
    };
    /**
     * 切换viewpager界面
     *
     * 2016-10-28 下午4:03:27
     */
    protected void switchPager() {
        //0 -> 1  -> 2 -> 3 -> 4 -> 5
        int currentItem = viewPager.getCurrentItem();//获取当前显示的界面的索引
        currentItem ++;
        viewPager.setCurrentItem(currentItem);//设置显示当前界面
        //执行下一次的切换
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    protected void onStart() {
        //发送一个定时消息，执行代码，会在3秒之后发送消息给handler，如果不执行代码，不会再发送消息
        handler.sendEmptyMessageDelayed(0, 3000);
        super.onStart();
    }
    @Override
    protected void onStop() {
        //不能发送消息
        handler.removeMessages(0);
        super.onStop();
    }

    /**
     * 初始化控件
     *
     * 2016-10-28 上午10:56:14
     */
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPagerText = (TextView) findViewById(R.id.viewpager_text);
        mLLRootDot = (LinearLayout) findViewById(R.id.root_dot);

        //根据图片的张数创建相应个数的imageView,并将图片设置个相应imageView显示
        for (int i = 0; i < imageResIds.length; i++) {
            //创建imageView
            createImageView(i);
            //根据图片的张数创建点的个数
            createDot(i);
        }

        //当viewpager的界面切换的时候，更改显示文本
        //设置viewpager的界面切换监听,当刚进入界面的时候不会调用，所以需要单独设置第一个界面的文本
        viewPager.setOnPageChangeListener(listener);
        change(0);//设置viewpager第一个界面的文本

        //获取viewpager无限滑动的最大条目数
        maxpage = imageResIds.length * 1000 * 100;

        //设置往前也可以实现无限滑动
        int currentItem = maxpage / 2;

        //跟listview相似
        viewPager.setAdapter(new Myadapter());//设置viewpager的adapter(pageAdapter)

        viewPager.setCurrentItem(currentItem);//设置viewpager当前显示的条目，item：条目的索引

    }


    /**viewpager的界面切换监听**/
    private OnPageChangeListener listener = new OnPageChangeListener() {
        //当界面切换完成调用的方法
        //position : 切换完成的界面的索引
        @Override
        public void onPageSelected(int position) {
            //设置显示相应界面的文本
            change(position);
        }
        //当viewpager滑动的时候调用的方法
        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            // TODO Auto-generated method stub

        }
        //当滑动状态改变的时候调用的方法
        //state : Viewpager的切换状态
        @Override
        public void onPageScrollStateChanged(int state) {
            //ViewPager.SCROLL_STATE_IDLE//空闲的状态，停止滑动的状态
            //ViewPager.SCROLL_STATE_DRAGGING;//触摸滑动的状态
            //ViewPager.SCROLL_STATE_SETTLING //滑动到最后一个条目的状态
            //当手动滑动的时候不能进行自动滑动，当不滑动的时候重新进行自动滑动
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                handler.sendEmptyMessageDelayed(0, 3000);
            }else{
                handler.removeMessages(0);
            }
        }
    };

    private int maxpage;

    /**
     * 设置切换的文本和点
     *@param position ： 切换完成的界面的索引
     * 2016-10-28 下午3:04:44
     */
    protected void change(int position) {
        //通过求余的形式通过显示文本和点的索引
        position = position % imageResIds.length;
        mViewPagerText.setText(descs[position]);

        //切换点的操作
        if (currentselectview != null) {
            currentselectview.setSelected(false);
        }
        dots[position].setSelected(true);//表示选中控件
        //当当前的点变成白色的时候，上一个点要变成黑色
        currentselectview = dots[position];
    }
    /**
     * 创建点
     *@param i
     * 2016-10-28 下午3:16:16
     */
    private void createDot(int i) {
        //将创建的点保存到数组中方便操作
        dots[i] = new View(this);
        dots[i].setBackgroundResource(R.drawable.selector_dot);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5, 5);
        //跟布局文件中的android:layout_marginRight属性的含义一样的，设置距离右边的距离
        params.rightMargin = 5;
        //将属性设置给点生效
        dots[i].setLayoutParams(params);//设置属性给view对象
        //将创建的点显示在界面上
        mLLRootDot.addView(dots[i]);
    }
    /**
     * 创建imageView
     *@param i
     * 2016-10-28 下午2:44:48
     */
    private void createImageView(int i) {
        //将创建的imageView保存到数组中，方便后面使用
        imageViews[i] = new ImageView(this);
        //给相应的imageView设置显示相应的图片
        imageViews[i].setBackgroundResource(imageResIds[i]);
    }

    /**viewpager的adapter**/
    private class Myadapter extends PagerAdapter {
        //设置viewpager的条目的个数，跟listview的adapter的getCount方法是一致的
        @Override
        public int getCount() {
            //return imageViews.length;
            return maxpage;
        }
        //设置是否显示相应界面的操作，因为有左右两种切换操作，所以需要知道相应的切换操作，应该切换到那个界面
        //view : viewpager的显示的界面
        //object : 创建的显示的界面，也是添加到viewpager中实际显示的界面，instantiateItem返回的
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        //给viewpager添加显示界面，在instantiateItem方法中就需要将显示的界面创建出来，并设置给viewpager
        //container : viewpager
        //position :条目的索引
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //通过求余的形式通过图片的索引
            position = position % imageResIds.length;

            //根据条目的索引获取相应的imageView
            ImageView imageView = imageViews[position];
            container.addView(imageView);//将imageView添加给viewpager
            //添加什么view对象，返回什么view对象
            return imageView;
        }
        //当界面删除的时候调用方法
        //container : Viewpager
        //position : 条目的索引
        //object : instantiateItem返回对象
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            //super.destroyItem(container, position, object);//抛出一个异常
            container.removeView((View) object);
        }

    }

}
