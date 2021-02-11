package com.selectuser.tools;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

public class EventObserver<T> implements Observer<Event<T>> {
    private final OnEventChanged onEventChanged;


    public EventObserver(OnEventChanged onEventChanged) {
        this.onEventChanged = onEventChanged;
    }



    @Override
    public void onChanged(@Nullable Event<T> tEvent) {
        if (tEvent != null && !tEvent.isHandled() && onEventChanged != null)
            onEventChanged.onUnhandledContent(tEvent.getContentIfNotHandled());
    }



    public interface OnEventChanged<T> {
        void onUnhandledContent(T data);
    }
}
