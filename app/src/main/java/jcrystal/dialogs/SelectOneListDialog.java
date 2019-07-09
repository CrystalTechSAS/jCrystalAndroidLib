package jcrystal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import jcrystal.lib.R;

public class SelectOneListDialog<T> extends DialogFragment {

    public static<T> SelectOneListDialog<T> getInstance(List<T> items, SelectOneListDialogDataSource<T> datasource){
        return getInstance(items,datasource,null);
    }
    public static<T> SelectOneListDialog<T> getInstance(List<T> items, SelectOneListDialogDataSource<T> datasource, SelectOneListDialogDataSource<T> datasourceDesc){
        SelectOneListDialog<T> dialog = new SelectOneListDialog();
        dialog.items = items;
        dialog.procesorName = datasource;
        dialog.procesorDescriptions = datasourceDesc;
        return dialog;
    }

    public interface SelectOneListDialogListener<T>{
        void onDialogClick(T z);
    }
    public interface SelectOneListDialogDataSource<T>{
        String getName(T z);
    }
    public List<T> items;
    public SelectOneListDialogListener<T> listener;
    public SelectOneListDialogDataSource<T> procesorName;
    public SelectOneListDialogDataSource<T> procesorDescriptions;

    public RecyclerView.Adapter adapter;

    public String title;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_one_list, null);
        TextView title =  view.findViewById(R.id.title_dialogo);
        title.setText(this.title);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.element_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter= new ElementsAdapter(getContext()));
        builder.setView(view);
        return builder.create();
    }

    private class ElementsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        protected final LayoutInflater mInflater;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ElementsHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_select_one_cell, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ElementsHolder)holder).set(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public ElementsAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }
    }
    private class ElementsHolder extends RecyclerView.ViewHolder{
        private TextView txt_name;
        private TextView txt_desc;
        private RadioButton btn_radio;
        private View view;
        public ElementsHolder(View view) {
            super(view);
            this.view = view;
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_desc = (TextView) view.findViewById(R.id.txt_desc);
            btn_radio = (RadioButton)view.findViewById(R.id.btn_radio);
            btn_radio.setClickable(false);
        }
        public void set(final T element) {
            txt_name.setText(procesorName.getName(element));
            if(procesorDescriptions!= null)
                txt_desc.setText(procesorDescriptions.getName(element));
            else
                txt_desc.setVisibility(View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                        listener.onDialogClick(element);
                    btn_radio.setChecked(true);
                    SelectOneListDialog.this.dismiss();
                }
            });
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            recoverState(savedInstanceState);

    }

    private void recoverState(Bundle savedInstanceState){
        dismiss();
    }

    public void onPause(){
        super.onPause();
        this.dismiss();
    }
    public SelectOneListDialog<T> setTitle(String title){
        this.title = title;
        return this;
    }
    public SelectOneListDialog<T> setListener(SelectOneListDialogListener<T> listener){
        this.listener = listener;
        return this;
    }
}