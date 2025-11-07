package fun.bm.chamomile.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
    public static Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }
}
