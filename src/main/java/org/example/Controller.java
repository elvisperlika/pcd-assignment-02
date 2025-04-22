package org.example;

import org.example.view.View;

public interface Controller {
    void attachView(View view);

    void start();
}
