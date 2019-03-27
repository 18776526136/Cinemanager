package net.lzzy.cinemanager.fragments;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityPicker;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;

/**
 * Created by lzzy_gxy on 2019/3/27.
 * Description:
 */
public class AddCinemasFragment extends BaseFragment implements View.OnClickListener {
    private TextView region;
    private EditText name;
    private String provinces="广西壮族自治区";
    private String citys="柳州市";
    private String areas="鱼峰区";
    private CinemaFactory cinemaFacotry;
    private JDCityPicker jdCityPicker;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_add_cinemas;


    }

    @Override
    protected void populate() {
        region = find(R.id.dialog_tv_region);
        find(R.id.dialog_but_preservation).setOnClickListener(this);
        find(R.id.dialog_but_cancel).setOnClickListener( this);
        name = find(R.id.dialog_et_name);
        region.setOnClickListener(this);
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_tv_region:
                jdCityPicker.showCityPicker();
                break;
            case R.id.dialog_but_preservation:
                seveCinema();
                break;
            case R.id.dialog_but_cancel:
                break;
        }
    }
    private void seveCinema() {
        String address=region.getText().toString();
        String isNmae=name.getText().toString();
        if (address.isEmpty()||isNmae.isEmpty()){
            Toast.makeText(getActivity(),"影院名称不能为空",Toast.LENGTH_SHORT).show();
        }else {
            Cinema cinema=  new Cinema(isNmae,address,provinces,citys,areas);
            cinemaFacotry = CinemaFactory.getInstance();
            cinemaFacotry.addCinema(cinema);
            Toast.makeText(getActivity(),"添加成功",Toast.LENGTH_SHORT).show();

        }

    }

    /**   仿京东地址选择*/
    private void initData() {
        jdCityPicker = new JDCityPicker();
        jdCityPicker.init(getActivity());
        jdCityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                provinces=province.toString();
                citys=city.toString();
                areas=district.getName();
                region.setText(provinces+citys+areas);
            }
            @Override
            public void onCancel() {
                super.onCancel();
            }
        });

    }
}
