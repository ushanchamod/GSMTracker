package com.ousl.gsm.ui.immense;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ImmenseViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ImmenseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is immense fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}