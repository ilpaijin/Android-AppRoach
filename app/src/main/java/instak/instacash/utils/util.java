package instak.instacash.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class util {

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
