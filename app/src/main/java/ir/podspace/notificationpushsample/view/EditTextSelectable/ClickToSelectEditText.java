package ir.podspace.notificationpushsample.view.EditTextSelectable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import ir.podspace.notificationpushsample.R;


public class ClickToSelectEditText<T extends Listable> extends TextInputEditText {

    List<T> mItems;
    String[] mListableItems;
    CharSequence mHint;
    Context context;

    OnItemSelectedListener<T> onItemSelectedListener;

    Object selectedItem = null;

    ListPopupWindow window;

    private Drawable drawableRight;
    private Drawable drawableLeft;
    private Drawable drawableTop;
    private Drawable drawableBottom;

    int actionX, actionY;

    private DrawableClickListener clickListener;


    public ClickToSelectEditText(Context context) {
        super(context);
        this.context = context;
        window = new ListPopupWindow(context);

        window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        window.setWidth(WindowManager.LayoutParams.MATCH_PARENT);

        window.setModal(false);
        window.setAnchorView(this);

        mHint = getHint();

        initialize(context, null);
    }

    public ClickToSelectEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        window = new ListPopupWindow(context);

        window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        window.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

        window.setModal(false);
        window.setAnchorView(this);

        mHint = getHint();

        initialize(context, attrs);
    }

    public ClickToSelectEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        window = new ListPopupWindow(context);

        window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        window.setWidth(WindowManager.LayoutParams.MATCH_PARENT);

        window.setModal(false);
        window.setAnchorView(this);

        mHint = getHint();

        initialize(context, attrs);
    }

    public void initialize(Context context, AttributeSet attrs) {
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setFocusable(false);
        setClickable(true);
    }

    public void setItems(List<T> items) {
        this.mItems = items;
        this.mListableItems = new String[items.size()];

        int i = 0;

        for (T item : mItems) {
            mListableItems[i++] = item.getLabel();
        }

        configureOnClickListener();
    }

    public List<T> getmItems() {
        return this.mItems;
    }

    public void clearItem() {
        if (mItems != null) {
            mItems.clear();
            mListableItems = null;
        }
    }

    public void clearSelectedItem(){
        if (mListableItems != null){
            mListableItems = null;
        }
    }

    private void configureOnClickListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                window.setAdapter(new WindowAdapter(mListableItems));
                window.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        setText(mListableItems[position]);

                        selectedItem = mItems.get(position);

                        if (onItemSelectedListener != null) {
                            onItemSelectedListener.onItemSelectedListener(mItems.get(position), position);
                        }

                        window.dismiss();
                    }
                });

                window.show();
            }
        });
    }

    public void setSelectedItem(int position) {
        setText(mListableItems[position]);
        this.selectedItem = mItems.get(position);
        if (onItemSelectedListener != null)
            onItemSelectedListener.onItemSelectedListener(mItems.get(position), position);
    }

    public void setSelectedItem(int position, boolean callItemSelect) {
        setText(mListableItems[position]);
        this.selectedItem = mItems.get(position);
        if (onItemSelectedListener != null && callItemSelect)
            onItemSelectedListener.onItemSelectedListener(mItems.get(position), position);
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener<T> onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public class WindowAdapter extends BaseAdapter {
        private String[] list;

        public WindowAdapter(String[] list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public String getItem(int i) {
            return list[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drop_down_layout, null);
            }

            TextView lblTitle = view.findViewById(R.id.lblTitle);
            lblTitle.setText(getItem(i));
            lblTitle.setTextColor(Color.BLACK);

            return view;
        }
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom) {
        if (left != null) {
            drawableLeft = left;
        }
        if (right != null) {
            drawableRight = right;
        }
        if (top != null) {
            drawableTop = top;
        }
        if (bottom != null) {
            drawableBottom = bottom;
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Rect bounds;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            actionX = (int) event.getX();
            actionY = (int) event.getY();
            if (drawableBottom != null
                    && drawableBottom.getBounds().contains(actionX, actionY)) {
                clickListener.onClick(DrawableClickListener.DrawablePosition.BOTTOM);
                return super.onTouchEvent(event);
            }

            if (drawableTop != null
                    && drawableTop.getBounds().contains(actionX, actionY)) {
                clickListener.onClick(DrawableClickListener.DrawablePosition.TOP);
                return super.onTouchEvent(event);
            }

            // this works for left since container shares 0,0 origin with bounds
            if (drawableLeft != null) {
                bounds = null;
                bounds = drawableLeft.getBounds();

                int x, y;
                int extraTapArea = (int) (13 * getResources().getDisplayMetrics().density + 0.5);

                x = actionX;
                y = actionY;

                if (!bounds.contains(actionX, actionY)) {
                    /** Gives the +20 area for tapping. */
                    x = (int) (actionX - extraTapArea);
                    y = (int) (actionY - extraTapArea);

                    if (x <= 0)
                        x = actionX;
                    if (y <= 0)
                        y = actionY;

                    /** Creates square from the smallest value */
                    if (x < y) {
                        y = x;
                    }
                }

                if (bounds.contains(x, y) && clickListener != null) {
                    clickListener
                            .onClick(DrawableClickListener.DrawablePosition.LEFT);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return false;

                }
            }

            if (drawableRight != null) {

                bounds = null;
                bounds = drawableRight.getBounds();

                int x, y;
                int extraTapArea = 13;

                /**
                 * IF USER CLICKS JUST OUT SIDE THE RECTANGLE OF THE DRAWABLE
                 * THAN ADD X AND SUBTRACT THE Y WITH SOME VALUE SO THAT AFTER
                 * CALCULATING X AND Y CO-ORDINATE LIES INTO THE DRAWBABLE
                 * BOUND. - this process help to increase the tappable area of
                 * the rectangle.
                 */
                x = (int) (actionX + extraTapArea);
                y = (int) (actionY - extraTapArea);

                /**Since this is right drawable subtract the value of x from the width
                 * of view. so that width - tappedarea will result in x co-ordinate in drawable bound.
                 */
                x = getWidth() - x;

                /*x can be negative if user taps at x co-ordinate just near the width.
                 * e.g views width = 300 and user taps 290. Then as per previous calculation
                 * 290 + 13 = 303. So subtract X from getWidth() will result in negative value.
                 * So to avoid this add the value previous added when x goes negative.
                 */

                if (x <= 0) {
                    x += extraTapArea;
                }

                /* If result after calculating for extra tappable area is negative.
                 * assign the original value so that after subtracting
                 * extratapping area value doesn't go into negative value.
                 */

                if (y <= 0)
                    y = actionY;

                /**If drawble bounds contains the x and y points then move ahead.*/
                if (bounds.contains(x, y) && clickListener != null) {
                    clickListener
                            .onClick(DrawableClickListener.DrawablePosition.RIGHT);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return false;
                }
                return super.onTouchEvent(event);
            }

        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        drawableRight = null;
        drawableBottom = null;
        drawableLeft = null;
        drawableTop = null;
        super.finalize();
    }

    public void setDrawableClickListener(DrawableClickListener listener) {
        this.clickListener = listener;
    }

    public interface OnItemSelectedListener<T> {
        void onItemSelectedListener(T item, int selectedIndex);
    }
}