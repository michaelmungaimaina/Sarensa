package mich.gwan.sarensa.info;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;

import mich.gwan.sarensa.R;

public class InformationApi {
    public void snackBar(NestedScrollView scrollView, String message, int color){
        Snackbar snackbar = Snackbar.make(scrollView, message, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        view.setBackgroundColor(color);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.CENTER;
        params.setMargins(0, 0, 0, 0);
        view.setLayoutParams(params);
        snackbar.setTextColor(Color.WHITE);
        (snackbar.getView()).getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        (snackbar.getView()).getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        snackbar.show();
    }
    public void snackBar(CoordinatorLayout layout, String message, int color){
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        view.setBackgroundColor(color);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)  view.getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.CENTER;
        params.setMargins(0, 0, 0, 0);
        view.setLayoutParams(params);
        snackbar.setTextColor(Color.WHITE);
        (snackbar.getView()).getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        (snackbar.getView()).getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        snackbar.show();
    }
}
