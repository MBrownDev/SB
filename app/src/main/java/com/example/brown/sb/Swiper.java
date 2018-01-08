package com.example.brown.sb;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by theon_000 on 12/29/2015.
 */
public class Swiper extends Fragment {
    String pageData[];	//Stores the text to swipe.
    LayoutInflater inflater;	//Used to create individual pages
    ViewPager vp;	//Reference to class to swipe views
    int[] covers;
    ImageButton slide;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pager_screen,container,false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Get the data to be swiped through
        //pageData=getResources().getStringArray(R.array.desserts);
        //get an inflater to be used to create single pages
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Reference ViewPager defined in activity
        vp=(ViewPager)getActivity().findViewById(R.id.pager);
        //set the adapter that will create the individual pages
        vp.setAdapter(new MyPagesAdapter());

        //covers = new int[]{R.drawable.marvel1,R.drawable.dc1,R.drawable.dh1};
    }

    class MyPagesAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            //Return total pages, here one for each data item
            //return pageData.length;
            return 3;
        }
        //Create the given page (indicated by position)
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View page = inflater.inflate(R.layout.pager_screen, null);
            //((TextView)page.findViewById(R.id.textMessage)).setText(pageData[position]);
            //((Button)page.findViewById(R.id.bt)).setText(pageData[position]);
            //((ImageButton)page.findViewById(R.id.bt)).setImageResource(covers[position]);

            //slide =(ImageButton) page.findViewById(R.id.bt);
            //slide.setImageResource(covers[position]);

            switch(position){
                case 0:
                    Fragment as = new add_screen();
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.swipe_page,as).commit();
                    break;
                case 1:
                    class_list cl = new class_list();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.swipe_page, cl);
                    ft.addToBackStack(null);
                    ft.commit();
                case 2:
                    break;
            }

            //Add the page to the front of the queue
            ((ViewPager) container).addView(page, 0);
            return page;
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //See if object from instantiateItem is related to the given view
            //required by API
            return arg0==(View)arg1;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
            object=null;
        }
    }
}
