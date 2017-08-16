package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ood.clean.waterball.teampathy.Domain.Model.Timeline;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.CrudPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.TimelinesPresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.UI.Adapter.BindingViewHolder;
import com.ood.clean.waterball.teampathy.Presentation.UI.Animation.TargetHeightAnimation;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.FragmentTimelinePageBinding;
import com.ood.clean.waterball.teampathy.databinding.TimelineItemBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelinesFragment extends BaseFragment implements CrudPresenter.CrudView<Timeline>, SwipeRefreshLayout.OnRefreshListener {
    int page = 0;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swiperefreshlayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.input_timeline_ed) EditText timelineInputEd;
    TimelinesAdapter adapter;
    @Inject User user;
    @Inject TimelinesPresenterImp presenterImp;
    FragmentTimelinePageBinding binding;
    List<Timeline> timelineList = new ArrayList<>();
    BroadcastReceiver receiver = new TimelineBroadcastReceiver();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_timeline_page, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getBaseView().showProgressBar();
        binding(view);
        setListeners();
        setupRecyclerview();
        presenterImp.setTimelinesView(this);
        presenterImp.loadEntities(page++);
    }

    private void binding(View view){
        ButterKnife.bind(this,view);
        MyApp.getProjectComponent(getActivity()).inject(this);
        binding.setUser(user);
    }

    public void setListeners(){
        setTimelineEdittextTypingListener();
    }

    private void setTimelineEdittextTypingListener() {
        timelineInputEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // create a timeline when finish the text and press enter
                if ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN ))
                    createTimeline();
                return false;
            }
        });
    }

    private void createTimeline() {
        getBaseView().showProgressBar();
        Timeline timeline = new Timeline(user,timelineInputEd.getText().toString());
        presenterImp.create(timeline);
    }

    private void setupRecyclerview() {
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new TimelinesAdapter();
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public void loadEntity(Timeline timeline) {
        timelineList.add(timeline);
        adapter.notifyDataSetChanged();
    }

    @Override  //todo  single line field would reduce the UX, so change it to multiple lines should have a better design
    public void onCreateFinishNotify(final Timeline timeline) {
        clearInputEdittextStatus();
        //todo timeline should be at the 0 position of adapter
        getBaseView().hideProgressBar();
        Snackbar.make(binding.getRoot(),getString(R.string.timeline_created_completed),Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenterImp.delete(timeline);
                    }
                }).show();
    }

    private void clearInputEdittextStatus() {
        /* hide down the keyboard */
        timelineInputEd.setText("");
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(timelineInputEd.getWindowToken(), 0);
    }

    @Override
    public void onDeleteFinishNotify(final Timeline timeline) {
        getBaseView().hideProgressBar();
        Snackbar.make(binding.getRoot(),getString(R.string.timeline_removed_completed),Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenterImp.create(timeline);
                    }
                }).show();
    }

    @Override
    public void onUpdateFinishNotify(Timeline timeline) {
        getBaseView().hideProgressBar();
    }

    @Override
    public void onPageIndexOutOfBound() {
        page --;
        onLoadFinishNotify();
    }

    @Override
    public void onLoadFinishNotify() {
        swipeRefreshLayout.setRefreshing(false);
        getBaseView().hideProgressBar();
    }

    @Override
    public void onRefresh() {
        getBaseView().showProgressBar();
        presenterImp.loadEntities(page++);
    }

    @Override
    public void onOperationTimeout(Throwable err) {
        getBaseView().hideProgressDialog();
        Toast.makeText(getContext(),err.getMessage(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("postTimeline");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiver),
                intentFilter
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterImp.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterImp.onDestroy();
    }

    public class TimelinesAdapter extends RecyclerView.Adapter<BindingViewHolder>{

        @Override
        public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            TimelineItemBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.timeline_item,parent,false);
            return new BindingViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(BindingViewHolder holder, int position) {
            Object obj = timelineList.get(position);
            holder.bind(obj);
            ((TimelineItemBinding)holder.getBinding()).setHandler(new EventHandler());
        }

        @Override  //prevent from the animation influence multiple views
        public long getItemId(int position) {
            return timelineList.get(position).getId();
        }

        @Override
        public int getItemViewType(int position) {
            return timelineList.get(position).getId();
        }

        @Override
        public int getItemCount() {
            return timelineList.size();
        }

        public class EventHandler{

            public void cardViewOnClick(View view){
                TextView contentTxt = (TextView) view.findViewById(R.id.contentTxt);
                scaleCardViewToFitWholeContent(contentTxt);
            }

            private void scaleCardViewToFitWholeContent(final TextView contentTxt) {
                final int startHeight = contentTxt.getHeight();
                contentTxt.setMaxLines(Integer.MAX_VALUE); //show whole content

                int widthSpec = View.MeasureSpec.makeMeasureSpec(contentTxt.getWidth(), View.MeasureSpec.EXACTLY);
                int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                contentTxt.measure(widthSpec, heightSpec);

                int targetHeight = contentTxt.getMeasuredHeight();

                Animation animation = new TargetHeightAnimation(contentTxt, startHeight, targetHeight, true);
                animation.setDuration(600);
                contentTxt.startAnimation(animation);
            }
        }
    }

    class TimelineBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String eventType = intent.getAction();
            String category = intent.getStringExtra("category");
            String content = intent.getStringExtra("content");
            String posterImageUrl = intent.getStringExtra("posterImageUrl");
            String posterName = intent.getStringExtra("posterName");
            User poster = new User();
            poster.setImageUrl(posterImageUrl);
            poster.setName(posterName);
            Timeline timeline = new Timeline();
            timeline.setCategory(Timeline.Category.valueOf(category));
            timeline.setContent(content);
            //todo create a converter class between Firebase model to the Domain Model
            // todo should get the user id to detect if the poster is the self !
            if (eventType.contains("post") && !poster.getName().equals(user.getName()))
                loadEntity(timeline);
        }
    }
}
