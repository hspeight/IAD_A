package com.itsadate.iad_a;

import android.app.Fragment;
import android.app.FragmentManager;

import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;


import java.util.ArrayList;


/*
 * Demonstrates a "screen-slide" animation using a {@link ViewPager}. Because {@link ViewPager}
 * automatically plays such an animation when calling {@link ViewPager#setCurrentItem(int)}, there
 * isn't any animation-specific code in this sample.
 *
 * <p>This sample shows a "next" button that advances the user to the next step in a wizard,
 * animating the current screen out (to the left) and the next screen in (from the right). The
 * reverse animation is played when the user presses the "previous" button.</p>
 *
 * @see ScreenSlidePageFragment
 */
public class ScreenSlidePageractivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private int NUM_PAGES;
    private int initialId;
    private ArrayList<SwipeItem> swipeitems = new ArrayList<>();
    private MyDBHandler dbHandler;
    //private int foods[] = {1711,1712,1713,1714,1715,1716};

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //System.out.println("!!- " + "creating pager activity");
        setContentView(R.layout.activity_screen_slide);
        dbHandler = new MyDBHandler(this, null, null, 1);
        // There should always be an associated row ID in the extras
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            initialId = bundle.getInt("ROW_ID"); // why not pass the event then you dont need to read db again ???
            //System.out.println("!!- " + "--initial id is " + initialId);
            //displayEventInfo(bundle);
        //} else {
        //    System.out.println("!!- " + "error"); // need to do something better than this
            //textTit.setText("Error reading event row data");
        }
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);

        //ArrayList<Events> events = new ArrayList<Events>();
        swipeitems = loadInitialObjectsFromDB();

        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), swipeitems);

        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(initialId); // start with event selected from main list

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
// onresume is called 3 times each time an event is updated.
        Events myEvent = dbHandler.getMyEvent(swipeitems.get(mPager.getCurrentItem()).getId());
        swipeitems.set(mPager.getCurrentItem(), new SwipeItem(myEvent.get_id(), myEvent.get_eventname(), myEvent.get_eventinfo(),
                myEvent.get_evtime(), myEvent.get_direction(), myEvent.get_incsec(), myEvent.get_dayyears()));
        mPager.getAdapter().notifyDataSetChanged();

    }

    //public Fragment getFragmentByPosition(int pos) {
    //    String tag = "android:switcher:" + mPager.getId() + ":" + pos;
    //    return getFragmentManager().findFragmentByTag(tag);
    //}

    public ArrayList<SwipeItem> loadInitialObjectsFromDB() {
        String evstring = dbHandler.getEventIDs("A"); // Fetch Id's of active events
        String[] foods = evstring.split(":");
        //System.out.println("!!- " + "foods=" + evstring);
        //String[] newfoods = rebuildArray(foods,initialId);
        //System.out.println("!!- " + "new foods=" + Arrays.toString(newfoods));
        NUM_PAGES = foods.length;
        for (int i = 0; i < NUM_PAGES; i++) {
            Events myEvent = dbHandler.getMyEvent(Integer.parseInt(foods[i]));
            swipeitems.add(new SwipeItem(myEvent.get_id(), myEvent.get_eventname(),myEvent.get_eventinfo(),
                    myEvent.get_evtime(), myEvent.get_direction(), myEvent.get_incsec(), myEvent.get_dayyears()));
            //architems.add(new ArchItem(myEvent.get_eventname(),myEvent.get_eventinfo()));
            //architems.add(new ArchItem(myEvent.get_eventname(),formatDateTime(myEvent.get_evtime(), myEvent.get_direction()),false));
            //System.out.println("!!- " + myEvent.get_eventname());
        }
        return swipeitems;
    }
    /*
    public String[] rebuildArray(String[] foods, int sPos) {
        //System.out.println("!!- " + "len=" + foods.length + "func foods=" + Arrays.toString(foods) + "pos=" + sPos);
        String[] foods2;
        foods2 = new String[foods.length];
        System.arraycopy(foods, sPos, foods2, 0, foods.length - sPos);
        System.arraycopy(foods, 0, foods2, foods.length - sPos, sPos);
        //System.out.println("!!- " + "len=" + foods.length + "foods2=" + Arrays.toString(foods2));
        return foods2;
    }
    */

    /**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<SwipeItem> objectList;

        public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<SwipeItem> swipeItems) {
            super(fm);
            this.objectList = swipeItems;
        }

        @Override
        public Fragment getItem(int position) {

            SwipeItem myEvent = objectList.get(position);
            //System.out.println("!!- " + "pos=" + position + " id=" + myEvent.());
            //return ScreenSlidePageFragment.create(myEvent.getId(),myEvent.getTitle());
            return ScreenSlidePageFragment.create(myEvent);
            //return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public int getItemPosition(Object object) {
            // after the itim is edited this method is called 3 times. Can we do anything?
            //System.out.println("!!- " + "getting item position " + object);
            return POSITION_NONE;
        }



    }

}
