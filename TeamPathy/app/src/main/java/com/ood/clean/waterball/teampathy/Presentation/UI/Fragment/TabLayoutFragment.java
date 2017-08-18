package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.ProjectSection;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.TabLayoutView;
import com.ood.clean.waterball.teampathy.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabLayoutFragment extends BaseFragment implements TabLayoutView{
    @Inject Member member;
    @Inject Project project;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tablayout) TabLayout tabLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_tablayout_page,container,false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.task_analysis_menu_on_toolbar,menu);  //Task analysis button on the toolbar
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.save_and_update_wbs:  // go to task analytics workspace
                MyApp.createWbsComponent(getActivity());
                getBaseView().getPageController().changePage(new TaskAnalyticsFragment());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        MyApp.getProjectComponent(getActivity()).inject(this);
        setupPages();
    }

    @Override
    public void onLoadWbsFinish() {
        getBaseView().hideProgressDialog();
    }

    private void setupPages() {
        viewPager.setAdapter(new MyFragmentPageAdapter(getChildFragmentManager()));
        // keep all fragments within viewpager alive
        viewPager.setOffscreenPageLimit(ProjectSection.values().length);
        tabLayout.setupWithViewPager(viewPager);
    }


    private class MyFragmentPageAdapter extends FragmentPagerAdapter {

        private final String[] projectSections;

        public MyFragmentPageAdapter(FragmentManager fm) {
            super(fm);
            projectSections = getResources().getStringArray(R.array.project_sections);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            super.getPageTitle(position);
            return projectSections[position];
        }

        @Override
        public Fragment getItem(int position) {
            if (position == ProjectSection.TIMELINE.ordinal())
                return new TimelinesFragment();
            else if (position == ProjectSection.FORUM.ordinal())
                return new IssuesFragment();
            else if (position == ProjectSection.TODOLIST.ordinal())
               return new TodolistFragment();
            else
                return new OfficeFragment();
        }

        @Override
        public int getCount() {
            return projectSections.length;
        }

    }

}
