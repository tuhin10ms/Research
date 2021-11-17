package com.example.research;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean isKeyboardOpened = false;

    public interface OnKeyboardVisibilityListener {
        void onVisibilityChanged(boolean visible);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setKeyboardListener(visible -> Toast.makeText(getApplicationContext(), "Keyboard is opened: " + isKeyboardOpened, Toast.LENGTH_SHORT).show());
    }

    public final void setKeyboardListener(final OnKeyboardVisibilityListener listener) {
        final View activityRootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);

        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private final int DefaultKeyboardDP = 100;

            private final int EstimatedKeyboardDP = DefaultKeyboardDP + 48;

            private final Rect r = new Rect();

            @Override
            public void onGlobalLayout() {
                // Convert the dp to pixels.
                int estimatedKeyboardHeight = (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, activityRootView.getResources().getDisplayMetrics());

                // Conclude whether the keyboard is shown or not.
                activityRootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == isKeyboardOpened) {
                    Log.d("Keyboard state", "Ignoring global layout change...");
                    return;
                }

                isKeyboardOpened = isShown;
                listener.onVisibilityChanged(isShown);
            }
        });
    }
}