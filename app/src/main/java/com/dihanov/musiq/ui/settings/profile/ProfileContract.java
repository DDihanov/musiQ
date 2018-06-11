package com.dihanov.musiq.ui.settings.profile;

import android.support.design.widget.AppBarLayout;

import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

public interface ProfileContract {
    interface View extends BaseView<Presenter>, MainViewFunctionable {
        void initViewPager();

        void setUserImage(String url);

        AppBarLayout getAppBarLayout();
    }

    interface Presenter extends BasePresenter<View> {
    }
}
