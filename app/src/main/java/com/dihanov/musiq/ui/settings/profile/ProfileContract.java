package com.dihanov.musiq.ui.settings.profile;

import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

public interface ProfileContract {
    interface View extends BaseView<Presenter>, MainViewFunctionable {
    }

    interface Presenter extends BasePresenter<View> {
    }
}
