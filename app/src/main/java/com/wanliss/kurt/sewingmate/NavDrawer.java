package com.wanliss.kurt.sewingmate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class NavDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContext;
    private Activity mActivity;

    public NavDrawer(Context currentContext) {
        this.mContext = currentContext;
        this.mActivity = (Activity) currentContext;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Snackbar.make(this.mActivity.findViewById(R.id.drawer_layout), item.getTitle() + " " + Integer.toString(id),
                Snackbar.LENGTH_SHORT)
                .show();

        if (id == R.id.contact) {
            Intent intent = new Intent(this.mActivity, ContactActivity.class);
            this.mActivity.startActivity(intent);
            return true;

        } else if (id == R.id.nav_sign_in) {
            Intent intent = new Intent(this.mActivity, Login.class);
            this.mActivity.startActivity(intent);
            return true;
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this.mActivity, MainActivity.class);
            this.mActivity.startActivity(intent);
            return true;
        }  else if (id == R.id.nav_storage) {
            Intent intent = new Intent(this.mActivity, StorageActivity.class);
            this.mActivity.startActivity(intent);
            return true;

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) this.mActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
