package net.center.upload_plugin;

import com.android.tools.r8.u.b.S;

/**
 * Created by Android-ZX
 * 2021/9/3.
 */
public class PluginUtils {

    public static boolean isEmpty(CharSequence s) {
        if (s == null) {
            return true;
        } else {
            return s.length() == 0;
        }
    }


    public static String getPgyIdentifier() {
//        return "xcxwo";
        return "pgyer";
    }
}
