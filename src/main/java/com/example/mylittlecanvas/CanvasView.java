package com.example.mylittlecanvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.florent37.mylittlecanvas.animation.ShapeAnimator;
import com.github.florent37.mylittlecanvas.shape.ArcShape;
import com.github.florent37.mylittlecanvas.shape.CircleShape;
import com.github.florent37.mylittlecanvas.shape.RectShape;
import com.github.florent37.mylittlecanvas.shape.Shape;
import com.github.florent37.mylittlecanvas.shape.TextShape;
import com.github.florent37.mylittlecanvas.touch.EventPos;
import com.github.florent37.mylittlecanvas.touch.ShapeEventManager;
import com.github.florent37.mylittlecanvas.values.Alignment;

import java.util.ArrayList;
import java.util.List;

import static com.github.florent37.mylittlecanvas.CanvasHelper.dpToPx;

public class CanvasView extends View {

    private final CircleShape circle = new CircleShape();
    private ShapeEventManager shapeEventManager = new ShapeEventManager(this);

    public CanvasView(Context context) {
        super(context);
        init(context);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);

        final float radius = Math.min(height, width) / 4f;

        circle
                .setColor(Color.parseColor("#2E7D32"))
                .setBorderWidth(dpToPx(this, 1))
                .setBorderColor(Color.parseColor("#1B5E20"))
                .setRadius(radius * 0.75f)
                .centerVertical(height)
                .centerHorizontal(width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circle.onDraw(canvas);
    }

    private void init(Context context) {
        handleEvents();
    }

    private void handleEvents() {
        shapeEventManager
                .ifTouched(circle, (touchSetup) -> touchSetup
                        .move(circle, CircleShape.Pos.CENTER_X, EventPos.X)
                        .move(circle, CircleShape.Pos.CENTER_Y, EventPos.Y)
                )
                .ifClicked(circle, (clickedCircle) -> {
                    if(clickedCircle.getColor() == Color.parseColor("#2E7D32")) {
                        clickedCircle.setColor(Color.parseColor("#EB4034"));
                    } else {
                        clickedCircle.setColor(Color.parseColor("#2E7D32"));
                    }
                });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}

