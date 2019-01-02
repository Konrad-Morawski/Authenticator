package com.intive.kmorawski.authenticator;

public interface BackgroundOperationObserver {
    void onStarted();

    void onFinished();
}
