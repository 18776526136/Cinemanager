package net.lzzy.cinemanager.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.cinemanager.utils.AppUtils;
import net.lzzy.simpledatepicker.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by lzzy_gxy on 2019/3/27.
 * Description:
 */

public class AddOrdersFragment extends BaseFragment  {
    private OnFragmentInteractionListener listener;
    private OnOrderCreatedListener orderCreate;
    private ArrayAdapter<Cinema> adapter;
    private TextView time;
    private Spinner place;
    private EditText edName;
    private EditText ticket;
    private ImageView code;
    private OrderFactory orderFacotry;
    private List<Cinema> cinemas;
//    private List<Cinema> cinema;
    private OrderFactory facotry=OrderFactory.getInstance();
    private String namel;
    private String itmel1;
    private String placel;
    private String ticketl;
    private CustomDatePicker datePicker;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_add_orders;
    }

    @Override
    public void search(String kw) {

    }

    @Override
    protected void populate() {

        time =  find(R.id.dialog_order_tv_time);
        place =  find(R.id.dialog_order_spinner);
        edName = find(R.id.dialog_order_et_name);
        ticket = find(R.id.dialog_order_et_ticket);
        code = find(R.id.dialog_order_iv_code);

        List<Order> orders= facotry.get();
        cinemas = CinemaFactory.getInstance().get();
        place.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, cinemas));

        listener.hideSearch();
        //获取时间

        time.setOnClickListener(v -> {
            initDate();
            datePicker.show(time.getText().toString());
        });
        //保存
        find(R.id.dialog_order_but_preservation).setOnClickListener(v -> {
//            String name=edName.getText().toString();
//            String moviePrice=ticket.getText().toString();
//            if (TextUtils.isEmpty(name)||TextUtils.isEmpty(moviePrice)){
//                Toast.makeText(getActivity(),"信息需要完整",Toast.LENGTH_SHORT).show();
//                return;
//            }
//            float price;
//            try{
//                price=Float.parseFloat(moviePrice);
//            }catch (NumberFormatException e){
//                Toast.makeText(getActivity(),"数字格式错误",Toast.LENGTH_SHORT).show();
//                return;
//            }
//            Order order=new Order();
//            Cinema cinema=cinemas.get(place.getSelectedItemPosition());
//            order.setCinemaId(cinema.getId());
//            order.setMovie(name);
//            order.setMovieTime(time.getText().toString());
//            order.setPrice(price);
//            edName.setText("");
//            ticket.setText("");
//            orderCreate.saveCinema(order);


            namel = edName.getText().toString();
            itmel1 = time.getText().toString();
            placel = place.getSelectedItem().toString();
            ticketl = ticket.getText().toString();
            if (namel.isEmpty()|| itmel1.isEmpty()|| placel.isEmpty()|| ticketl.isEmpty()){
                Toast.makeText(getContext(),"信息输入不完整",Toast.LENGTH_SHORT).show();
            }else {
                Cinema cinema=null;
                for (Cinema c:cinemas){
                    if (c.toString().equals(placel)){
                        cinema=c;
                    }
                }
                if (cinema!=null){
                    Order order=  new Order(namel, itmel1,Float.valueOf(ticketl),cinema.getId());
                     orderFacotry = OrderFactory.getInstance();
                     orderFacotry.addOrder(order);
                     orderCreate.saveOrder(order);
                }
            }

        });
        find(R.id.dialog_order_but_cancel).setOnClickListener(v -> {
            orderCreate.cancelAddOrder();
        });
        find(R.id.dialog_order_but_code).setOnClickListener(v -> {
            namel = edName.getText().toString();
            ticketl = ticket.getText().toString();

            if (TextUtils.isEmpty(namel)||TextUtils.isEmpty(ticketl)){
                Toast.makeText(getContext(),"信息输入不完整",Toast.LENGTH_LONG).show();
                return;
            }
            String content="["+namel+"]"+ticketl+"\n"+placel;
            code.setImageBitmap(AppUtils.createQRCodeBitmap(content,150,150));


        });
        code.setOnLongClickListener(v ->  {
            Bitmap bitmap=((BitmapDrawable)code.getDrawable()).getBitmap();
            Toast.makeText(getContext(),AppUtils.readQRCode(bitmap),Toast.LENGTH_SHORT).show();
            return true;

        });

        //获取影院地址
//        List<Order> orders=facotry.get();
//        cinemas = CinemaFactory.getInstance().get();
//        adapter = new ArrayAdapter<Cinema>(getActivity(),android.R.layout.simple_spinner_item,cinemas){
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                if (convertView==null){
//                    convertView= LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_spinner_item,null);
//                    TextView textView=convertView.findViewById(android.R.id.text1);
////                    textView.setTextSize(20);
////                    textView.setPadding(10,5,10,5);
//                    textView.setText(getItem(position).getLocation());
//                }
//                return super.getView(position, convertView, parent);
//            }
//        };
//        place.setAdapter(adapter);

    }
    //获取时间
    private void initDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        time.setText(now);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,1);
        datePicker = new CustomDatePicker(getContext(), s -> time.setText(s), now, "2025-01-01 00:00");
        datePicker.setIsLoop(true);
        datePicker.showSpecificTime(true);
        String date=time.getText().toString();
        datePicker.show(date);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isHidden()){
            listener.hideSearch();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener= (OnFragmentInteractionListener) context;
            orderCreate=(OnOrderCreatedListener)context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必需实现OnFragmentInteractionListener&OnCinemaCreatedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }
    public interface OnOrderCreatedListener{
        /** 取消*/
        void cancelAddOrder();
        /**保存*/
        void  saveOrder(Order order);
    }
}
