package tech.labs.rucker.llamachat.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tech.labs.rucker.llamachat.Model.ListItem;
import tech.labs.rucker.llamachat.R;

/**
 * Created by Carlos on 2/8/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
private List<ListItem> listItems;
private Context context;

public MessageAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
        }

@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.message_list_item, parent, false);
        return new ViewHolder(v);
        }


    @Override
public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {

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
        textViewHead = itemView.findViewById(R.id.textViewHead);
        textViewDesc = itemView.findViewById(R.id.textViewDesc);
    }
}
}
