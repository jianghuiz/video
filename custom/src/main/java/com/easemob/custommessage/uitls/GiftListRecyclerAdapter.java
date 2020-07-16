package com.easemob.custommessage.uitls;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.easemob.custommessage.R;
import com.easemob.custommessage.bean.GiftBean;

public class GiftListRecyclerAdapter extends EaseBaseRecyclerViewAdapter<GiftBean> {
    private int selectedPosition = 0;

    @Override
    public ViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new GiftListRecyclerAdapter.GiftViewHolder(LayoutInflater.from(mContext).inflate(R.layout.em_item_gift_recyclerview, parent, false));
    }

    private class GiftViewHolder extends ViewHolder<GiftBean> {
//        private ImageView ivGift;
        private TextView tvGiftName;
//        private LinearLayout  leaGiftView;
//        private TextView emGiftRule;

        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void initView(View itemView) {
            tvGiftName = findViewById(R.id.tv_gift_name);
//            leaGiftView=findViewById(R.id.em_item_gift_lineaview);
//            editGiftContent=findViewById(R.id.em_item_edit);
//            emGiftRule=findViewById(R.id.em_item_gift_rule);
        }

        @Override
        public void setData(GiftBean item, final int position) {
//            ivGift.setImageResource(item.getResource());
            tvGiftName.setText(item.getName());
            if(selectedPosition == position) {
                item.setChecked(true);
                tvGiftName.setBackground(ContextCompat.getDrawable(mContext, R.mipmap.em_live_select));
                tvGiftName.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            }else {
                item.setChecked(false);
                tvGiftName.setBackground(ContextCompat.getDrawable(mContext, R.drawable.em_bg_full_grey));
                tvGiftName.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            }
//            if(item.getName().equals("自定义")){
//                leaGiftView.setVisibility(View.VISIBLE);
//                emGiftRule.setVisibility(View.VISIBLE);
//                tvGiftName.setVisibility(View.GONE);
//            }else {
//                leaGiftView.setVisibility(View.GONE);
//                emGiftRule.setVisibility(View.GONE);
//                tvGiftName.setVisibility(View.VISIBLE);
//            }
            tvGiftName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener!=null){
                        mOnItemClickListener.onItemClick(v,position);
                    }
                }
            });
        }
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }

}
