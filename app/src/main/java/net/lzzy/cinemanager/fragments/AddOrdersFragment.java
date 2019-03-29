package net.lzzy.cinemanager.fragments;

import android.content.Context;

import net.lzzy.cinemanager.R;

/**
 * Created by lzzy_gxy on 2019/3/27.
 * Description:
 */

public class AddOrdersFragment extends BaseFragment {
    private OnFragmentInteractionListener listener;
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_add_orders;
    }

    @Override
    protected void populate() {
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
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必需实现OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }
}
