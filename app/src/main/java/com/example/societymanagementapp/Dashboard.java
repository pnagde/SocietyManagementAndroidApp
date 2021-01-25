package com.example.societymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.os.Bundle;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import java.util.ArrayList;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //variable
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    GridLayout gridLayout;
    int logo[]={
                R.drawable.ic_memberform,
                R.drawable.ic_societyform,
                R.drawable.memberlist_ic,
                R.drawable.form_ic_society,
                R.drawable.ic_member_form,//post
                R.drawable.maintenance,//mm
                R.drawable.doc,//abt
            R.drawable.expenses
    };

    String list[]={
                "Society Form",
                "Member Form",
                "Member List",
                "Society List",
                "Post Form",
                "Maintenance Form",
                "Upload Documents",
            "Monthly Expenses"
    };
    ProgressBar progressBar;
    GridView optionslist;
    ArrayList options=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        optionslist = (GridView) findViewById(R.id.simple_grid_view);
        //hooks
        hooks();
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setTitle("Dashboard");
        //toolbar
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //navigation
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        optionslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){

                    case 0:
                        Intent society=new Intent(getApplicationContext(),SocietyForm.class);
                        startActivity(society);
                        break;
                    case 1:
                        Intent memberform =new Intent(getApplicationContext(),MemberForm.class);
                        startActivity(memberform);
                        break;
                    case 2:
                        Intent memberlist =new Intent(getApplicationContext(),MemberList.class);
                        startActivity(memberlist);
                        break;
                    case 3:
                        Intent societylist =new Intent(getApplicationContext(),SocietyList.class);
                        startActivity(societylist);
                        break;
                    case 4:
                        Intent post =new Intent(getApplicationContext(),post.class);
                        startActivity(post);
                        break;
                    case 5:
                        Intent maintanance =new Intent(getApplicationContext(),Maintenance.class);
                        startActivity(maintanance);
                        break;
                    case 6:
                        Intent doc  =new Intent(getApplicationContext(),SocietyDoc.class);
                        startActivity(doc);
                        break;
                    case 7:
                        Intent  monthly=new Intent(getApplicationContext(),MonthlyExpForm.class);
                        startActivity(monthly);
                        break;
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
            builder.setTitle("Exit");
            builder.setIcon(R.drawable.logo);
            builder.setMessage("Do you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.setOnShowListener(new DialogInterface.OnShowListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.primaryDark);
                    alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.primaryDark);
                }
            });
            alert.show();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_profile:
                Intent intent = new Intent(this, Profile.class);
                startActivity(intent);
                break;
            case R.id.nav_memberform:
                Intent intent1 = new Intent(this, MemberForm.class);
                startActivity(intent1);
                break;
            case R.id.nav_societyform:
                Intent intent2 = new Intent(this, SocietyForm.class);
                startActivity(intent2);
                break;
            case R.id.nav_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder.setTitle("Exit");
                builder.setIcon(R.drawable.logo);
                builder.setMessage("Do you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.primaryDark);
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.primaryDark);
                    }
                });
                alert.show();
                break;
            case R.id.nav_aboutus:
                Intent intent3 = new Intent(this, AboutUs.class);
                startActivity(intent3);
                break;
            case R.id.nav_memberlist:
                Intent intent4 = new Intent(this, MemberList.class);
                startActivity(intent4);
                break;

            case R.id.society_list:
                Intent intent5 = new Intent(this, SocietyList.class);
                startActivity(intent5);
                break;
            case R.id.nav_post:
                Intent intent6 = new Intent(this, post.class);
                startActivity(intent6);
                break;
            case R.id.nav_exp_menu:
                Intent intent7 = new Intent(this, MonthlyExpForm.class);
                startActivity(intent7);
                break;
            case R.id.nav_documents:
                Intent intent8 = new Intent(this, SocietyDoc.class);
                startActivity(intent8);
                break;
            case R.id.maintenance:
                Intent intent9 = new Intent(this, Maintenance.class);
                startActivity(intent9);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void hooks()
    {


        GridViewAdapter myAdapter=new GridViewAdapter(getApplicationContext(),logo,list);
        optionslist.setAdapter(myAdapter);
    }
}