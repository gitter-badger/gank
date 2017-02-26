package com.junnanhao.gank.ganks;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.junnanhao.gank.R;
import com.junnanhao.gank.data.models.Gank;
import com.junnanhao.gank.ui.ScrollChildSwipeRefreshLayout;
import com.junnanhao.gank.ui.VerticalGridCardSpacingDecoration;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Jonas on 2017/2/22.
 * Display a list of {@link Gank}s in a specific day.
 */

public class GanksView extends ScrollChildSwipeRefreshLayout implements GanksContract.View {
    @BindView(R.id.ganks) RecyclerView gankListView;
    @BindView(R.id.noGank) LinearLayout noGank;

    private GanksAdapter mAdapter;
    private GanksContract.Presenter mPresenter;

    public GanksView(Context context) {
        super(context);
        init();
    }

    public GanksView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.ganks_view_content, this);
        ButterKnife.bind(this);

        mAdapter = new GanksAdapter();
        gankListView.setAdapter(mAdapter);
        gankListView.setLayoutManager(new LinearLayoutManager(getContext()));
        gankListView.setItemAnimator(new DefaultItemAnimator());
        gankListView.addItemDecoration(new VerticalGridCardSpacingDecoration());

        setScrollUpChild(gankListView);
        setOnRefreshListener(() -> mPresenter.loadGanks(false));
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        final SwipeRefreshLayout srl = this;
        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(() -> srl.setRefreshing(active));
    }

    @Override
    public void showGanks(Map<String, List<Gank>> ganks) {
        if (ganks.size() == 0) {
            showNoGank();
            return;
        }

        mAdapter.setData(ganks);

        gankListView.setVisibility(VISIBLE);
        noGank.setVisibility(GONE);
    }


    @Override
    public void showSubmitGank() {
        // start submit Gank activity
    }

    @Override
    public void showSuccessfullySubmitMessage() {
        showMessage(R.string.submit_success);
    }


    @Override
    public void showLoadingGanksError() {
        showMessage(R.string.load_failed);
        Timber.e("加载干货失败");
    }

    @Override
    public void showNoGank() {
        noGank.setVisibility(VISIBLE);
        gankListView.setVisibility(GONE);
        Timber.d("No gank");
    }


    @Override
    public void showCalendarMenu() {

    }

    @Override
    public void setPresenter(GanksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void showMessage(@StringRes int resId) {
        Snackbar.make(this, resId, Snackbar.LENGTH_LONG).show();
    }

}
