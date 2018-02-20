package tech.labs.rucker.llamachat;

/**
 * Created by Carlos on 2/19/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder>{
    private List<ListItem> listItems;
    private Context context;

    public ContactsAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_list_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder holder, int position) {

        ListItem listItem = listItems.get(position);
        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDesc.setText(listItem.getDesc());
    }

    @Override
    public int getItemCount() {
        return listItems.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewHead;
        public TextView textViewDesc;


        public ViewHolder(View itemView) {
            super(itemView);
            textViewHead = itemView.findViewById(R.id.contactNameLbl);
            textViewDesc = itemView.findViewById(R.id.contact);
        }
    }
}

