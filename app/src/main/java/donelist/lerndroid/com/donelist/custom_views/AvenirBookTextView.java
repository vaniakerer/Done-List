package donelist.lerndroid.com.donelist.custom_views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ivan on 20.12.16.
 */

public class AvenirBookTextView extends TextView {
    public AvenirBookTextView(Context context) {
        super(context);
        changeTypeface();
    }

    public AvenirBookTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        changeTypeface();
    }

    public AvenirBookTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        changeTypeface();
    }

    public AvenirBookTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        changeTypeface();
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/avenir_book.otf");
        super.setTypeface(tf, style);
    }

    @Override
    public void setTypeface(Typeface tf) {
        tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/avenir_book.otf");
        super.setTypeface(tf);
    }

    private void changeTypeface() {
        Typeface avenirBook = Typeface.createFromAsset(getContext().getAssets(), "fonts/avenir_book.otf");
        this.setTypeface(avenirBook);
    }
}
