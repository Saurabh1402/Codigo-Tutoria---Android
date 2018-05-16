package com.massacre.codigotutoria.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.massacre.codigotutoria.R;
import com.massacre.codigotutoria.models.ProgrammingLanguage;
import com.massacre.codigotutoria.utils.CodigoTutoriaConstant;
import com.massacre.codigotutoria.utils.ConfigUtils;
import com.massacre.codigotutoria.utils.LocalStorage;
import com.massacre.codigotutoria.utils.NetworkUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by saurabh on 16/8/17.
 */

public class LanguageHomeAdapter extends RecyclerView.Adapter<LanguageHomeAdapter.LanguageHomeViewHolder>{
    private int TYPE_LANGUAGE_HOME_CARD_VIEW =1;
    private List<ProgrammingLanguage> programmingLanguageList;
    private Context context;
    public LanguageHomeAdapter(List<ProgrammingLanguage> programmingLanguageList, Context context){
        this.programmingLanguageList=programmingLanguageList;
        this.context=context;
    }
    @Override
    public LanguageHomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("","LanguageHomeAdapter.onCreateViewHolder()");
        if(viewType==TYPE_LANGUAGE_HOME_CARD_VIEW){
            View languageHomeCardView= LayoutInflater.from(parent.getContext()).inflate(R.layout.language_home_card_view,parent,false);
            LanguageHomeViewHolder languageHomeViewHolder=new LanguageHomeViewHolder(languageHomeCardView,viewType,context);
            return languageHomeViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(LanguageHomeViewHolder holder, int position) {
        Log.e("Location","inside LanguageHomeAdapter.onBindViewHolder:holderId"+holder.holderId);

        if(holder.holderId==TYPE_LANGUAGE_HOME_CARD_VIEW){
            ProgrammingLanguage programmingLanguage=programmingLanguageList.get(position);
            holder.languageTitle.setText(programmingLanguage.getTitle());
            holder.languageId.setText(programmingLanguage.getLanguageId()+"");
            String imageFileName=CodigoTutoriaConstant.ABBR_PROGRAMMING_LANGUAGE+CodigoTutoriaConstant.DOT+
                    programmingLanguage.getLanguageId()+CodigoTutoriaConstant.DOT+
                    programmingLanguage.getImageResource();
            Bitmap bitmap=null;
            bitmap= new LocalStorage().getImage(
                        CodigoTutoriaConstant.IMAGE_FOLDER,
                        imageFileName,context);

            if(bitmap!=null){
                holder.languageImage.setImageBitmap(bitmap);
            }else{
                // ADD default language Image
                new NetworkUtils().downloadImage(programmingLanguage.getLanguageId(),programmingLanguage.getImageResource(),context);
            }

            holder.itemView.setBackgroundColor(Color.parseColor(programmingLanguage.getColorPrimary()));
        }
    }

    @Override
    public int getItemCount() {
        return programmingLanguageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_LANGUAGE_HOME_CARD_VIEW;
    }

    public void swapData(List<ProgrammingLanguage> data){
        this.programmingLanguageList.clear();
        this.programmingLanguageList.addAll(data);
        notifyDataSetChanged();
     //   runLayoutAnimation();
    }


    public class LanguageHomeViewHolder extends RecyclerView.ViewHolder{
        Context context;
        int holderId;
        TextView languageId;
        TextView languageTitle;
        ImageView languageImage;
        public LanguageHomeViewHolder(View view,int viewType,Context context){
            super(view);
            this.context=context;
            if(viewType== TYPE_LANGUAGE_HOME_CARD_VIEW){
                holderId=viewType;
                languageId=view.findViewById(R.id.texview_programminglanguage_id_home);
                languageImage=view.findViewById(R.id.imageview_programminglanguage_image_home);
                languageTitle=view.findViewById(R.id.textview_programminglanguage_title_home);
            }
        }
    }



}
