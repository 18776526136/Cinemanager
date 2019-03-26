package net.lzzy.cinemanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import net.lzzy.cinemanager.R;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout layoutMenu;
    private TextView tvTitle;
    private SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setTitleMenu();
    }

    public void setTitleMenu(){
        layoutMenu=findViewById(R.id.bar_title_layout_menu);
        layoutMenu.setVisibility(View.GONE);
        findViewById(R.id.bar_title_img_menu).setOnClickListener(this);
        tvTitle=findViewById(R.id.bar_title_tv_title);
        tvTitle.setText(R.string.bar_title_menu_order);
        search=findViewById(R.id.bat_title_search);
        findViewById(R.id.bar_title_tv_exit).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_add_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_add_order).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_cinema).setOnClickListener(this);
        findViewById(R.id.bar_title_tv_view_order).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bar_title_img_menu:
                int visible=layoutMenu.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE;
                layoutMenu.setVisibility(visible);
                break;
            case R.id.bar_title_tv_exit:
                layoutMenu.setVisibility(View.GONE);
                break;
            case R.id.bar_title_tv_add_cinema:
                tvTitle.setText(R.string.bar_title_menu_add_cinema);
                layoutMenu.setVisibility(View.GONE);
                break;
            case R.id.bar_title_tv_add_order:
                tvTitle.setText(R.string.bar_title_menu_add_order);
                layoutMenu.setVisibility(View.GONE);
                break;
            case R.id.bar_title_tv_view_cinema:
                tvTitle.setText(R.string.bar_title_menu_view_cinema);
                layoutMenu.setVisibility(View.GONE);
                break;
            case R.id.bar_title_tv_view_order:
                tvTitle.setText(R.string.bar_title_menu_order);
                layoutMenu.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }
}
