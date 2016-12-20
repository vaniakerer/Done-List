package donelist.lerndroid.com.donelist.custom_views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by ivan on 20.12.16.
 */

public class AvenirBookButton extends Button {
    public AvenirBookButton(Context context) {
        super(context);
        changeTypeface();
    }

    public AvenirBookButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        changeTypeface();
    }

    public AvenirBookButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        changeTypeface();
    }

    public AvenirBookButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        changeTypeface();
    }

    private void changeTypeface() {
        Typeface avenirBook = Typeface.createFromAsset(getContext().getAssets(), "fonts/avenir_book.otf");
        this.setTypeface(avenirBook);
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
}
