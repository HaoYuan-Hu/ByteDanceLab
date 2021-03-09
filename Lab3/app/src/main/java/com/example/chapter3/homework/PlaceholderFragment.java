package com.example.chapter3.homework;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

public class PlaceholderFragment extends Fragment {

    private View view;
    private LottieAnimationView loading;
    private LinearLayout list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
        view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        loading = view.findViewById(R.id.loading);
        list = view.findViewById(R.id.list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                ObjectAnimator loadingAnimator = ObjectAnimator.ofFloat(loading, "alpha", 1f, 0f);
                loadingAnimator.setDuration(300);

                ObjectAnimator listAnimator = ObjectAnimator.ofFloat(list,"alpha",0f,1f);
                listAnimator.setDuration(300);

                AnimatorSet animatorSet;
                animatorSet = new AnimatorSet();
                animatorSet.playTogether(loadingAnimator, listAnimator);
                animatorSet.start();
            }
        }, 5000);
    }
}
