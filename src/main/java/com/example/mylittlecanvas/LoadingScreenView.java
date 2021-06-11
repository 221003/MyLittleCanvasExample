package com.example.mylittlecanvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.florent37.mylittlecanvas.animation.ShapeAnimator;
import com.github.florent37.mylittlecanvas.shape.CircleShape;
import com.github.florent37.mylittlecanvas.shape.RectShape;
import com.github.florent37.mylittlecanvas.shape.Shape;

import java.util.ArrayList;
import java.util.List;

import static com.github.florent37.mylittlecanvas.CanvasHelper.dpToPx;

public class LoadingScreenView extends FrameLayout {


    private final int[] colors = new int[]{
            Color.parseColor("#039BE5"),
            Color.parseColor("#D32F2F"),
            Color.parseColor("#FFB300"),
            Color.parseColor("#6D4C41"),
            Color.parseColor("#ED1B2F"),
    };

    private final int numberOfRectangles = 5;
    private final List<RectShape> rectangles = new ArrayList<>();

    private final AnimationHandler animationHandler = new AnimationHandler();

    public LoadingScreenView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public LoadingScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public LoadingScreenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);

        if(rectangles.isEmpty()){
            for (int i = 0; i < numberOfRectangles; i++) {
                rectangles.add(new RectShape());
            }
        }

        final float rectWidth = dpToPx(this, 16);
        final float distanceBetweenRectangles = dpToPx(this, 16);

        for (int i = 0; i < rectangles.size(); i++) {
            rectangles.get(i)
                    .setColor(colors[i % colors.length])
                    .setRect(0, 0, rectWidth, rectWidth)
                    .moveCenterYTo(height / 2f)
                    .moveCenterXTo((width / 2f) + ((rectWidth + distanceBetweenRectangles) * (i - rectangles.size() / 2)));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (RectShape rect : rectangles) {
            rect.onDraw(canvas);
        }
    }

    private class AnimationHandler extends Handler {
        private int animateRectIndex = 0;

        public AnimationHandler() {
            super(Looper.getMainLooper());
        }

        public void start() {
            animationLoop(this, animateRectIndex);
        }

        public void next() {
            animateRectIndex++;
            if (animateRectIndex == rectangles.size() - 1) {
                animateRectIndex = 0;
            }

            animationLoop(this, animateRectIndex);
        }
    }

    private void animationLoop(final AnimationHandler handler, int animateRectIndex) {
        final RectShape firstRect = rectangles.get(animateRectIndex);
        final RectShape nextRect = rectangles.get(animateRectIndex + 1);

        new ShapeAnimator(this).play(
                firstRect.animate().centerYTo(firstRect.getCenterY() + 100)
        ).onAnimationEnd(() -> {
                new ShapeAnimator(this).play(
                        nextRect.animate().centerXTo(firstRect.getCenterX()),
                        firstRect.animate().centerXTo(nextRect.getCenterX())
                ).onAnimationEnd(() -> {
                        new ShapeAnimator(this).play(
                                firstRect.animate().centerYTo(nextRect.getCenterY())
                        ).onAnimationEnd(() -> {
                                rectangles.set(animateRectIndex, nextRect);
                                rectangles.set(animateRectIndex + 1, firstRect);
                                handler.next();
                        }).start();
                }).start();
        }).start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(() -> animationHandler.start());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
