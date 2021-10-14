package com.reedtech.electronics.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.reedtech.electronics.R;
import com.reedtech.electronics.Welcome_Activity;
import com.reedtech.electronics.walkthrough_Activity;

public class Walkthrough_Adapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    LinearLayout dot_layout;
    TextView[] mdots = new TextView[3];
    public ImageView imageView, back_arrow;
    TextView textView;
    TextView textView2;
    private Button btn_next, btn_skip;
    RelativeLayout rl;

    public Walkthrough_Adapter(Context context) {
        this.context = context;
    }

    public int[] images = {R.drawable.airpods2, R.drawable.jbl_charge3, R.drawable.controller};
    public String[] header = {"Buy your favourite Gadgets", "Affordable Quality", "Tested and Trusted"};
    public String[] desc= {"Be the first to own one of the finest gadgets in the World after each release",
            "Save big on future purchases of the best quality your money can buy",
            "Avoid unnecessary stress when you shop with us. \n" +
                    "Welcome to the Future"};

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.from(context).inflate(R.layout.walkthrough_layout,container,false);

        imageView = view.findViewById(R.id.wt_pic);
        btn_skip = view.findViewById(R.id.btn_skip);
        btn_next = view.findViewById(R.id.btn_next);
        back_arrow = view.findViewById(R.id.wt_back_arrow);
        rl = view.findViewById(R.id.wt_rl);
        textView = view.findViewById(R.id.header);
        textView2 = view.findViewById(R.id.description);
        dot_layout = view.findViewById(R.id.dot_layout);

        addDotsIndicator(0);
        imageView.setImageResource(images[position]);
        textView.setText(header[position]);
        textView2.setText(desc[position]);

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Welcome_Activity.class);
                context.startActivity(intent);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(walkthrough_Activity.viewPager.getCurrentItem()!=2){
                    walkthrough_Activity.viewPager.setCurrentItem(walkthrough_Activity.viewPager.getCurrentItem()+1);

                }else{
                    Intent intent = new Intent(context, Welcome_Activity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }

            }
        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(walkthrough_Activity.viewPager.getCurrentItem()!=0){
                    walkthrough_Activity.viewPager.setCurrentItem(walkthrough_Activity.viewPager.getCurrentItem()-1);
                }
            }
        });

        container.addView(view);

        return view;
    }

    public void addDotsIndicator(int position){

        for(int i=0; i<=2; i++){
            mdots[i]=new TextView(context);
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(context.getResources().getColor(R.color.dgrey2));


            dot_layout.addView(mdots[i]);
        }
        //mdots[0].setTextColor(context.getResources().getColor(R.color.white));


    }

    public ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            if(position==0){

                //mdots[0].setTextColor(context.getResources().getColor(R.color.dgrey2));
                mdots[position].setTextColor(context.getResources().getColor(R.color.white));

            }

            if (position==1){

                rl.setBackgroundColor(context.getResources().getColor(R.color.white));
                back_arrow.setImageResource(R.drawable.back_arrow_black);
                btn_next.setBackground(context.getResources().getDrawable(R.drawable.btn_back2));
                btn_skip.setTextColor(context.getResources().getColor(R.color.black));
                textView.setTextColor(context.getResources().getColor(R.color.black));
                textView2.setTextColor(context.getResources().getColor(R.color.black));

                for(int i=0; i<=2; i++){
                    mdots[i].setTextColor(context.getResources().getColor(R.color.ash));
                }
                mdots[position].setTextColor(context.getResources().getColor(R.color.black));

            }

            if (position==2){
                rl.setBackgroundColor(context.getResources().getColor(R.color.dgrey3));
                textView.setTextColor(context.getResources().getColor(R.color.white));
                back_arrow.setImageResource(R.drawable.back_arrow_white);
                btn_next.setBackground(context.getResources().getDrawable(R.drawable.btn_back4));
                btn_next.setText("Finish");
                btn_skip.setTextColor(context.getResources().getColor(R.color.white));
                textView2.setTextColor(context.getResources().getColor(R.color.white));

                for(int i=0; i<=2; i++){
                    mdots[i].setTextColor(context.getResources().getColor(R.color.dgrey2));
                }
                mdots[position].setTextColor(context.getResources().getColor(R.color.white));

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
