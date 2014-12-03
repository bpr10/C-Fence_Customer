package c_fence_customer.anipr.com.myapplication;

import java.lang.reflect.Field;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.ParseUser;

public class CardDetails extends ActionBarActivity {
    private ViewPager viewPager;
    PagerSlidingTabStrip mPagerSlidingTabStrip;
    private FragmentPageAdapter mFragmentPageAdapter;
    String cardNo,company;
    @Override
	protected void onStart() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		super.onStart();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_details);
        if(ParseUser.getCurrentUser()!=null){
            TextView user = (TextView)findViewById(R.id.user_name);
            user.setText(ParseUser.getCurrentUser().get("Name")+" +91-"+ ParseUser.getCurrentUser().get("mobile"));
            TextView holder_name = (TextView)findViewById(R.id.holder_name);
            holder_name.setText(ParseUser.getCurrentUser().get("Name")+"");
            viewPager = (ViewPager) findViewById(R.id.pager);
            mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
            mPagerSlidingTabStrip.setBackgroundColor(getResources().getColor(R.color.card_bg_green));
            mPagerSlidingTabStrip.setIndicatorColor(getResources().getColor(R.color.green));
            mFragmentPageAdapter = new FragmentPageAdapter(getSupportFragmentManager());
            viewPager.setAdapter(mFragmentPageAdapter);
            mPagerSlidingTabStrip.setViewPager(viewPager);
            cardNo = getIntent().getStringExtra("cardno");
            company = getIntent().getStringExtra("company");
            String exp = getIntent().getStringExtra("exp");
            ImageView cardLogo = (ImageView)findViewById(R.id.card_logo);
            if(company.equals("visa"))
            {
                cardLogo.setImageResource(R.drawable.visa);
            }else{
                cardLogo.setImageResource(R.drawable.mastercard);
            }
            TextView cardNumber = (TextView) findViewById(R.id.card_no);
            cardNumber.setText("************"
                    + cardNo.substring(12,16));
            TextView expiry = (TextView) findViewById(R.id.expiry);
            expiry.setText(exp);

        }else{
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_logout) {
			Intent i = new Intent(this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();

		}
		return super.onOptionsItemSelected(item);
	}
    class FragmentPageAdapter extends FragmentPagerAdapter  {

        public FragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            switch (arg0) {
                case 0:
                    return new Fragment1();
                case 1:
                    return new Fragment2();
                default:
                    break;
            }
            return null;

        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "History";
                case 1:
                    return "Rules";
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }


    }




}
