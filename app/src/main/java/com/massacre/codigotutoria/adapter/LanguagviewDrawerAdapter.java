package com.massacre.codigotutoria.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.massacre.codigotutoria.R;
import com.massacre.codigotutoria.activity.LanguageViewDrawer;
import com.massacre.codigotutoria.dbhelper.ProgrammingContract;
import com.massacre.codigotutoria.models.ProgrammingLanguage;

/**
 * Created by saurabh on 27/8/17.
 */

public class LanguagviewDrawerAdapter extends RecyclerView.Adapter<LanguagviewDrawerAdapter.LanguageViewDrawerViewHolder> {
    ProgrammingLanguage programmingLanguage;
    Context context;
    private final int TYPE_HEADER=1;
    private final int TYPE_SUBHEADER=2;
    private final int TYPE_ITEM=3;
    public Toast toast;
    public LanguagviewDrawerAdapter(ProgrammingLanguage programmingLanguage,Context context){
        this.programmingLanguage =programmingLanguage;
        this.context=context;
    }
    @Override
    public LanguageViewDrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LanguageViewDrawerViewHolder viewDrawerViewHolder;
        if(viewType==TYPE_ITEM){
            View view= LayoutInflater.from(context).inflate(R.layout.language_view_drawer_row_item,parent,false);
            viewDrawerViewHolder=new LanguageViewDrawerViewHolder(view,viewType,context);
            return viewDrawerViewHolder;
        }else if(viewType==TYPE_SUBHEADER){
            View view= LayoutInflater.from(context).inflate(R.layout.language_view_drawer_row_subheader,parent,false);
            viewDrawerViewHolder=new LanguageViewDrawerViewHolder(view,viewType,context);
            return viewDrawerViewHolder;
        }else if(viewType==TYPE_HEADER){
            View view= LayoutInflater.from(context).inflate(R.layout.language_view_drawer_header,parent,false);
            viewDrawerViewHolder=new LanguageViewDrawerViewHolder(view,viewType,context);
            return viewDrawerViewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(LanguageViewDrawerViewHolder holder, int position) {
        if(holder.holderId==TYPE_ITEM){
            int headerIndex=0;
            int itemIndex=0;
            int headerPosition=1;
            for (int i = 0; i < programmingLanguage.getHeaders().size(); i++) {
                if(position>headerPosition && headerPosition+programmingLanguage.getHeaders().get(i).getIndex().size()>=position){
                    headerIndex=i;
                    itemIndex=position-headerPosition-1;
                    break;
                }
                    headerPosition+=programmingLanguage.getHeaders().get(i).getIndex().size()+1;
                    Log.e("Position:", headerPosition+": "+headerIndex+","+itemIndex);

            }
            //programmingLanguage.getHeaders().get(headerIndex).getIndex().get(itemIndex).getIndexTitle()
            holder.languageViewDrawerRowItem_Name.setText(programmingLanguage.getHeaders().get(headerIndex).getIndex().get(itemIndex).getIndexTitle());
            holder.languageViewDrawerRowItem_Name.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf"));
            holder.languageViewDrawerRowItem_ItemIndexValue.setText(itemIndex+"");
            holder.languageViewDrawerRowItem_SubHeaderIndexValue.setText(headerIndex+"");
        }else if(holder.holderId==TYPE_SUBHEADER){
            int headerPosition=1;
            int index=0;
            for (int i = 0; i < programmingLanguage.getHeaders().size(); i++) {
                if(headerPosition==position){
                    index=i;
                    break;
                }
                headerPosition+=programmingLanguage.getHeaders().get(i).getIndex().size()+1;
            }
            holder.languageViewDrawerSubHeaderNameTextView.setText(programmingLanguage.getHeaders().get(index).getHeaderTitle());
        }else if(holder.holderId==TYPE_HEADER){
            holder.languageViewDrawerHeaderLanguageName.setText(programmingLanguage.getTitle());

        }

    }

    @Override
    public int getItemCount() {
        int total=1;//1 => Header
        total+=programmingLanguage.getHeaders().size(); // For Subheaders
        for (int i = 0; i < programmingLanguage.getHeaders().size(); i++) {
            total+=programmingLanguage.getHeaders().get(i).getIndex().size();
        }
        return total;
    }

    @Override
    public int getItemViewType(int position) {

        if(itemIsHeader(position))
            return TYPE_HEADER;
        else if(itemIsSubHeader(position))
            return TYPE_SUBHEADER;
        else
            return TYPE_ITEM;

    }

    public boolean itemIsHeader(int position){
        return position==0;
    }
    public boolean itemIsSubHeader(int position){
        int subHeaderPosition=1;
        for (int i = 0; i < programmingLanguage.getHeaders().size(); i++) {
            if(subHeaderPosition==position)return true;
            subHeaderPosition+=programmingLanguage.getHeaders().get(i).getIndex().size()+1;
        }
        return false;
    }

    public class LanguageViewDrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        int holderId;
        Context context;

        //Header
        TextView languageViewDrawerHeaderLanguageName;
        RelativeLayout languaviewDrawerHeaderBackgroundHolder;

        //SubHeader
        TextView languageViewDrawerSubHeaderNameTextView;
        ImageView languageSeparator;

        //ROW ITEM
        TextView languageViewDrawerRowItem_Name;
        TextView languageViewDrawerRowItem_SubHeaderIndexValue;
        TextView languageViewDrawerRowItem_ItemIndexValue;

        public LanguageViewDrawerViewHolder(View view, int viewType, Context context){
            super(view);
            this.context=context;
            this.holderId=viewType;
            if(viewType==TYPE_ITEM){
                languageViewDrawerRowItem_Name=(TextView)view.findViewById(R.id.language_view_drawer_row_item_name_textView);
                languageViewDrawerRowItem_SubHeaderIndexValue=(TextView)view.findViewById(R.id.language_view_drawer_row_item_subheader_index);
                languageViewDrawerRowItem_ItemIndexValue=(TextView)view.findViewById(R.id.language_view_drawer_row_item_item_index);
                languageViewDrawerRowItem_Name.setOnClickListener(this);
            }else if(viewType==TYPE_SUBHEADER){
                languageViewDrawerSubHeaderNameTextView=(TextView)view.findViewById(R.id.language_view_drawer_subheader_name_textView);
                languageSeparator=(ImageView)view.findViewById(R.id.language_view_drawer_subheader_horizontal_separator);
            }else if(viewType==TYPE_HEADER){
                languageViewDrawerHeaderLanguageName=(TextView)view.findViewById(R.id.language_view_drawer_header_language_name);
                languaviewDrawerHeaderBackgroundHolder=(RelativeLayout)view.findViewById(R.id.language_view_drawer_header_background_holder);
            }
        }

        @Override
        public void onClick(View view) {
            if (toast != null) toast.cancel();
            view = (View) view.getParent();
            Log.e("Location","LanguageViewDrawerAdapter.LanguageViewDrawerViewHolder.onClick()");
            int subheaderIndex=Integer.parseInt(((TextView) view.findViewById(R.id.language_view_drawer_row_item_subheader_index)).getText()+"");
            int itemIndex=Integer.parseInt(((TextView) view.findViewById(R.id.language_view_drawer_row_item_item_index)).getText()+"");
//            toast = Toast.makeText(context, subheaderIndex+ " "
//                            +itemIndex + " "
//                            ,
//                    Toast.LENGTH_LONG
//            );
//            toast.show();

            Intent webviewLoadBroadcastIndent=new Intent(LanguageViewDrawer.LANGUAGE_VIEW_LOAD_WEBVIEW_INTENT_FILTER_STRING);
            webviewLoadBroadcastIndent.putExtra(
                    ProgrammingContract.LanguageIndexDefinition.COLUMN_LANGUAGE_INDEX_ID,
                    programmingLanguage.getHeaders().get(subheaderIndex).getIndex().get(itemIndex).getLanguageIndexId());
            webviewLoadBroadcastIndent.putExtra(
                    ProgrammingContract.LanguageHeaderDefinition.COLUMN_LANGUAGE_HEADER_ID,
                    programmingLanguage.getHeaders().get(subheaderIndex).getLanguageHeaderId());
            LanguageViewDrawer.languageIndexId=programmingLanguage.getHeaders().get(subheaderIndex).getIndex().get(itemIndex).getLanguageIndexId();
            LanguageViewDrawer.languageHeaderId=programmingLanguage.getHeaders().get(subheaderIndex).getLanguageHeaderId();
            LocalBroadcastManager.getInstance(context).sendBroadcast(webviewLoadBroadcastIndent);

        }
    }
}
