package com.cyl.musiclake.ui.music.local.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Pair;
import android.view.View;

import com.cyl.musiclake.R;
import com.cyl.musiclake.base.BaseLazyFragment;
import com.cyl.musiclake.bean.Album;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.common.NavigationHelper;
import com.cyl.musiclake.ui.music.local.adapter.AlbumAdapter;
import com.cyl.musiclake.ui.music.local.contract.AlbumsContract;
import com.cyl.musiclake.ui.music.local.presenter.AlbumPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：本地歌曲列表
 * 作者：yonglong on 2016/8/10 20:49
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
public class AlbumFragment extends BaseLazyFragment<AlbumPresenter> implements AlbumsContract.View {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private AlbumAdapter mAdapter;
    private List<Album> albumList = new ArrayList<>();

    public static AlbumFragment newInstance() {

        Bundle args = new Bundle();
        AlbumFragment fragment = new AlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化视图
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.fragment_recyclerview_notoolbar;
    }

    /**
     * 初始化控件
     */
    @Override
    public void initViews() {
        mAdapter = new AlbumAdapter(albumList);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
    }

    @Override
    protected void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    protected void listener() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(() -> {
                if (mPresenter != null) {
                    mPresenter.loadAlbums("all");
                }
            });
        }
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Album album = (Album) adapter.getItem(position);
            NavigationHelper.INSTANCE.navigateToAlbum(getActivity(),
                    album.getId(),
                    album.getName(),
                    new Pair<View, String>(view.findViewById(R.id.album), Constants.TRANSTITION_ALBUM));
        });
    }


    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onLazyLoad() {
        mPresenter.loadAlbums("all");
    }

    @Override
    public void showLoading() {
        super.showLoading();
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showAlbums(List<Album> albumList) {
        mAdapter.setNewData(albumList);
    }

    @Override
    public void showEmptyView() {
        mAdapter.setEmptyView(R.layout.view_song_empty);
    }
}
