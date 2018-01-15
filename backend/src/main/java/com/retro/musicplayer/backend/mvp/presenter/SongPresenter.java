package com.retro.musicplayer.backend.mvp.presenter;

import android.support.annotation.NonNull;

import com.retro.musicplayer.backend.model.Song;
import com.retro.musicplayer.backend.mvp.contract.SongContract;
import com.retro.musicplayer.backend.providers.interfaces.Repository;

import java.util.ArrayList;

import com.retro.musicplayer.backend.mvp.Presenter;

/**
 * Created by hemanths on 10/08/17.
 */

public class SongPresenter extends Presenter implements SongContract.Presenter {

    @NonNull
    private SongContract.SongView view;


    public SongPresenter(@NonNull Repository repository,
                         @NonNull SongContract.SongView view) {
        super(repository);
        this.view = view;
    }

    @Override
    public void loadSongs() {
        disposable.add(repository.getAllSongs()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable1 -> view.loading())
                .subscribe(this::showList,
                        throwable -> view.showEmptyView(),
                        () -> view.completed()));
    }

    @Override
    public void subscribe() {
        loadSongs();
    }

    private void showList(@NonNull ArrayList<Song> songs) {
        if (songs.isEmpty()) {
            view.showEmptyView();
        } else {
            view.showData(songs);
        }
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }
}
