package alfred.eu.eventrecommendationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by MRguez.
 */
public abstract class ListAdapter extends BaseAdapter {

    private List<?> entries;
    private int R_layout_IdView;
    private Context context;

    public ListAdapter(Context context, int R_layout_IdView, List<?> entries) {
        super();
        this.context = context;
        this.entries = entries;
        this.R_layout_IdView = R_layout_IdView;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        //if (view == null) {
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = vi.inflate(R_layout_IdView, null);
        //view = vi.inflate(R_layout_IdView, parent, false);
        //}
        onEntry(entries.get(pos), view);
        return view;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int posicion) {
        return entries.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    /** Each entry
     * @param entry of type handler asociated to view
     * @param view with data from handler
     */
    public abstract void onEntry (Object entry, View view);

}