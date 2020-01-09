package ir.ashkanabd.lineup;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;

import java.util.*;

class Formula {

    static String F343 = "3 - 4 - 3";
    static String F352 = "3 - 5 - 2";
    static String F361 = "3 - 6 - 1";
    static String F424 = "4 - 2 - 4";
    static String F433 = "4 - 3 - 3";
    static String F442 = "4 - 4 - 2";
    static String F451 = "4 - 5 - 1";
    static String F532 = "5 - 3 - 2";
    static String F541 = "5 - 4 - 1";
    static String F4123 = "4 - 1 - 2 - 3";
    static String F4141 = "4 - 1 - 4 - 1";
    static String F4213 = "4 - 2 - 1 - 3";
    static String F4231 = "4 - 2 - 3 - 1";
    static String F4321 = "4 - 3 - 2 - 1";
    static String FCustom = "Custom format";

    private int[] ids = {R.id.p0, R.id.p1, R.id.p2, R.id.p3, R.id.p4, R.id.p5, R.id.p6, R.id.p7, R.id.p8, R.id.p9, R.id.p10};
    private ImageView[] playersImageView;
    private TextView[] playersTextView;
    private Context context;
    private RelativeLayout.LayoutParams params;

    Formula(Context context) {
        this.context = context;
        playersImageView = new ImageView[11];
        playersTextView = new TextView[11];
        for (int i = 0; i < 11; i++) {
            playersImageView[i] = new ImageView(context);
            playersTextView[i] = new TextView(context);
        }
    }

    public void firstSetup(Bitmap[] playersShirt, String[] playersName) {
        for (int i = 0; i < 11; i++) {
            playersImageView[i].setImageBitmap(playersShirt[i]);
            playersTextView[i].setText(playersName[i]);
            playersImageView[i].setTag(i + "img");
            playersTextView[i].setTag(i + "text");
            playersImageView[i].setId(ids[i]);
            playersTextView[i].setTextColor(Color.WHITE);
            playersTextView[i].setGravity(Gravity.CENTER);
            playersTextView[i].setTextSize(17);
            playersTextView[i].setMaxLines(2);
        }
        matchTextAndImage();
    }

    public void setPlayersImageAndText(Bitmap[] playersShirt, String[] playersName) {
        for (int i = 0; i < 11; i++) {
            playersImageView[i].setImageBitmap(playersShirt[i]);
            playersTextView[i].setText(playersName[i]);
        }
        matchTextAndImage();
    }

    public int toDp(int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px * scale + 0.5f);
    }

    public void addToView(RelativeLayout mainLayout) {
        for (int i = 0; i < 11; i++) {
            params = new RelativeLayout.LayoutParams(toDp(50), toDp(50));
            playersImageView[i].setLayoutParams(params);
            mainLayout.addView(playersImageView[i]);
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            playersTextView[i].setLayoutParams(params);
            mainLayout.addView(playersTextView[i]);
        }
        matchTextAndImage();
    }

    public void setOnClick(View.OnClickListener onClickListener) {
        for (int i = 0; i < 11; i++) {
            playersTextView[i].setOnClickListener(onClickListener);
            playersImageView[i].setOnClickListener(onClickListener);
        }
    }

    public void setOnLongClick(View.OnLongClickListener onLongClickListener) {
        for (int i = 0; i < 11; i++) {
            playersTextView[i].setOnLongClickListener(onLongClickListener);
            playersImageView[i].setOnLongClickListener(onLongClickListener);
        }
    }

    public void matchTextAndImage() {
        for (int i = 0; i < 11; i++) {
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, ids[i]);
            params.addRule(RelativeLayout.ALIGN_RIGHT, ids[i]);
            params.addRule(RelativeLayout.ALIGN_LEFT, ids[i]);
            params.setMargins(toDp(-30), toDp(0), toDp(-30), toDp(0));
            playersTextView[i].setLayoutParams(params);
        }
    }

    public ImageView[] getPlayersImageView() {
        return playersImageView;
    }

    public TextView[] getPlayersTextView() {
        return playersTextView;
    }

    public void setPlayersImageView(ImageView[] playersImageView) {
        this.playersImageView = playersImageView;
    }

    public void setPlayersTextView(TextView[] playersTextView) {
        this.playersTextView = playersTextView;
    }

    /**
     * @param formula setup player position with it
     * @return {@link List} of {@link ImageView} and {@link TextView} that contains players position
     */
    /*List[] requestFormula(String formula) {
        if (formula.equals(F352)) {

        } else if (formula.equals(F361)) {

        } else if (formula.equals(F424)) {

        } else if (formula.equals(F433)) {

        } else if (formula.equals(F442)) {

        } else if (formula.equals(F451)) {

        } else if (formula.equals(F532)) {

        } else if (formula.equals(F541)) {

        } else if (formula.equals(F4123)) {

        } else if (formula.equals(F4141)) {

        } else if (formula.equals(F4213)) {

        } else if (formula.equals(F4231)) {

        } else if (formula.equals(F4321)) {

        } else {
            //Custom and F343

        }
        return new List[]{playersImageView, playersTextView};
    }*/
}
