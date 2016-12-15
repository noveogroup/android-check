package example;

import android.content.Context;

import example.library.R;

public final class AndroidLibrary {

    private AndroidLibrary() {
        throw new UnsupportedOperationException();
    }

    public static String process(Context context) {
        return context.getString(R.string.sample_value);
    }

}
