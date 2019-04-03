package net.lzzy.cinemanager.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;

import net.lzzy.cinemanager.R;
import net.lzzy.cinemanager.models.Cinema;
import net.lzzy.cinemanager.models.CinemaFactory;
import net.lzzy.cinemanager.models.Order;
import net.lzzy.cinemanager.models.OrderFactory;
import net.lzzy.cinemanager.utils.AppUtils;
import net.lzzy.sqllib.GenericAdapter;
import net.lzzy.sqllib.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzzy_gxy on 2019/3/26.
 * Description:
 */
public class OrdersFragment extends BaseFragment {
    private static final float TOUCH_MIN_DISTANCE=100;
    public static final String ORDER = "order";
    private static List<Order> orders=new ArrayList<>();
    private OrderFactory factory=OrderFactory.getInstance();
    private GenericAdapter<Order> adapter;
    private float touchX1;
    private boolean isDeleting=false;
    public Order order;

    public static OrdersFragment newInstance(Order order){
        OrdersFragment fragment=new OrdersFragment();
        Bundle args=new Bundle();
        args.putParcelable(ORDER,order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            order= getArguments().getParcelable(ORDER);
        }
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_orders;
    }

    @Override
    public void search(String kw) {
        orders.clear();
        if (TextUtils.isEmpty(kw)){
            orders.addAll(factory.get());
        }else {
            orders.addAll(factory.searchOrders(kw));
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void populate() {
        ListView lv=find(R.id.activity_orders_lv);
        View empty=find(R.id.activity_order_tv_none);
        lv.setEmptyView(empty);
        orders.clear();
        orders=factory.get();


        adapter = new GenericAdapter<Order>(getActivity(),
                R.layout.order_item,orders) {

            @Override
            public void populate(ViewHolder viewHolder, Order order) {
                    String location= String.valueOf(CinemaFactory.getInstance()
                            .getById(order.getCinemaId().toString()));
                    viewHolder.setTextView(R.id.order_item_tv_movie,order.getMovie())
                            .setTextView(R.id.order_item_tv_places,location);
                    Button btn=viewHolder.getView(R.id.order_item_btn);
                    btn.setOnClickListener(v -> new AlertDialog.Builder(getContext())
                            .setTitle("删除确认")
                            .setMessage("确定要删除项目吗？")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确认", (dialogInterface, i) ->
                            {   isDeleting=false;
                                adapter.remove(order);
                                btn.setVisibility(View.GONE);
                            }).show());

                    int visibility=isDeleting?View.VISIBLE:View.GONE;
                    btn.setVisibility(visibility);
                    viewHolder.getConvertView().setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            slideToDelete(event, order,btn);
                            return true;
                        }
                    });
            }

            @Override
            public boolean persistInsert(Order order) {
                return factory.addOrder(order);
            }

            @Override
            public boolean persistDelete(Order order) {
                return factory.delete(order);
            }
        };
        lv.setAdapter(adapter);
        if (order!=null){
            saveOrder(order);
        }

    }
    public void saveOrder(Order order){
        adapter.add(order);
    }

    private void slideToDelete(MotionEvent event, Order order, Button btn) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchX1=event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if(touchX1-event.getX()>TOUCH_MIN_DISTANCE){
                    if (!isDeleting){
                        btn.setVisibility(View.VISIBLE);
                        isDeleting=true;
                    }
                }else {
                    if(btn.isShown()){
                        btn.setVisibility(View.GONE);
                        isDeleting=false;
                    }else {
                        clickOrder(order);
                    }
                }
                break;
                default:
                    break;
        }
    }
    private void clickOrder(Order order) {
        Cinema cinema= CinemaFactory.getInstance().getById(order.getCinemaId().toString());
        String content="["+order.getMovie()+"]"+order.getMovieTime()+"\n"+cinema.toString();
        View view= LayoutInflater.from(getContext()).inflate(R.layout.dialog_qrcode,null);
        ImageView img=view.findViewById(R.id.dialog_qrcode);
        img.setImageBitmap(AppUtils.createQRCodeBitmap(content,300,300));
        new AlertDialog.Builder(getContext()).setView(view).show();
    }

}
