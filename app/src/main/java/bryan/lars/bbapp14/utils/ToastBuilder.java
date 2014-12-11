package bryan.lars.bbapp14.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Goblom on 12/3/2014.
 */
public class ToastBuilder {

    public static ToastBuilder builder() {
        return new ToastBuilder();
    }

    private ToastBuilder() { }

    private CharSequence text;
    private Gravity gravity;
    private Margin margin;
    private Duration duration = Duration.LONG;

    public ToastBuilder setGravity(int gravity, int xOffset, int yOffset) {
        this.gravity = new Gravity(gravity, xOffset, yOffset);
        return this;
    }

    public ToastBuilder setText(CharSequence sequence) {
        this.text = sequence;
        return this;
    }

    public ToastBuilder setDuration(Duration dur) {
        this.duration = duration;
        return this;
    }

    public ToastBuilder setMargin(float horizontal, float vertical) {
        this.margin = new Margin(horizontal, vertical);
        return this;
    }

    public Toast getToast(Context context) {
        Toast toast = Toast.makeText(context, text, duration.length);

        if (gravity != null) {
            toast.setGravity(gravity.gravity, gravity.xOffset, gravity.yOffset);
        }

        if (margin != null) {
            toast.setMargin(margin.horizontal, margin.vertical);
        }

        return toast;
    }

    public void show(Context context) {
        getToast(context).show();
    }

    class Gravity {

        protected final int gravity, xOffset, yOffset;

        private Gravity(int gravity, int xOffset, int yOffset) {
            this.gravity = gravity;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }
    }

    class Margin {
        final float horizontal, vertical;

        private Margin(float horizontal, float vertical) {
            this.horizontal = horizontal;
            this.vertical = vertical;
        }
    }

    public static enum Duration {
        LONG(Toast.LENGTH_LONG),
        SHORT(Toast.LENGTH_SHORT);

        private final int length;

        Duration(int i) {
            this.length = i;
        }
    }
}
