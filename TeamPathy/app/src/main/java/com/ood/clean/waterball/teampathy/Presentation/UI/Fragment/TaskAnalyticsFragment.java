package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.R;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAnalyticsFragment extends BaseFragment {
    @BindView(R.id.tablayout) TabLayout tabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;
    MyFragmentPageAdapter adapter;
    @Inject Member member;
    @Inject Project project;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_analytics,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        MyApp.getProjectComponent(getActivity()).inject(this);
        initiateViewPager();
    }

    private void initiateViewPager(){
        adapter = new MyFragmentPageAdapter(getChildFragmentManager());
        viewPager.setOffscreenPageLimit(adapter.getCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class MyFragmentPageAdapter extends FragmentPagerAdapter {

        private final List<String> taskAnalysisSections;

        public MyFragmentPageAdapter(FragmentManager fm) {
            super(fm);
            taskAnalysisSections = Arrays.asList(getResources().getStringArray(R.array.task_analysis_sections));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            super.getPageTitle(position);
            return taskAnalysisSections.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return new WbsConsoleFragment();
                case 1:
                    return ChartWebViewFragment.newInstance(
                            ChartWebViewFragment.XslType.WBS);
                case 2:
                    return ChartWebViewFragment.newInstance(
                            ChartWebViewFragment.XslType.GanttChart);
                default:
                    return new TaskPendingFragment();
            }
        }
        @Override
        public int getCount() {
            if (!member.isMemberPosition())  // manager and leader has one more workspace to review the tasks
                return taskAnalysisSections.size() ;
            return taskAnalysisSections.size() - 1;
        }
    }

}
