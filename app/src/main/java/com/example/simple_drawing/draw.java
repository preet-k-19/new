package com.example.simple_drawing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class draw extends View {

    private final Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;

    private float previousX, previousY;

    private Path path;

    private final Stack<Pair<Path, Paint>> pathsStack;
    private final List<Pair<Path, Paint>> pathsList;

    private float lastTouchX;
    private float lastTouchY;



    public draw(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(30);

        pathsStack = new Stack<>();
        pathsList = new ArrayList<>();

        previousX = 0;
        previousY = 0;
    }

    private boolean isEraserMode = false;

    // Other existing methods...

    public void setEraserMode(boolean isEraser) {
        isEraserMode = isEraser;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
        if (path != null) {
            canvas.drawPath(path, paint);
        }
        for (Pair<Path, Paint> pair : pathsList) {
            canvas.drawPath(pair.first, pair.second);
        }
    }



    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isEraserMode) {
                    lastTouchX = currentX;
                    lastTouchY = currentY;
                    erase();
                } else {
                    // Your drawing logic when not in eraser mode
                    path = new Path();
                    path.moveTo(currentX, currentY);
                    previousX = currentX;
                    previousY = currentY;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (!isEraserMode) {
                    // Your drawing logic when not in eraser mode
                    path.quadTo(previousX, previousY, (currentX + previousX) / 2, (currentY + previousY) / 2);
                    previousX = currentX;
                    previousY = currentY;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (!isEraserMode) {
                    // Your drawing logic when not in eraser mode
                    canvas.drawPath(path, paint);
                    pathsList.add(new Pair<>(path, new Paint(paint)));
                    path = null;
                    addToUndoStack();
                }
                break;
        }
        invalidate();
        return true;
    }

    public void setDrawingColor(int color) {
        paint.setColor(color);
    }

    public void erase() {
        int eraseRadius = 50; // You can adjust the eraser size as needed

        // Create a new path representing the erase area
        Path erasePath = new Path();
        erasePath.addCircle(lastTouchX, lastTouchY, eraseRadius, Path.Direction.CW);

        // Set the PorterDuff mode to clear, which makes the pixels transparent
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        // Draw the erase path with the clear mode
        canvas.drawPath(erasePath, paint);

        // Reset the Xfermode to null (source-over mode)
        paint.setXfermode(null);

        // Invalidate to refresh the view
        invalidate();
    }


    public void undo() {
        if (!pathsList.isEmpty()) {
            pathsStack.push(pathsList.remove(pathsList.size() - 1));
            redrawPaths();
        }
    }

    private void redrawPaths() {
        bitmap.eraseColor(Color.TRANSPARENT);
        for (Pair<Path, Paint> pair : pathsList) {
            canvas.drawPath(pair.first, pair.second);
        }
        invalidate();
    }

    private void addToUndoStack() {
        pathsStack.clear();
    }

}
