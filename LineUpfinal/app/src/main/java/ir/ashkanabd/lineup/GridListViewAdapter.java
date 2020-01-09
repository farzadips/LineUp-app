package ir.ashkanabd.lineup;

import android.content.*;
import android.graphics.*;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.birin.gridlistviewadapters.*;
import com.birin.gridlistviewadapters.dataholders.*;
import com.birin.gridlistviewadapters.utils.*;

public class GridListViewAdapter extends ListGridAdapter<ItemBitmap, CardView> {

    private OnClick onClick;
    private Class callerClass;

    public GridListViewAdapter(Context context, int totalCardsInRow, OnClick onClick, Class callerClass) {
        super(context, totalCardsInRow);
        this.onClick = onClick;
        this.callerClass = callerClass;
    }

    /**
     * @param px size to pixels
     * @return size to dp
     */
    public int toDp(int px) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px * scale + 0.5f);
    }

    /**
     * interface for call when on card clicked
     */
    interface OnClick {
        /**
         * @param cardData call on {@link StartActivity#onCardClicked(ItemBitmap, Class)}
         * @see StartActivity#onCardClicked(ItemBitmap, Class)
         */
        void onCardClicked(ItemBitmap cardData, Class callerClass);
    }

    @Override
    public int getCardWidth(int totalCardsInRow) {
        return toDp(60);
    }

    @Override
    protected Card<CardView> getNewCard(int cardwidth) {
        CardView cardView = new CardView();
        cardView.imageView = new ImageView(getContext());
        return new Card<>(cardView.imageView, cardView);
    }

    @Override
    protected void setCardView(CardDataHolder<ItemBitmap> cardDataHolder, CardView cardView) {
        cardView.imageView.setImageBitmap(cardDataHolder.getData().getBitmap());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(toDp(55), toDp(90));
        params.setMarginStart(toDp(10));
        params.setMarginEnd(toDp(10));
        cardView.imageView.setLayoutParams(params);
    }

    @Override
    protected void onCardClicked(ItemBitmap cardData) {
        onClick.onCardClicked(cardData, callerClass);
    }

    @Override
    protected void registerChildrenViewClickEvents(CardView cardViewHolder, ChildViewsClickHandler childViewsClickHandler) {

    }

    @Override
    protected void onChildViewClicked(View clickedChildView, ItemBitmap cardData, int eventId) {

    }

    @Override
    protected void setRowView(View rowView, int arg1) {
        rowView.setBackgroundColor(Color.parseColor("#f6404040"));
    }
}
