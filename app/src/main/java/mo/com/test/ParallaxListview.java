package mo.com.test;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Mo on 2017/9/8.
 */

public class ParallaxListview extends ListView {
    private ImageView iv_header;
    private int drawableHeight;//原始高度
    private int orignalHeight;//回弹高度

    public ParallaxListview(Context context) {
        this(context,null);
    }

    public ParallaxListview(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ParallaxListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setparallaxImage(ImageView iv_header){
         this.iv_header=iv_header;
        drawableHeight=iv_header.getDrawable().getIntrinsicHeight();//得到当前高低
        orignalHeight=iv_header.getHeight();//得到回弹高度
    }

    /**4
     *
     * @param deltaX
     * @param deltaY 数值方向滑动瞬时变化
     * @param scrollX
     * @param scrollY
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX
     * @param maxOverScrollY
     * @param isTouchEvent 是否是用户触摸拉动
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //顶部下拉,用户触摸的操作才执行视差效果
        if(deltaY<0&&isTouchEvent){
                    //deltay 是负值,我们要改为绝对值,累加给iv_header 高度
            int newHeight=iv_header.getHeight()+Math.abs(deltaY);
            //避免图片的无限放大  使图片最大不能超过图片本人身高度
            if(newHeight<=drawableHeight){
                //把新的高度赋值给控件,改变控件高度
                iv_header.getLayoutParams().height=newHeight;
                //刷新控件
                iv_header.requestLayout();
            }
            }


        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    /**
     * 复写触摸事件,让滑动的图片重新恢复到原有的样子
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case  MotionEvent.ACTION_UP:
            //把当前的头布局高度恢复初始高度
                int currentHeight=iv_header.getHeight();
                        //属性动画,改变高度的值,吧我们当前头布局的高度,改为原始高度
                ValueAnimator valueAnimator = ValueAnimator.ofInt(currentHeight, orignalHeight);
                //动画更新的监听
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        //获取动画执行过程的分度值
                        float fraction = valueAnimator.getAnimatedFraction();
                        //获取中间的值,并赋值给控件新高度,可以使控件平稳回弹效果
                        Integer animatedValue = (Integer) valueAnimator.getAnimatedValue();
                        //让新的高度值生效
                        iv_header.getLayoutParams().height=animatedValue;
                        //刷新数据
                        iv_header.requestLayout();
                    }
                });
                //动画回弹效果,值越大,回弹越厉害
                valueAnimator.setInterpolator(new OvershootInterpolator(2));
                //设置动画执行时间
                valueAnimator.setDuration(1000);
                //动画执行
                valueAnimator.start();
                break;
        }
        return super.onTouchEvent(ev);
    }
}
