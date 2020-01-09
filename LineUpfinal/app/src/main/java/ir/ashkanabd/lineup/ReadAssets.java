package ir.ashkanabd.lineup;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;

import java.io.*;
import java.util.*;

/**
 * use {@link ReadAssets} for getting images from {@link #assetManager} or /android/data/ {@link #getPackageName()} /download
 */
public class ReadAssets {

    private File downloadLocation;
    private Context context;
    private AssetManager assetManager;
    private List<Bitmap> pitchesList, shirtList, downloadList;

    /**
     * @param #context {@link ReadAssets#context} for {@link #getPackageName()}
     */
    ReadAssets(Context context) {
        this.context = context;
        downloadLocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/data/" + getPackageName() + "/download");
        pitchesList = new ArrayList<>();
        shirtList = new ArrayList<>();
        downloadList = new ArrayList<>();
        assetManager = context.getAssets();
        reloadAssets();
    }

    public void reloadAssets() {
        readAssetsPitches();
        readAssetsShirts();
        readDownloads();
    }

    private String getPackageName() {
        return context.getPackageName();
    }

    List<Bitmap> getDownloadBitmap() {
        return downloadList;
    }

    List<Bitmap> getAssetsShirts() {
        return shirtList;
    }

    List<Bitmap> getAssetsPitches() {
        return pitchesList;
    }

    /**
     * read pitches from {@link #assetManager} and fill {@link #pitchesList} or empty list if {@link Exception} occurred
     */
    private void readAssetsPitches() {
        pitchesList.clear();
        try {
            for (String s : assetManager.list("pitches")) {
                pitchesList.add(BitmapFactory.decodeStream(assetManager.open("pitches/" + s)));
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * read shirts from {@link #assetManager} and fill {@link #shirtList} or empty list if {@link Exception} occurred
     */
    private void readAssetsShirts() {
        shirtList.clear();
        try {
            for (String s : assetManager.list("shirts")) {
                shirtList.add(BitmapFactory.decodeStream(assetManager.open("shirts/" + s)));
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * read downloads and fill {@link #downloadList}
     */
    private void readDownloads() {
        downloadList.clear();
        if (downloadLocation.list() != null)
            for (String s : downloadLocation.list()) {
                downloadList.add(BitmapFactory.decodeFile(downloadLocation.getAbsolutePath() + "/" + s));
            }
    }

    @Override
    public int hashCode() {
        return context.hashCode() * 7;
    }
}
