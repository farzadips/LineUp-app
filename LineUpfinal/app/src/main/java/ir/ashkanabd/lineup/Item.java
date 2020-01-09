package ir.ashkanabd.lineup;

import android.graphics.*;


abstract class Item {
    private int position, hashcode;

    public Item(int position, int hashcode) {
        this.hashcode = hashcode;
        this.position = position;
    }

    public int getHashCode() {
        return hashcode;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public int hashCode() {
        return hashcode;
    }
}

/**
 * {@link #ItemBitmap(Bitmap, int, int)} class for Items in {@link GridListViewAdapter}
 *
 * @see Item
 */
class ItemBitmap extends Item {

    private Bitmap bitmap;

    public ItemBitmap(Bitmap bitmap, int position, int hashcode) {
        super(position, hashcode);
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public int hashCode() {
        return getHashCode();
    }
}