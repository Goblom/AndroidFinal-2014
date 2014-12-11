package bryan.lars.bbapp14.impl;

/**
 * Created by Goblom on 12/11/2014.
 */
public interface HttpSuccessListener {

    void onHttpSuccess(Type type, String result);

    public static enum Type {
        PUT, GET, POST;
    }
}
