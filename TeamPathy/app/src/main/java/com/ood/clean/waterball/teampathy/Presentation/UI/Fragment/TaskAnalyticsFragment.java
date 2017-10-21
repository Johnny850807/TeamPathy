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
import com.ood.clean.waterball.teampathy.Presentation.Presenter.WbsConsolePresenterImp;
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
    @Inject WbsConsolePresenterImp wbsConsolePresenterImp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_analytics,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        MyApp.getWbsComponent(getActivity()).inject(this);
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
            /*
              The Wbs console, wbs chart and the gantt chart fragments should be added as a WbsUpdatedListener
              into the presenter in order to ensure all the fragments would be updated
              at the same time whenever the wbs updated.
             */
            switch (position)
            {
                case 0:
                    WbsConsoleFragment consolefragment = new WbsConsoleFragment();
                    wbsConsolePresenterImp.putWbsUpdatedListener("console", consolefragment);
                    return consolefragment;
                case 1:
                    ChartWebViewFragment wbsfragment = ChartWebViewFragment.newInstance(
                            ChartWebViewFragment.XslType.WBS);
                    wbsConsolePresenterImp.putWbsUpdatedListener("wbs", wbsfragment);
                    return wbsfragment;
                case 2:
                    ChartWebViewFragment ganttfragment = ChartWebViewFragment.newInstance(
                            ChartWebViewFragment.XslType.GanttChart);
                    wbsConsolePresenterImp.putWbsUpdatedListener("gantt", ganttfragment);
                    return ganttfragment;
                default:
                    return new TaskPendingFragment();
            }
        }
/*
        private Fragment createFragmentAndPutAsWbsUpdatedListener(){

        }*/

        @Override
        public int getCount() {
            if (!member.isNotManager())  // manager and leader has one more workspace to review the tasks
                return taskAnalysisSections.size() ;
            return taskAnalysisSections.size() - 1;
        }
    }

}
