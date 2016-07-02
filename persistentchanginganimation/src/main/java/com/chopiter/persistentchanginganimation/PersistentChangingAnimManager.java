package com.chopiter.persistentchanginganimation;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;

/**
 * Created by xiekejun on 16/7/2.
 */
public class PersistentChangingAnimManager {
    private static final String TAG = PersistentChangingAnimManager.class.getSimpleName();
    private AnimationInfo mAnimationInfo;
    private Animation.AnimationListener mOutListener;
    private AnimInterpolator mAnimInterpolator;
    //系统提供的一些Interpolator
    private Interpolator mInterpolator;
    private static final boolean logFlag = false;

    public interface AnimInterpolator {
        float getYfromX(float x);

        float getXfromY(float y);
    }

    public PersistentChangingAnimManager(AnimInterpolator interpolation) {
        mAnimInterpolator = interpolation;
    }

    public void setAnimationListener(Animation.AnimationListener listener) {
        mOutListener = listener;
    }
    public void setInterpolator(Interpolator interpolator){
        mInterpolator = interpolator;
    }

    public void startAnim(final View view, final Animation animation, final long duration) {
        if (animation != null) {
            if (mAnimationInfo == null) {
                mAnimationInfo = new AnimationInfo();
            }
            mAnimationInfo.reset();
            mAnimationInfo.setAnimationListener(mOutListener);
            mAnimationInfo.setAnimInterpolator(mAnimInterpolator);
            mAnimationInfo.setAnimation(animation);
            mAnimationInfo.setOriginalDuration(duration);
            mAnimationInfo.setInterpolator(mInterpolator);
            mAnimationInfo.startAnimation(view);

        }
    }

    private static class AnimationInfo {
        private Animation.AnimationListener mOutListener;
        private AnimInterpolator mAnimInterpolator;
        private Animation mAnimation;
        private long mOriginalDuration;
        private int mRepeatTime;
        private long mDuration;
        private float mCurrDx = 1;
        private Interpolator mInterpolator;
        AnimationInfo() {

        }

        public void reset() {
            if(mAnimation != null){
                mAnimation.cancel();
            }
            mOutListener = null;
            mAnimInterpolator = null;
        }

        public void setAnimationListener(Animation.AnimationListener listener) {
            mOutListener = listener;
        }

        public void setInterpolator(Interpolator interpolator){
            mInterpolator = interpolator;
        }
        public void setAnimInterpolator(AnimInterpolator animInterpolator) {
            this.mAnimInterpolator = animInterpolator;
        }

        public void setAnimation(Animation animation) {
            mAnimation = animation;
        }

        public void setOriginalDuration(long duration) {
            mOriginalDuration = duration;
        }

        public void startAnimation(final View v) {
            mAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (mOutListener != null) {
                        mOutListener.onAnimationStart(animation);
                    }
                    mRepeatTime = 0;
                    mDuration = mOriginalDuration;
                    mCurrDx = 1;
                    if (logFlag) {
                        Log.d(TAG, "onAnimationStart");
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mOutListener != null) {
                        mOutListener.onAnimationEnd(animation);
                    }
                    mRepeatTime = 0;
                    mDuration = mOriginalDuration;
                    mCurrDx = 1;
                    if (logFlag) {
                        Log.d(TAG, "onAnimationEnd");
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    if (mOutListener != null) {
                        mOutListener.onAnimationRepeat(animation);
                    }

                    mRepeatTime++;
                    //计算下一个区间
                    float nextDx = getXfromY(mRepeatTime + 1)
                            - getXfromY(mRepeatTime);
                    mDuration = (int) (mDuration * nextDx / mCurrDx);
                    if (mDuration <= 0) {
                        animation.cancel();
                        mDuration = mOriginalDuration;
                        return;
                    }
                    mCurrDx = nextDx;
                    animation.setDuration(mDuration);
                    if (logFlag) {
                        Log.d(TAG, "onAnimationRepeat");
                    }
                }
            });


            mAnimation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    float result;
                    //当前区间起点
                    float lastX = getXfromY(mRepeatTime);
                    //当前区间
                    float curDx = mCurrDx;
                    //把input映射到当前区间
                    float curx = lastX + curDx * input;
                    float curY = getYfromX(curx);
                    if (logFlag) {
                        Log.d(TAG, "curY : " + curY + ",duration: " + mDuration);
                    }
                    result = curY - mRepeatTime;

                    if (logFlag) {
                        Log.d(TAG, "result : " + result + ",duration: " + mDuration);
                    }


                    if(mInterpolator != null){
                        float output = mInterpolator.getInterpolation(result);
                        result = output;
                        if (logFlag) {
                            Log.e(TAG, "changed result : " + result + ",duration: " + mDuration);
                        }
                    }

                    if (mAnimation.hasEnded()) {
                        mAnimation.cancel();
                    }
                    return result;
                }
            });
            mAnimation.setDuration(mOriginalDuration);
            v.startAnimation(mAnimation);
        }

        private float getXfromY(float y) {
            if (mAnimInterpolator != null) {
                return mAnimInterpolator.getXfromY(y);
            }
            return 0;
        }

        private float getYfromX(float x) {
            if (mAnimInterpolator != null) {
                return mAnimInterpolator.getYfromX(x);
            }
            return 0;
        }

    }


}
