package example;

import android.content.Context;

public final class JavaLibrary {

    private JavaLibrary() {
        throw new UnsupportedOperationException();
    }

    public static String process(Context context) {
        return context.getPackageName();
    }

}
