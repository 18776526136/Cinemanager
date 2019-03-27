package net.lzzy.cinemanager.fragments;


import android.view.View;
import android.widget.ListView;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/3/26.
 * Description:
 */
public class CinemasFragment extends BaseFragment {
    private static List<Cinema> cinemas=new ArrayList<>();
    private CinemaFactory factory=CinemaFactory.getInstance();


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_cinemas;
    }

    @Override
    protected void populate() {
        ListView lv=find(R.id.activity_cinemas_lv);
        View empty=find(R.id.activity_cinemas_tv_none);
        lv.setEmptyView(empty);
        cinemas=factory.get();
        GenericAdapter<Cinema> adapter = new GenericAdapter<Cinema>(getActivity(),
                R.layout.cinema_item,cinemas) {
            @Override
            public void populate(ViewHolder viewHolder, Cinema cinema) {
                viewHolder.setTextView(R.id.main_item_tv_name,cinema.getName())
                        .setTextView(R.id.main_item_tv_place,cinema.getLocation());

            }

            @Override
            public boolean persistInsert(Cinema cinema) {
                return factory.addCinema(cinema);
            }

            @Override
            public boolean persistDelete(Cinema cinema) {
                return factory.deleteCinema(cinema);
            }
        };
        lv.setAdapter(adapter);


    }
}
