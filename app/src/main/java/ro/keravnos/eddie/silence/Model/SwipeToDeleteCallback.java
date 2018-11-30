package ro.keravnos.eddie.silence.Model;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;



public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback
{
    private LocationsAdapter mAdapter;


    public SwipeToDeleteCallback( LocationsAdapter adapter )
    {
        super(0, ItemTouchHelper.RIGHT );
        mAdapter = adapter;
    }

    @Override
    public void onSwiped( @NonNull RecyclerView.ViewHolder viewHolder, int direction)
    {
        int position = viewHolder.getAdapterPosition();
        mAdapter.deleteItem(position);
    }

    @Override
    public boolean onMove( @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
    {
        // used for up and down movements
        return false;
    }


}