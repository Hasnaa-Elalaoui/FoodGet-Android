package foodget.ihm.foodget.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import foodget.ihm.foodget.R;
import foodget.ihm.foodget.adapters.PageAdapter;
import foodget.ihm.foodget.models.User;


public class ManagementActivity extends AppCompatActivity {
    private PageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        final ViewPager viewPager = findViewById(R.id.container);
        final TabLayout tabLayout = findViewById(R.id.menu);

        tabLayout.addTab(tabLayout.newTab().setText("Accueil").setIcon(R.drawable.home));
        tabLayout.addTab(tabLayout.newTab().setText("Alertes").setIcon(R.drawable.notifications_bell));
        tabLayout.addTab(tabLayout.newTab().setText("Mon compte").setIcon(R.drawable.account_icon));

        //create a PageAdapter for 3 tabs max
        User loggedUser = getIntent().getExtras().getParcelable("user");
        adapter = new PageAdapter( getSupportFragmentManager(), tabLayout.getTabCount(), loggedUser);

        //adapt the viewPager with the adapter
        viewPager.setAdapter(adapter);

        //listener on tab selected (set the current num tab in the viewPager)
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(
                tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem( tab.getPosition() );
//                System.out.println(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

}
