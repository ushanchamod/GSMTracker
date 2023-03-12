package com.ousl.gsm.ui.slighting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SlightingViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SlightingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slighting fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}