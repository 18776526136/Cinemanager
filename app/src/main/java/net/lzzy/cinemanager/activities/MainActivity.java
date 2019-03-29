package net.lzzy.cinemanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.fragments.AddCinemasFragment;
import net.lzzy.cinemanager.fragments.AddOrdersFragment;
import net.lzzy.cinemanager.fragments.CinemasFragment;
import net.lzzy.cinemanager.fragments.OnFragmentInteractionListener;
import net.lzzy.cinemanager.fragments.OrdersFragment;
import net.lzzy.cinemanager.models.Cinema;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener
             , OnFragmentInteractionListener,AddCinemasFragment.OnCinemaCreatedListener {
    private LinearLayout layoutMenu;
    private TextView tvTitle;
    private SearchView search;
    private FragmentManager manager=getSupportFragmentManager();
    private SparseArray<String> titleArray=new SparseArray<>();
    private SparseArray<Fragment> fragmentArray=new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setTitleMenu();
    }

    public void setTitleMenu(){
        titleArray.put(R.id.bar_title_tv_add_cinema,"添加影院");
        titleArray.put(R.id.bar_title_tv_add_order,"添加订单");
        titleArray.put(R.id.bar_title_tv_view_cinema,"查看影院");
        titleArray.put(R.id.bar_title_tv_view_order,"我的订单");
        layoutMenu=findViewById(R.id.bar_title_layout_menu);
        layoutMenu.setVisibility(View.GONE);
        findViewById(R.id.bar_title_img_menu).setOnClickListener(v ->{
            int visible=layoutMenu.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE;
            layoutMenu.setVisibility(visible);
        });

        tvTitle=findViewById(R.id.bar_title_tv_title);
        tvTitle.setText(R.string.bar_title_menu_order);
        search=findViewById(R.id.bar_title_search);
        findViewById(R.id.bar_title_tv_exit).setOnClickListener(v -> System.exit(0));
        findViewById(R.id.bar_title_tv_add_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_add_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_order).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        search.setVisibility(View.VISIBLE);
        layoutMenu.setVisibility(View.GONE);
        tvTitle.setText(titleArray.get(v.getId()));
        Fragment fragment=fragmentArray.get(v.getId());
        FragmentTransaction transaction=manager.beginTransaction();
        if (fragment==null){
            fragment=createFragment(v.getId());
            fragmentArray.put(v.getId(),fragment);
            transaction.add(R.id.relativ_fragments,fragment);
        }

        //把所有的fragment隐藏
        for (Fragment f:manager.getFragments()){
            transaction.hide(f);
        }
        transaction.show(fragment).commit();


    }

    private Fragment createFragment(int id) {
        switch (id) {
            case R.id.bar_title_tv_add_cinema:
                return new AddCinemasFragment();
            case R.id.bar_title_tv_add_order:
                return new AddOrdersFragment();
            case R.id.bar_title_tv_view_cinema:
//                manager.beginTransaction()
//                        .replace(R.id.relativ_fragments,new CinemasFragment())
//                        .commit();
                return new CinemasFragment();
            case R.id.bar_title_tv_view_order:
//                manager.beginTransaction()
//                        .replace(R.id.relativ_fragments,new OrdersFragment())
//                        .commit();
                return new OrdersFragment();
            default:
                break;
        }
        return null;
    }

//2.实现接口
    @Override
    public void hideSearch() {
        search.setVisibility(View.GONE);
    }

    @Override
    public void cancelAddCinema() {
        Fragment addCinemFragment=fragmentArray.get(R.id.bar_title_tv_add_cinema);
        if (addCinemFragment==null){
            return;
        }
        Fragment cinemasFragment=fragmentArray.get(R.id.bar_title_tv_view_cinema);
        FragmentTransaction transaction=manager.beginTransaction();
        if (cinemasFragment==null){
            cinemasFragment=new CinemasFragment();
            fragmentArray.put(R.id.bar_title_tv_view_cinema,cinemasFragment);
            transaction.add(R.id.relativ_fragments,cinemasFragment);
        }
        transaction.hide(addCinemFragment).show(cinemasFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_title_tv_view_cinema));

    }

    @Override
    public void saveCinema(Cinema cinema) {
        Fragment addCinemFragment=fragmentArray.get(R.id.bar_title_tv_add_cinema);
        if (addCinemFragment==null){
            return;
        }
        Fragment cinemasFragment=fragmentArray.get(R.id.bar_title_tv_view_cinema);
        FragmentTransaction transaction=manager.beginTransaction();
        if (cinemasFragment==null){
            //创建CinemaFragment的同时传Cinema
            cinemasFragment=new CinemasFragment(cinema);
            fragmentArray.put(R.id.bar_title_tv_view_cinema,cinemasFragment);
            transaction.add(R.id.relativ_fragments,cinemasFragment);
        }else {
            ((CinemasFragment)cinemasFragment).save(cinema);
        }
        transaction.hide(addCinemFragment).show(cinemasFragment).commit();
        tvTitle.setText(titleArray.get(R.id.bar_title_tv_view_cinema));
    }
}
