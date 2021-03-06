package com.dtalk.dd.ui.widget.circle;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dtalk.dd.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * 微信朋友圈评论弹窗
 */
public class CommentPopup extends BasePopupWindow implements View.OnClickListener {

    private ImageView mLikeAnimaView;
    private TextView mLikeText;

    private RelativeLayout mLikeClikcLayout;
    private RelativeLayout mCommentClickLayout;


    private OnCommentPopupClickListener mOnCommentPopupClickListener;

    private Handler mHandler;

    public CommentPopup(Activity context) {
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public CommentPopup(Activity context, int w, int h) {
        super(context, w, h);

        mHandler = new Handler();

        mLikeAnimaView = (ImageView) findViewById(R.id.iv_like);
        mLikeText = (TextView) findViewById(R.id.tv_like);

        mLikeClikcLayout = (RelativeLayout) findViewById(R.id.item_like);
        mCommentClickLayout = (RelativeLayout) findViewById(R.id.item_comment);

        mLikeClikcLayout.setOnClickListener(this);
        mCommentClickLayout.setOnClickListener(this);

        buildAnima();
    }

    private AnimationSet mAnimationSet;

    private void buildAnima() {
        ScaleAnimation mScaleAnimation = new ScaleAnimation(1f, 2f, 1f, 2f, Animation.RELATIVE_TO_SELF, 0.5f,
                                                            Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setDuration(200);
        mScaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mScaleAnimation.setFillAfter(false);

        AlphaAnimation mAlphaAnimation = new AlphaAnimation(1, .2f);
        mAlphaAnimation.setDuration(400);
        mAlphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mAlphaAnimation.setFillAfter(false);

        mAnimationSet = new AnimationSet(false);
        mAnimationSet.setDuration(400);
        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.addAnimation(mAlphaAnimation);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 150);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void showPopupWindow(View v, boolean isFavor) {
        mLikeText.setText(isFavor ? "取消" : "赞");
        setRelativeToAnchorView(true);
        setRelativePivot(RelativePivot.RIGHT | RelativePivot.CENTER_Y);
        setOffsetX(v.getWidth() / 2);
        super.showPopupWindow(v);
    }

    @Override
    protected Animation initShowAnimation() {
        return getScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                                 Animation.RELATIVE_TO_SELF, 0.0f);
    }

    @Override
    public Animation initExitAnimation() {
        return getScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                                 Animation.RELATIVE_TO_SELF, 0.0f);
    }

    @Override
    public View onCreatePopupView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.popup_comment, null);
    }

    @Override
    public View initAnimaView() {
        return getPopupWindowView().findViewById(R.id.comment_popup_contianer);
    }
    //=============================================================Getter/Setter

    public OnCommentPopupClickListener getOnCommentPopupClickListener() {
        return mOnCommentPopupClickListener;
    }

    public void setOnCommentPopupClickListener(OnCommentPopupClickListener onCommentPopupClickListener) {
        mOnCommentPopupClickListener = onCommentPopupClickListener;
    }

    //=============================================================clickEvent
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_like:
                if (mOnCommentPopupClickListener != null) {
                    mOnCommentPopupClickListener.onLikeClick(v, mLikeText);
                    mLikeAnimaView.clearAnimation();
                    mLikeAnimaView.startAnimation(mAnimationSet);
                }
                break;
            case R.id.item_comment:
                if (mOnCommentPopupClickListener != null) {
                    mOnCommentPopupClickListener.onCommentClick(v);
                    dismiss();
                }
                break;
        }
    }

    //=============================================================InterFace
    public interface OnCommentPopupClickListener {
        void onLikeClick(View v, TextView likeText);

        void onCommentClick(View v);
    }
    //=============================================================abortMethods

    @Override
    public View getClickToDismissView() {
        return null;
    }
}
