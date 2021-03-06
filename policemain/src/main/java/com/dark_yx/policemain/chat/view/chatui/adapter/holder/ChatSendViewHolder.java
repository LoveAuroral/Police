package com.dark_yx.policemain.chat.view.chatui.adapter.holder;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dark_yx.policemain.R;
import com.dark_yx.policemain.chat.beans.ChatMessage;
import com.dark_yx.policemain.chat.view.chatui.adapter.ChatAdapter;
import com.dark_yx.policemain.chat.view.chatui.util.Constants;
import com.dark_yx.policemain.chat.view.chatui.util.Utils;
import com.dark_yx.policemain.chat.view.chatui.widget.BubbleImageView;
import com.dark_yx.policemain.chat.view.chatui.widget.GifTextView;
import com.dark_yx.policemaincommon.Util.TimeUtil;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：Rance on 2016/11/29 10:47
 * 邮箱：rance935@163.com
 */
public class ChatSendViewHolder extends BaseViewHolder<ChatMessage> {
    @Bind(R.id.chat_item_date)
    TextView chatItemDate;
    @Bind(R.id.chat_item_header)
    CircularImageView chatItemHeader;
    @Bind(R.id.chat_item_content_text)
    GifTextView chatItemContentText;
    @Bind(R.id.chat_item_content_image)
    BubbleImageView chatItemContentImage;
    @Bind(R.id.chat_item_fail)
    ImageView chatItemFail;
    @Bind(R.id.chat_item_progress)
    ProgressBar chatItemProgress;
    @Bind(R.id.chat_item_voice)
    ImageView chatItemVoice;
    @Bind(R.id.chat_item_layout_content)
    LinearLayout chatItemLayoutContent;
    @Bind(R.id.chat_item_voice_time)
    TextView chatItemVoiceTime;
    private ChatAdapter.onItemClickListener onItemClickListener;
    private Handler handler;

    public ChatSendViewHolder(ViewGroup parent, ChatAdapter.onItemClickListener onItemClickListener, Handler handler) {
        super(parent, R.layout.item_chat_send);
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
    }


    @Override
    public void setData(ChatMessage data) {
        chatItemDate.setText(TimeUtil.getChatTime(data.getTickets()));
        chatItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onHeaderClick(getDataPosition());
            }
        });
        chatItemFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onSendFailClick(v, getDataPosition());
            }
        });


        switch (data.getChatMessageType()) {
            case Constants.TEXT:
                chatItemContentText.setSpanText(handler, data.getMessage(), true);
                chatItemVoice.setVisibility(View.GONE);
                chatItemContentText.setVisibility(View.VISIBLE);
                chatItemLayoutContent.setVisibility(View.VISIBLE);
                chatItemVoiceTime.setVisibility(View.GONE);
                chatItemContentImage.setVisibility(View.GONE);
                chatItemLayoutContent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onTextLongClick(v, getDataPosition());
                        return true;
                    }
                });
                break;
            case Constants.AUDIO:
                dealVoice(data);
                break;
            case Constants.PICTURE:
                chatItemVoice.setVisibility(View.GONE);
                chatItemLayoutContent.setVisibility(View.GONE);
                chatItemVoiceTime.setVisibility(View.GONE);
                chatItemContentText.setVisibility(View.GONE);
                chatItemContentImage.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(data.getFilePath()).into(chatItemContentImage);
                chatItemContentImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onImageClick(chatItemContentImage, getDataPosition());
                    }
                });
                chatItemContentImage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onImageLongClick(v, getDataPosition());
                        return true;
                    }
                });
                break;
            case Constants.PTT:
                dealVoice(data);
                break;
            case Constants.CANCEL:
                break;
        }

        switch (data.getSendState()) {
            case Constants.CHAT_ITEM_SENDING:
                chatItemProgress.setVisibility(View.VISIBLE);
                chatItemFail.setVisibility(View.GONE);
                break;
            case Constants.CHAT_ITEM_SEND_ERROR:
                chatItemProgress.setVisibility(View.GONE);
                chatItemFail.setVisibility(View.VISIBLE);
                break;
            case Constants.CHAT_ITEM_SEND_SUCCESS:
                chatItemProgress.setVisibility(View.GONE);
                chatItemFail.setVisibility(View.GONE);
                break;
        }
    }

    private void dealVoice(ChatMessage data) {
        chatItemVoice.setVisibility(View.VISIBLE);
        chatItemLayoutContent.setVisibility(View.VISIBLE);
        chatItemContentText.setVisibility(View.GONE);
        chatItemVoiceTime.setVisibility(View.VISIBLE);
        chatItemContentImage.setVisibility(View.GONE);
        chatItemVoiceTime.setText(Utils.formatTime(data.getDuration()));
        chatItemLayoutContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onVoiceClick(chatItemVoice, getDataPosition());
            }
        });
        chatItemLayoutContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onVoiceLongClick(v, getDataPosition());
                return true;
            }
        });
    }
}
