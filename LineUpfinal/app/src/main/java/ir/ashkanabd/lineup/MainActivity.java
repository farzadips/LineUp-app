package ir.ashkanabd.lineup;

import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.provider.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.os.*;
import android.support.v7.app.ActionBar;
import android.view.*;
import android.widget.*;

import java.util.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, GridListViewAdapter.OnClick {

    public static String CHANGEALL = "1";
    public static String CHANGEONE = "2";
    public static String CHANGEPITCH = "3";

    private RelativeLayout mainLayout, editPlayerLayout;
    private RelativeLayout.LayoutParams params;
    private Formula formula;
    private Vibrator vibrator;
    private Dialog shirtsDialog, pitchDialog;
    private GridListViewAdapter shirtListViewAdapter, pitchListViewAdapter;
    private ListView shirtListView, pitchListView;
    private Intent downloadIntent, getImageIntent;
    private String teamFormula, teamName, changeImageTask;
    private Bitmap playersShirt[], importIcon;
    private String playersName[];
    private View dragView, editView;
    private List<Bitmap> shirtList, pitchList;
    private List<ItemBitmap> shirtItemBitmap, pitchItemBitmap;
    private TextView[] playersTextView;
    private ImageView[] playersImageView;
    private ReadAssets readAssets;
    private EditText teamNameEdit, managerNameEdit, editPlayerName;
    private ImageView teamLogo, editPlayerImage;
    private ImageView p1I, p2I, p3I, p4I, p5I, p6I, p7I, p8I, p9I, p10I, p11I;
    private TextView p1T, p2T, p3T, p4T, p5T, p6T, p7T, p8T, p9T, p10T, p11T;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        checkPermission();
        widgets();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        formula = new Formula(this);
        formula.firstSetup(playersShirt, playersName);
        playersImageView = formula.getPlayersImageView();
        playersTextView = formula.getPlayersTextView();
        formula.addToView(mainLayout);
        formula.setOnClick(this);
        formula.setOnLongClick(this);
        shirtList = new ArrayList<>();
        pitchList = new ArrayList<>();
        shirtItemBitmap = new ArrayList<>();
        pitchItemBitmap = new ArrayList<>();
        shirtList.addAll(readAssets.getAssetsShirts());
        shirtList.addAll(readAssets.getDownloadBitmap());
        pitchList.addAll(readAssets.getAssetsPitches());
        onDrop();
        buildShirtsDialog();
        buildPitchDialog();
    }

    private void onDrag(View view) {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.setVisibility(View.INVISIBLE);
        mainLayout.findViewWithTag(getIndexFromTag(view.getTag().toString()) + "text").setVisibility(View.INVISIBLE);
        view.startDrag(data, shadowBuilder, null, 0);
        dragView = view;
    }

    private void onDrop() {
        mainLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                int action = dragEvent.getAction();
                switch (action) {
                    case DragEvent.ACTION_DROP:
                        params = new RelativeLayout.LayoutParams(toDp(50), toDp(50));
                        params.topMargin = ((int) dragEvent.getY()) - toDp(25);
                        params.leftMargin = ((int) dragEvent.getX()) - toDp(25);
                        dragView.setLayoutParams(params);
                        mainLayout.findViewWithTag(getIndexFromTag(dragView.getTag().toString()) + "text").setVisibility(View.VISIBLE);
                        dragView.setVisibility(View.VISIBLE);
                        formula.matchTextAndImage();
                        break;
                }
                return true;
            }
        });
    }

    private void buildPitchDialog() {
        pitchDialog = new Dialog(this, R.style.list_style);
        pitchListView = new ListView(this);
        pitchListViewAdapter = new GridListViewAdapter(this, 4, this, getClass());
        pitchListViewAdapter.addItemsInGrid(addPitchToListView(pitchList));
        pitchListView.setAdapter(pitchListViewAdapter);
        pitchDialog.setContentView(pitchListView, dialogLayout(toDp(500), toDp(300)));
    }

    private void buildShirtsDialog() {
        shirtsDialog = new Dialog(this, R.style.list_style);
        shirtListView = new ListView(this);
        shirtListViewAdapter = new GridListViewAdapter(this, 4, this, getClass());
        shirtListViewAdapter.addItemsInGrid(addShirtsToListView(shirtList));
        shirtListView.setAdapter(shirtListViewAdapter);
        shirtsDialog.setContentView(shirtListView, dialogLayout(toDp(500), toDp(300)));
    }

    private ActionBar.LayoutParams dialogLayout(int height, int width) {
        return new ActionBar.LayoutParams(width, height);
    }

    private List<ItemBitmap> addPitchToListView(List<Bitmap> pitchList) {
        pitchItemBitmap.clear();
        pitchItemBitmap.add(new ItemBitmap(importIcon, 0, importIcon.hashCode()));
        for (Bitmap bitmap : pitchList) {
            if (bitmap != null) {
                pitchItemBitmap.add(new ItemBitmap(bitmap, pitchList.indexOf(bitmap) + 1, bitmap.hashCode()));
            }
        }
        return pitchItemBitmap;
    }

    private List<ItemBitmap> addShirtsToListView(List<Bitmap> shirtList) {
        shirtItemBitmap.clear();
        shirtItemBitmap.add(new ItemBitmap(importIcon, 0, importIcon.hashCode()));
        for (Bitmap bitmap : shirtList) {
            if (bitmap != null)
                shirtItemBitmap.add(new ItemBitmap(bitmap, shirtList.indexOf(bitmap) + 1, bitmap.hashCode()));
        }
        return shirtItemBitmap;
    }

    private Bitmap[] buildShirts(Bitmap shirt) {
        Bitmap[] bitmaps = new Bitmap[11];
        for (int i = 0; i < 11; i++) {
            bitmaps[i] = shirt;
        }
        return bitmaps;
    }

    public int toDp(int px) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (px * scale + 0.5f);
    }

    private void widgets() {
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        editPlayerLayout = (RelativeLayout) findViewById(R.id.editPlayerLayout);
        teamNameEdit = (EditText) findViewById(R.id.teamNameEdit);
        managerNameEdit = (EditText) findViewById(R.id.managerNameEdit);
        editPlayerName = (EditText) findViewById(R.id.editPlayerName);
        teamLogo = (ImageView) findViewById(R.id.logo);
        editPlayerImage = (ImageView) findViewById(R.id.editPlayerImage);
        Bundle getData = getIntent().getBundleExtra(StartActivity.SENDDATA);
        playersShirt = buildShirts(Bitmap.createBitmap(StartActivity.SELECTEDSHIRT));
//        playersShirt = buildShirts((Bitmap) getIntent().getExtras().get(StartActivity.SHIRT));
        readAssets = StartActivity.GETASSETS;
        teamFormula = getData.getString(StartActivity.FORMULA);
        teamName = getData.getString(StartActivity.TEAMNAME);
        playersName = getData.getStringArray(StartActivity.NAMES);
        teamNameEdit.setText(teamName);
        getImageIntent = new Intent();
        downloadIntent = new Intent(this, DownloadActivity.class);
        importIcon = BitmapFactory.decodeResource(getResources(), R.drawable.import1);
    }

    public void editPlayerCancel(View view) {
        mainLayout.setEnabled(true);
        teamNameEdit.setEnabled(true);
        managerNameEdit.setEnabled(true);
        teamLogo.setEnabled(true);
        editPlayerLayout.setVisibility(View.INVISIBLE);
    }

    public void changePlayerShirt(View view) {
        shirtsDialog.show();
        changeImageTask = CHANGEONE;
    }

    public void editPlayerSubmit(View view) {
        mainLayout.setEnabled(true);
        teamNameEdit.setEnabled(true);
        managerNameEdit.setEnabled(true);
        teamLogo.setEnabled(true);
        editPlayerLayout.setVisibility(View.INVISIBLE);
        playersShirt[getIndexFromTag(editView.getTag().toString())]
                = ((BitmapDrawable) (editPlayerImage.getDrawable())).getBitmap();
        playersName[getIndexFromTag(editView.getTag().toString())] = editPlayerName.getText().toString();
        formula.setPlayersImageAndText(playersShirt, playersName);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onClick(View view) {
        editView = view;
        mainLayout.setEnabled(false);
        teamNameEdit.setEnabled(false);
        managerNameEdit.setEnabled(false);
        teamLogo.setEnabled(false);
        editPlayerLayout.setVisibility(View.VISIBLE);
        editPlayerImage.setImageBitmap(((BitmapDrawable) (((ImageView) view).getDrawable())).getBitmap());
        editPlayerName.setText(((TextView) mainLayout.findViewWithTag(getIndexFromTag(view.getTag().toString()) + "text")).getText());
    }

    @Override
    public boolean onLongClick(View view) {
        vibrator.vibrate(50);
        onDrag(view);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (editPlayerLayout.getVisibility() == View.VISIBLE) {
            menu.close();
        }
        return true;
    }

    @Override
    public void onCardClicked(ItemBitmap cardData, Class callerClass) {
        if (callerClass == getClass()) {
            if (changeImageTask.equals(CHANGEONE)) {
                if (cardData.getPosition() == 0) {
                    getImageIntent.setType("image/*");
                    getImageIntent.setAction(Intent.ACTION_GET_CONTENT);
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String pickTitle = "Take or select a photo";
                    Intent chooserIntent = Intent.createChooser(getImageIntent, pickTitle);
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});
                    startActivityForResult(chooserIntent, 1);
                } else {
                    editPlayerImage.setImageBitmap(cardData.getBitmap());
                }
                shirtsDialog.dismiss();
            } else if (changeImageTask.equals(CHANGEALL)) {
                if (cardData.getPosition() == 0) {
                    getImageIntent.setType("image/*");
                    getImageIntent.setAction(Intent.ACTION_GET_CONTENT);
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String pickTitle = "Take or select a photo";
                    Intent chooserIntent = Intent.createChooser(getImageIntent, pickTitle);
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});
                    startActivityForResult(chooserIntent, 1);
                } else {
                    playersShirt = changeAllBitmaps(playersShirt, cardData.getBitmap());
                    formula.setPlayersImageAndText(playersShirt, playersName);
                }
                shirtsDialog.dismiss();
            } else {
                if (cardData.getPosition() == 0) {
                    getImageIntent.setType("image/*");
                    getImageIntent.setAction(Intent.ACTION_GET_CONTENT);
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String pickTitle = "Take or select a photo";
                    Intent chooserIntent = Intent.createChooser(getImageIntent, pickTitle);
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});
                    startActivityForResult(chooserIntent, 1);
                } else {
                    mainLayout.setBackground(new BitmapDrawable(getResources(), cardData.getBitmap()));
                }
                pitchDialog.dismiss();
            }
        }
    }

    private Bitmap[] changeAllBitmaps(Bitmap[] playersShirt, Bitmap shirt) {
        for (int i = 0; i < playersShirt.length; i++) {
            playersShirt[i] = shirt;
        }
        return playersShirt;
    }

    private int getIndexFromTag(String tag) {
        String index = "";
        for (int i = 0; i < tag.length(); i++) {
            if (Character.isDigit(tag.charAt(i)))
                index += tag.charAt(i);
        }
        return Integer.parseInt(index);
    }

    public void logo(View view) {
        getImageIntent.setType("image/*");
        getImageIntent.setAction(Intent.ACTION_GET_CONTENT);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String pickTitle = "Take or select a photo";
        Intent chooserIntent = Intent.createChooser(getImageIntent, pickTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePhotoIntent});
        startActivityForResult(chooserIntent, 1);
    }

    public void downloadAssets(MenuItem item) {
        startActivity(downloadIntent);
    }

    public void changeAllShirt(MenuItem item) {
        shirtsDialog.show();
        changeImageTask = CHANGEALL;
    }

    public void changePitch(MenuItem item) {
        pitchDialog.show();
        changeImageTask = CHANGEPITCH;
    }
}