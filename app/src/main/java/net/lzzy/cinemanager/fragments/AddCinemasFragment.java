package net.lzzy.cinemanager.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Adapter;
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

import java.util.zip.Inflater;

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
    //3.声明接口对象
    private OnFragmentInteractionListener listener;
    //(3)声明接口对象
    private OnCinemaCreatedListener cinemaCreate;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_add_cinemas;


    }

    @Override
    public void search(String kw) {

    }

    @Override
    protected void populate() {
        listener.hideSearch();
        getActivity().findViewById(R.id.bar_title_search).setVisibility(View.GONE);
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
                cinemaCreate.cancelAddCinema();
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
//          cinemaFacotry.addCinema(cinema);
            cinemaCreate.saveCinema(cinema);
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

    //6.调用方法
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            listener.hideSearch();
        }
    }

    //4(4).初始化
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener= (OnFragmentInteractionListener) context;
            cinemaCreate= (OnCinemaCreatedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    +"必需实现OnFragmentInteractionListener&OnCinemaCreatedListener ");
        }
    }
    //5(5).销毁
    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
        cinemaCreate=null;
    }

    public interface OnCinemaCreatedListener{
        /** 取消*/
        void cancelAddCinema();
        /**保存*/
        void  saveCinema(Cinema cinema);
    }

}
