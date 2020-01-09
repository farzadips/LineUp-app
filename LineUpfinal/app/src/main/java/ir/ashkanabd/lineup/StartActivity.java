package ir.ashkanabd.lineup;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.support.v7.app.*;
import android.os.*;
import android.support.v7.app.ActionBar;
import android.view.*;
import android.widget.*;

import java.util.*;

import es.dmoral.toasty.*;

public class StartActivity extends AppCompatActivity implements GridListViewAdapter.OnClick {

    public static String FORMULA = "formula";
    public static String SHIRT = "shirt";
    public static String NAMES = "playersName";
    public static String TEAMNAME = "name";
    public static String SENDDATA = "data";
    public static Bitmap SELECTEDSHIRT = null;
    public static ReadAssets GETASSETS = null;

    /**
     * use in {@link #submit(View)} and {@link #shirtBtn(View)} and {@link #gamePlanBtn(View)}
     * no need to define {@link Button}
     */
    private View view;
    private RelativeLayout.LayoutParams params;
    private Dialog shirtDialog, formulaDialog;
    private ActionBar actionBar;
    private boolean quit = false;
    private EditText editTeamName;
    private ReadAssets readAssets;
    private String formula = null;
    private Bitmap shirt = null;
    private List<ItemBitmap> bitmaps = new ArrayList<>();
    private List<String> formulas = new ArrayList<>();
    private ListView shirtList, formulaList;
    private CustomAdapter formulaAdapter;
    private GridListViewAdapter shirtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.tarkib_layout);
        readAssets = new ReadAssets(getApplicationContext());
        widgets();
        actionbar();
        buttonParams();
        readAssets.reloadAssets();
        shirtDialog = new Dialog(this, R.style.list_style);
        formulaDialog = new Dialog(this, R.style.list_style);
        shirtAdapter = new GridListViewAdapter(this, 4, this, getClass());
        formulas = setUpFormulas(formulas);
        formulaAdapter = new CustomAdapter(this, R.layout.formula_layout, formulas, this);
        prepareLists();
        onFormulaClicked(formulaList);
    }

    List<ItemBitmap> list(List<Bitmap> bitmapList) {
        bitmaps.clear();
        for (int i = 0; i < bitmapList.size(); i++) {
            if (bitmapList.get(i) != null)
                this.bitmaps.add(new ItemBitmap(bitmapList.get(i), i, bitmapList.get(i).hashCode()));
        }
        return this.bitmaps;
    }

    /**
     * @param formulaList set on item click listener
     */
    private void onFormulaClicked(ListView formulaList) {
        formulaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                formulaDialog.dismiss();
                ((Button) view).setText(formulas.get(position));
                formula = formulas.get(position);
            }
        });
    }

    /**
     * preparing {@link #shirtList} and {@link #shirtAdapter} to show in {@link #shirtDialog}
     * preparing {@link #formulaList} and {@link #formulaAdapter} to show in {@link #formulaDialog}
     */
    private void prepareLists() {
        shirtList = new ListView(this);
        shirtDialog.setContentView(shirtList, dialogLayout(toDp(500), toDp(300)));
        shirtAdapter.addItemsInGrid(list(readAssets.getAssetsShirts()));
        shirtAdapter.addItemsInGrid(list(readAssets.getDownloadBitmap()));
        shirtList.setAdapter(shirtAdapter);
        formulaList = new ListView(this);
        formulaList.setAdapter(formulaAdapter);
        formulaDialog.setContentView(formulaList, dialogLayout(toDp(400), toDp(250)));
    }

    /**
     * get data from this activity and send it to {@link MainActivity}
     *
     * @see MainActivity
     */
    public void submit(View view) {
        if (shirt == null) {
            Toasty.error(this, "Please select a T-Shirt", Toast.LENGTH_SHORT, true).show();
        } else if (formula == null) {
            Toasty.error(this, "Please select a formula", Toast.LENGTH_SHORT, true).show();
        } else if (editTeamName.getText().toString().isEmpty()) {
            Toasty.error(this, "Please enter team name", Toast.LENGTH_SHORT, true).show();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            Bundle sendData = new Bundle();
            sendData.putString(FORMULA, formula);
            sendData.putString(TEAMNAME, editTeamName.getText().toString());
            StartActivity.SELECTEDSHIRT = shirt;
            StartActivity.GETASSETS = readAssets;
//            intent.putExtra(SHIRT,shirt);
            sendData.putStringArray(NAMES, buildPlayersName());
            intent.putExtra(SENDDATA, sendData);
            startActivity(intent);
        }
    }

    private String[] buildPlayersName() {
        String[] names = new String[11];
        for (int i = 0; i < 11; i++) {
            names[i] = "";
        }
        return names;
    }

    /**
     * open {@link #shirtDialog} and fill it with {@link ReadAssets#getAssetsShirts()}
     * <p>
     * see also {@link Item}
     */
    public void shirtBtn(View view) {
        this.view = view;
        shirtDialog.show();
    }

    /**
     * open {@link #formulaDialog} and fill it with {@link #setUpFormulas(List)}
     */
    public void gamePlanBtn(View view) {
        this.view = view;
        formulaDialog.show();
    }

    /**
     * @param px size to pixels
     * @return size to dp
     */
    public int toDp(int px) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (px * scale + 0.5f);
    }

    public void buttonParams() {
        params = new RelativeLayout.LayoutParams(toDp(150), toDp(150));
        params.addRule(RelativeLayout.BELOW, R.id.teamNameEdit);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.topMargin = toDp(60);
    }

    /**
     * @param cardData call on {@link GridListViewAdapter#onCardClicked(ItemBitmap)}
     */
    @Override
    public void onCardClicked(ItemBitmap cardData, Class callerClass) {
        if (callerClass == getClass()) {
            view.setLayoutParams(params);
            view.setBackground(new BitmapDrawable(cardData.getBitmap()));
            ((TextView) view).setText("");
            shirtDialog.dismiss();
            shirt = cardData.getBitmap();
        }
    }

    /**
     * prepareShirtList custom {@link #actionBar}
     *
     * @see ActionBar
     */
    private void actionbar() {
        if (getSupportActionBar() != null) {
            actionBar = getSupportActionBar();
            actionBar.hide();
        }
    }

    /**
     * register views in {@link #setContentView(View)}
     */
    private void widgets() {
        editTeamName = (EditText) findViewById(R.id.teamNameEdit);
    }

    /**
     * Double tap when user want to quit
     *
     * @see AppCompatActivity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        if (!quit) {
            quit = true;
            Toasty.warning(this, "Press back again", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    quit = false;
                }
            }, 2000);
        } else
            super.onBackPressed();
    }

    /**
     * build {@link android.app.ActionBar.LayoutParams} for {@link #shirtDialog} and {@link #formulaDialog}
     *
     * @return {@link android.app.ActionBar.LayoutParams}
     * @see GridListViewAdapter
     * @see CustomAdapter
     */
    private ActionBar.LayoutParams dialogLayout(int height, int width) {
        return new ActionBar.LayoutParams(width, height);
    }

    /**
     * {@link #formulas} will fill with formulas in this method
     *
     * @param formulas an empty list that will use in {@link #formulaAdapter} and {@link #formulaList}
     * @return {@link #formulas}
     */
    private List<String> setUpFormulas(List<String> formulas) {
        formulas.add(Formula.F343);
        formulas.add(Formula.F352);
        formulas.add(Formula.F361);
        formulas.add(Formula.F424);
        formulas.add(Formula.F433);
        formulas.add(Formula.F442);
        formulas.add(Formula.F451);
        formulas.add(Formula.F532);
        formulas.add(Formula.F541);
        formulas.add(Formula.F4123);
        formulas.add(Formula.F4141);
        formulas.add(Formula.F4213);
        formulas.add(Formula.F4231);
        formulas.add(Formula.F4321);
        formulas.add(Formula.FCustom);
        return formulas;
    }
}
