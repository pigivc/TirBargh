package com.irprogram.tirbargh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class navigation_drawer extends Fragment
{
    private ActionBarDrawerToggle drawer_toggle;
    private DrawerLayout my_drawer_layout;

    public navigation_drawer()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    public void setUp( DrawerLayout dl )
    {
        my_drawer_layout = dl;

        drawer_toggle = new ActionBarDrawerToggle(
                getActivity(), dl, R.string.drawer_open, R.string.drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if (item != null && item.getItemId() == android.R.id.home)
                {
                    if( my_drawer_layout.isDrawerOpen(Gravity.RIGHT))
                    {
                        my_drawer_layout.closeDrawer(Gravity.RIGHT);
                    }
                    else
                    {
                        my_drawer_layout.openDrawer(Gravity.RIGHT);
                    }

                    return true;
                }

                return false;
            }
        };

        my_drawer_layout.setDrawerListener(drawer_toggle);

        my_drawer_layout.post(
            new Runnable() {
                @Override
                public void run() {
                    drawer_toggle.syncState();
                }
            }
        );
    }
}
