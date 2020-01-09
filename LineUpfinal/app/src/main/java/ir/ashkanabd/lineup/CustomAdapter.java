package ir.ashkanabd.lineup;

import android.content.*;
import android.support.annotation.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;

import java.util.*;

/**
 * Adapter for custom {@link ListView}
 */
public class CustomAdapter extends ArrayAdapter<String> {

    private AppCompatActivity activity;

    public CustomAdapter(Context context, int resource, List<String> objects, AppCompatActivity activity) {
        super(context, resource, objects);
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.formula_layout, parent, false);
        TextView tv = (TextView) convertView.findViewById(R.id.formula);
        tv.setText(getItem(position));
        return convertView;
    }
}
