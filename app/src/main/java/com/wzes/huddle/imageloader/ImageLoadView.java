package com.wzes.huddle.imageloader;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.android.volley.DefaultRetryPolicy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ImageLoadView extends ImageView {
    public static final float FLING_DAMPING_FACTOR = 0.9f;
    private static final float MAX_SCALE = 4.0f;
    public static final int PINCH_MODE_FREE = 0;
    public static final int PINCH_MODE_SCALE = 2;
    public static final int PINCH_MODE_SCROLL = 1;
    public static final int SCALE_ANIMATOR_DURATION = 200;
    private int mDispatchOuterMatrixChangedLock;
    private FlingAnimator mFlingAnimator;
    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new C04821());
    private PointF mLastMovePoint = new PointF();
    private RectF mMask;
    private MaskAnimator mMaskAnimator;
    private OnClickListener mOnClickListener;
    private OnLongClickListener mOnLongClickListener;
    private Matrix mOuterMatrix = new Matrix();
    private List<OuterMatrixChangedListener> mOuterMatrixChangedListeners;
    private List<OuterMatrixChangedListener> mOuterMatrixChangedListenersCopy;
    private int mPinchMode = 0;
    private ScaleAnimator mScaleAnimator;
    private float mScaleBase = 0.0f;
    private PointF mScaleCenter = new PointF();

    class C04821 extends SimpleOnGestureListener {
        C04821() {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (ImageLoadView.this.mPinchMode == 0 && (ImageLoadView.this.mScaleAnimator == null || !ImageLoadView.this.mScaleAnimator.isRunning())) {
                ImageLoadView.this.fling(velocityX, velocityY);
            }
            return true;
        }

        public void onLongPress(MotionEvent e) {
            if (ImageLoadView.this.mOnLongClickListener != null) {
                ImageLoadView.this.mOnLongClickListener.onLongClick(ImageLoadView.this);
            }
        }

        public boolean onDoubleTap(MotionEvent e) {
            if (ImageLoadView.this.mPinchMode == 1 && (ImageLoadView.this.mScaleAnimator == null || !ImageLoadView.this.mScaleAnimator.isRunning())) {
                ImageLoadView.this.doubleTap(e.getX(), e.getY());
            }
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (ImageLoadView.this.mOnClickListener != null) {
                ImageLoadView.this.mOnClickListener.onClick(ImageLoadView.this);
            }
            return true;
        }
    }

    private class FlingAnimator extends ValueAnimator implements AnimatorUpdateListener {
        private float[] mVector;

        public FlingAnimator(float vectorX, float vectorY) {
            setFloatValues(new float[]{0.0f, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT});
            setDuration(1000000);
            addUpdateListener(this);
            this.mVector = new float[]{vectorX, vectorY};
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            boolean result = ImageLoadView.this.scrollBy(this.mVector[0], this.mVector[1]);
            float[] fArr = this.mVector;
            fArr[0] = fArr[0] * ImageLoadView.FLING_DAMPING_FACTOR;
            fArr = this.mVector;
            fArr[1] = fArr[1] * ImageLoadView.FLING_DAMPING_FACTOR;
            if (!result || MathUtils.getDistance(0.0f, 0.0f, this.mVector[0], this.mVector[1]) < DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
                animation.cancel();
            }
        }
    }

    private class MaskAnimator extends ValueAnimator implements AnimatorUpdateListener {
        private float[] mEnd = new float[4];
        private float[] mResult = new float[4];
        private float[] mStart = new float[4];

        public MaskAnimator(RectF start, RectF end, long duration) {
            setFloatValues(new float[]{0.0f, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT});
            setDuration(duration);
            addUpdateListener(this);
            this.mStart[0] = start.left;
            this.mStart[1] = start.top;
            this.mStart[2] = start.right;
            this.mStart[3] = start.bottom;
            this.mEnd[0] = end.left;
            this.mEnd[1] = end.top;
            this.mEnd[2] = end.right;
            this.mEnd[3] = end.bottom;
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            float value = ((Float) animation.getAnimatedValue()).floatValue();
            for (int i = 0; i < 4; i++) {
                this.mResult[i] = this.mStart[i] + ((this.mEnd[i] - this.mStart[i]) * value);
            }
            if (ImageLoadView.this.mMask == null) {
                ImageLoadView.this.mMask = new RectF();
            }
            ImageLoadView.this.mMask.set(this.mResult[0], this.mResult[1], this.mResult[2], this.mResult[3]);
            ImageLoadView.this.invalidate();
        }
    }

    public static class MathUtils {
        private static MatrixPool mMatrixPool = new MatrixPool(16);
        private static RectFPool mRectFPool = new RectFPool(16);

        public static Matrix matrixTake() {
            return (Matrix) mMatrixPool.take();
        }

        public static Matrix matrixTake(Matrix matrix) {
            Matrix result = (Matrix) mMatrixPool.take();
            if (matrix != null) {
                result.set(matrix);
            }
            return result;
        }

        public static void matrixGiven(Matrix matrix) {
            mMatrixPool.given(matrix);
        }

        public static RectF rectFTake() {
            return (RectF) mRectFPool.take();
        }

        public static RectF rectFTake(float left, float top, float right, float bottom) {
            RectF result = (RectF) mRectFPool.take();
            result.set(left, top, right, bottom);
            return result;
        }

        public static RectF rectFTake(RectF rectF) {
            RectF result = (RectF) mRectFPool.take();
            if (rectF != null) {
                result.set(rectF);
            }
            return result;
        }

        public static void rectFGiven(RectF rectF) {
            mRectFPool.given(rectF);
        }

        public static float getDistance(float x1, float y1, float x2, float y2) {
            float x = x1 - x2;
            float y = y1 - y2;
            return (float) Math.sqrt((double) ((x * x) + (y * y)));
        }

        public static float[] getCenterPoint(float x1, float y1, float x2, float y2) {
            return new float[]{(x1 + x2) / 2.0f, (y1 + y2) / 2.0f};
        }

        public static float[] getMatrixScale(Matrix matrix) {
            if (matrix == null) {
                return new float[2];
            }
            matrix.getValues(new float[9]);
            return new float[]{value[0], value[4]};
        }

        public static float[] inverseMatrixPoint(float[] point, Matrix matrix) {
            if (point == null || matrix == null) {
                return new float[2];
            }
            float[] dst = new float[2];
            Matrix inverse = matrixTake();
            matrix.invert(inverse);
            inverse.mapPoints(dst, point);
            matrixGiven(inverse);
            return dst;
        }

        public static void calculateRectTranslateMatrix(RectF from, RectF to, Matrix result) {
            if (from != null && to != null && result != null && from.width() != 0.0f && from.height() != 0.0f) {
                result.reset();
                result.postTranslate(-from.left, -from.top);
                result.postScale(to.width() / from.width(), to.height() / from.height());
                result.postTranslate(to.left, to.top);
            }
        }

        public static void calculateScaledRectInContainer(RectF container, float srcWidth, float srcHeight, ScaleType scaleType, RectF result) {
            if (container != null && result != null && srcWidth != 0.0f && srcHeight != 0.0f) {
                if (scaleType == null) {
                    scaleType = ScaleType.FIT_CENTER;
                }
                result.setEmpty();
                if (ScaleType.FIT_XY.equals(scaleType)) {
                    result.set(container);
                } else if (ScaleType.CENTER.equals(scaleType)) {
                    matrix = matrixTake();
                    rect = rectFTake(0.0f, 0.0f, srcWidth, srcHeight);
                    matrix.setTranslate((container.width() - srcWidth) * 0.5f, (container.height() - srcHeight) * 0.5f);
                    matrix.mapRect(result, rect);
                    rectFGiven(rect);
                    matrixGiven(matrix);
                    result.left += container.left;
                    result.right += container.left;
                    result.top += container.top;
                    result.bottom += container.top;
                } else if (ScaleType.CENTER_CROP.equals(scaleType)) {
                    matrix = matrixTake();
                    rect = rectFTake(0.0f, 0.0f, srcWidth, srcHeight);
                    dx = 0.0f;
                    dy = 0.0f;
                    if (container.height() * srcWidth > container.width() * srcHeight) {
                        scale = container.height() / srcHeight;
                        dx = (container.width() - (srcWidth * scale)) * 0.5f;
                    } else {
                        scale = container.width() / srcWidth;
                        dy = (container.height() - (srcHeight * scale)) * 0.5f;
                    }
                    matrix.setScale(scale, scale);
                    matrix.postTranslate(dx, dy);
                    matrix.mapRect(result, rect);
                    rectFGiven(rect);
                    matrixGiven(matrix);
                    result.left += container.left;
                    result.right += container.left;
                    result.top += container.top;
                    result.bottom += container.top;
                } else if (ScaleType.CENTER_INSIDE.equals(scaleType)) {
                    matrix = matrixTake();
                    rect = rectFTake(0.0f, 0.0f, srcWidth, srcHeight);
                    if (srcWidth > container.width() || srcHeight > container.height()) {
                        scale = Math.min(container.width() / srcWidth, container.height() / srcHeight);
                    } else {
                        scale = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
                    }
                    dx = (container.width() - (srcWidth * scale)) * 0.5f;
                    dy = (container.height() - (srcHeight * scale)) * 0.5f;
                    matrix.setScale(scale, scale);
                    matrix.postTranslate(dx, dy);
                    matrix.mapRect(result, rect);
                    rectFGiven(rect);
                    matrixGiven(matrix);
                    result.left += container.left;
                    result.right += container.left;
                    result.top += container.top;
                    result.bottom += container.top;
                } else if (ScaleType.FIT_CENTER.equals(scaleType)) {
                    matrix = matrixTake();
                    rect = rectFTake(0.0f, 0.0f, srcWidth, srcHeight);
                    tempSrc = rectFTake(0.0f, 0.0f, srcWidth, srcHeight);
                    tempDst = rectFTake(0.0f, 0.0f, container.width(), container.height());
                    matrix.setRectToRect(tempSrc, tempDst, ScaleToFit.CENTER);
                    matrix.mapRect(result, rect);
                    rectFGiven(tempDst);
                    rectFGiven(tempSrc);
                    rectFGiven(rect);
                    matrixGiven(matrix);
                    result.left += container.left;
                    result.right += container.left;
                    result.top += container.top;
                    result.bottom += container.top;
                } else if (ScaleType.FIT_START.equals(scaleType)) {
                    matrix = matrixTake();
                    rect = rectFTake(0.0f, 0.0f, srcWidth, srcHeight);
                    tempSrc = rectFTake(0.0f, 0.0f, srcWidth, srcHeight);
                    tempDst = rectFTake(0.0f, 0.0f, container.width(), container.height());
                    matrix.setRectToRect(tempSrc, tempDst, ScaleToFit.START);
                    matrix.mapRect(result, rect);
                    rectFGiven(tempDst);
                    rectFGiven(tempSrc);
                    rectFGiven(rect);
                    matrixGiven(matrix);
                    result.left += container.left;
                    result.right += container.left;
                    result.top += container.top;
                    result.bottom += container.top;
                } else if (ScaleType.FIT_END.equals(scaleType)) {
                    matrix = matrixTake();
                    rect = rectFTake(0.0f, 0.0f, srcWidth, srcHeight);
                    tempSrc = rectFTake(0.0f, 0.0f, srcWidth, srcHeight);
                    tempDst = rectFTake(0.0f, 0.0f, container.width(), container.height());
                    matrix.setRectToRect(tempSrc, tempDst, ScaleToFit.END);
                    matrix.mapRect(result, rect);
                    rectFGiven(tempDst);
                    rectFGiven(tempSrc);
                    rectFGiven(rect);
                    matrixGiven(matrix);
                    result.left += container.left;
                    result.right += container.left;
                    result.top += container.top;
                    result.bottom += container.top;
                } else {
                    result.set(container);
                }
            }
        }
    }

    private static abstract class ObjectsPool<T> {
        private Queue<T> mQueue = new LinkedList();
        private int mSize;

        protected abstract T newInstance();

        protected abstract T resetInstance(T t);

        public ObjectsPool(int size) {
            this.mSize = size;
        }

        public T take() {
            if (this.mQueue.size() == 0) {
                return newInstance();
            }
            return resetInstance(this.mQueue.poll());
        }

        public void given(T obj) {
            if (obj != null && this.mQueue.size() < this.mSize) {
                this.mQueue.offer(obj);
            }
        }
    }

    public interface OuterMatrixChangedListener {
        void onOuterMatrixChanged(ImageLoadView imageLoadView);
    }

    private class ScaleAnimator extends ValueAnimator implements AnimatorUpdateListener {
        private float[] mEnd;
        private float[] mResult;
        private float[] mStart;

        public ScaleAnimator(ImageLoadView imageLoadView, Matrix start, Matrix end) {
            this(start, end, 200);
        }

        public ScaleAnimator(Matrix start, Matrix end, long duration) {
            this.mStart = new float[9];
            this.mEnd = new float[9];
            this.mResult = new float[9];
            setFloatValues(new float[]{0.0f, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT});
            setDuration(duration);
            addUpdateListener(this);
            start.getValues(this.mStart);
            end.getValues(this.mEnd);
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            float value = ((Float) animation.getAnimatedValue()).floatValue();
            for (int i = 0; i < 9; i++) {
                this.mResult[i] = this.mStart[i] + ((this.mEnd[i] - this.mStart[i]) * value);
            }
            ImageLoadView.this.mOuterMatrix.setValues(this.mResult);
            ImageLoadView.this.dispatchOuterMatrixChanged();
            ImageLoadView.this.invalidate();
        }
    }

    private static class MatrixPool extends ObjectsPool<Matrix> {
        public MatrixPool(int size) {
            super(size);
        }

        protected Matrix newInstance() {
            return new Matrix();
        }

        protected Matrix resetInstance(Matrix obj) {
            obj.reset();
            return obj;
        }
    }

    private static class RectFPool extends ObjectsPool<RectF> {
        public RectFPool(int size) {
            super(size);
        }

        protected RectF newInstance() {
            return new RectF();
        }

        protected RectF resetInstance(RectF obj) {
            obj.setEmpty();
            return obj;
        }
    }

    public void setOnClickListener(OnClickListener l) {
        this.mOnClickListener = l;
    }

    public void setOnLongClickListener(OnLongClickListener l) {
        this.mOnLongClickListener = l;
    }

    public Matrix getOuterMatrix(Matrix matrix) {
        if (matrix == null) {
            return new Matrix(this.mOuterMatrix);
        }
        matrix.set(this.mOuterMatrix);
        return matrix;
    }

    public Matrix getInnerMatrix(Matrix matrix) {
        if (matrix == null) {
            matrix = new Matrix();
        } else {
            matrix.reset();
        }
        if (isReady()) {
            RectF tempSrc = MathUtils.rectFTake(0.0f, 0.0f, (float) getDrawable().getIntrinsicWidth(), (float) getDrawable().getIntrinsicHeight());
            RectF tempDst = MathUtils.rectFTake(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
            matrix.setRectToRect(tempSrc, tempDst, ScaleToFit.CENTER);
            MathUtils.rectFGiven(tempDst);
            MathUtils.rectFGiven(tempSrc);
        }
        return matrix;
    }

    public Matrix getCurrentImageMatrix(Matrix matrix) {
        matrix = getInnerMatrix(matrix);
        matrix.postConcat(this.mOuterMatrix);
        return matrix;
    }

    public RectF getImageBound(RectF rectF) {
        if (rectF == null) {
            rectF = new RectF();
        } else {
            rectF.setEmpty();
        }
        if (isReady()) {
            Matrix matrix = MathUtils.matrixTake();
            getCurrentImageMatrix(matrix);
            rectF.set(0.0f, 0.0f, (float) getDrawable().getIntrinsicWidth(), (float) getDrawable().getIntrinsicHeight());
            matrix.mapRect(rectF);
            MathUtils.matrixGiven(matrix);
        }
        return rectF;
    }

    public RectF getMask() {
        if (this.mMask != null) {
            return new RectF(this.mMask);
        }
        return null;
    }

    public int getPinchMode() {
        return this.mPinchMode;
    }

    public boolean canScrollHorizontally(int direction) {
        if (this.mPinchMode == 2) {
            return true;
        }
        RectF bound = getImageBound(null);
        if (bound == null) {
            return false;
        }
        if (bound.isEmpty()) {
            return false;
        }
        if (direction > 0) {
            if (bound.right <= ((float) getWidth())) {
                return false;
            }
            return true;
        } else if (bound.left >= 0.0f) {
            return false;
        } else {
            return true;
        }
    }

    public boolean canScrollVertically(int direction) {
        if (this.mPinchMode == 2) {
            return true;
        }
        RectF bound = getImageBound(null);
        if (bound == null) {
            return false;
        }
        if (bound.isEmpty()) {
            return false;
        }
        if (direction > 0) {
            if (bound.bottom <= ((float) getHeight())) {
                return false;
            }
            return true;
        } else if (bound.top >= 0.0f) {
            return false;
        } else {
            return true;
        }
    }

    public void outerMatrixTo(Matrix endMatrix, long duration) {
        if (endMatrix != null) {
            this.mPinchMode = 0;
            cancelAllAnimator();
            if (duration <= 0) {
                this.mOuterMatrix.set(endMatrix);
                dispatchOuterMatrixChanged();
                invalidate();
                return;
            }
            this.mScaleAnimator = new ScaleAnimator(this.mOuterMatrix, endMatrix, duration);
            this.mScaleAnimator.start();
        }
    }

    public void zoomMaskTo(RectF mask, long duration) {
        if (mask != null) {
            if (this.mMaskAnimator != null) {
                this.mMaskAnimator.cancel();
                this.mMaskAnimator = null;
            }
            if (duration <= 0 || this.mMask == null) {
                if (this.mMask == null) {
                    this.mMask = new RectF();
                }
                this.mMask.set(mask);
                invalidate();
                return;
            }
            this.mMaskAnimator = new MaskAnimator(this.mMask, mask, duration);
            this.mMaskAnimator.start();
        }
    }

    public void reset() {
        this.mOuterMatrix.reset();
        dispatchOuterMatrixChanged();
        this.mMask = null;
        this.mPinchMode = 0;
        this.mLastMovePoint.set(0.0f, 0.0f);
        this.mScaleCenter.set(0.0f, 0.0f);
        this.mScaleBase = 0.0f;
        if (this.mMaskAnimator != null) {
            this.mMaskAnimator.cancel();
            this.mMaskAnimator = null;
        }
        cancelAllAnimator();
        invalidate();
    }

    public void addOuterMatrixChangedListener(OuterMatrixChangedListener listener) {
        if (listener != null) {
            if (this.mDispatchOuterMatrixChangedLock == 0) {
                if (this.mOuterMatrixChangedListeners == null) {
                    this.mOuterMatrixChangedListeners = new ArrayList();
                }
                this.mOuterMatrixChangedListeners.add(listener);
                return;
            }
            if (this.mOuterMatrixChangedListenersCopy == null) {
                if (this.mOuterMatrixChangedListeners != null) {
                    this.mOuterMatrixChangedListenersCopy = new ArrayList(this.mOuterMatrixChangedListeners);
                } else {
                    this.mOuterMatrixChangedListenersCopy = new ArrayList();
                }
            }
            this.mOuterMatrixChangedListenersCopy.add(listener);
        }
    }

    public void removeOuterMatrixChangedListener(OuterMatrixChangedListener listener) {
        if (listener != null) {
            if (this.mDispatchOuterMatrixChangedLock != 0) {
                if (this.mOuterMatrixChangedListenersCopy == null && this.mOuterMatrixChangedListeners != null) {
                    this.mOuterMatrixChangedListenersCopy = new ArrayList(this.mOuterMatrixChangedListeners);
                }
                if (this.mOuterMatrixChangedListenersCopy != null) {
                    this.mOuterMatrixChangedListenersCopy.remove(listener);
                }
            } else if (this.mOuterMatrixChangedListeners != null) {
                this.mOuterMatrixChangedListeners.remove(listener);
            }
        }
    }

    private void dispatchOuterMatrixChanged() {
        if (this.mOuterMatrixChangedListeners != null) {
            this.mDispatchOuterMatrixChangedLock++;
            for (OuterMatrixChangedListener listener : this.mOuterMatrixChangedListeners) {
                listener.onOuterMatrixChanged(this);
            }
            this.mDispatchOuterMatrixChangedLock--;
            if (this.mDispatchOuterMatrixChangedLock == 0 && this.mOuterMatrixChangedListenersCopy != null) {
                this.mOuterMatrixChangedListeners = this.mOuterMatrixChangedListenersCopy;
                this.mOuterMatrixChangedListenersCopy = null;
            }
        }
    }

    protected float getMaxScale() {
        return MAX_SCALE;
    }

    protected float calculateNextScale(float innerScale, float outerScale) {
        return innerScale * outerScale < MAX_SCALE ? MAX_SCALE : innerScale;
    }

    public ImageLoadView(Context context) {
        super(context);
        initView();
    }

    public ImageLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ImageLoadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        super.setScaleType(ScaleType.MATRIX);
    }

    public void setScaleType(ScaleType scaleType) {
    }

    protected void onDraw(Canvas canvas) {
        if (isReady()) {
            Matrix matrix = MathUtils.matrixTake();
            setImageMatrix(getCurrentImageMatrix(matrix));
            MathUtils.matrixGiven(matrix);
        }
        if (this.mMask != null) {
            canvas.save();
            canvas.clipRect(this.mMask);
            super.onDraw(canvas);
            canvas.restore();
            return;
        }
        super.onDraw(canvas);
    }

    private boolean isReady() {
        return getDrawable() != null && getDrawable().getIntrinsicWidth() > 0 && getDrawable().getIntrinsicHeight() > 0 && getWidth() > 0 && getHeight() > 0;
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int action = event.getAction() & 255;
        if (action == 1 || action == 3) {
            if (this.mPinchMode == 2) {
                scaleEnd();
            }
            this.mPinchMode = 0;
        } else if (action == 6) {
            if (this.mPinchMode == 2 && event.getPointerCount() > 2) {
                if ((event.getAction() >> 8) == 0) {
                    saveScaleContext(event.getX(1), event.getY(1), event.getX(2), event.getY(2));
                } else if ((event.getAction() >> 8) == 1) {
                    saveScaleContext(event.getX(0), event.getY(0), event.getX(2), event.getY(2));
                }
            }
        } else if (action == 0) {
            if (this.mScaleAnimator == null || !this.mScaleAnimator.isRunning()) {
                cancelAllAnimator();
                this.mPinchMode = 1;
                this.mLastMovePoint.set(event.getX(), event.getY());
            }
        } else if (action == 5) {
            cancelAllAnimator();
            this.mPinchMode = 2;
            saveScaleContext(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
        } else if (action == 2 && (this.mScaleAnimator == null || !this.mScaleAnimator.isRunning())) {
            if (this.mPinchMode == 1) {
                scrollBy(event.getX() - this.mLastMovePoint.x, event.getY() - this.mLastMovePoint.y);
                this.mLastMovePoint.set(event.getX(), event.getY());
            } else if (this.mPinchMode == 2 && event.getPointerCount() > 1) {
                float distance = MathUtils.getDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                float[] lineCenter = MathUtils.getCenterPoint(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                this.mLastMovePoint.set(lineCenter[0], lineCenter[1]);
                scale(this.mScaleCenter, this.mScaleBase, distance, this.mLastMovePoint);
            }
        }
        this.mGestureDetector.onTouchEvent(event);
        return true;
    }

    private boolean scrollBy(float xDiff, float yDiff) {
        if (!isReady()) {
            return false;
        }
        RectF bound = MathUtils.rectFTake();
        getImageBound(bound);
        float displayWidth = (float) getWidth();
        float displayHeight = (float) getHeight();
        if (bound.right - bound.left < displayWidth) {
            xDiff = 0.0f;
        } else if (bound.left + xDiff > 0.0f) {
            if (bound.left < 0.0f) {
                xDiff = -bound.left;
            } else {
                xDiff = 0.0f;
            }
        } else if (bound.right + xDiff < displayWidth) {
            if (bound.right > displayWidth) {
                xDiff = displayWidth - bound.right;
            } else {
                xDiff = 0.0f;
            }
        }
        if (bound.bottom - bound.top < displayHeight) {
            yDiff = 0.0f;
        } else if (bound.top + yDiff > 0.0f) {
            if (bound.top < 0.0f) {
                yDiff = -bound.top;
            } else {
                yDiff = 0.0f;
            }
        } else if (bound.bottom + yDiff < displayHeight) {
            if (bound.bottom > displayHeight) {
                yDiff = displayHeight - bound.bottom;
            } else {
                yDiff = 0.0f;
            }
        }
        MathUtils.rectFGiven(bound);
        this.mOuterMatrix.postTranslate(xDiff, yDiff);
        dispatchOuterMatrixChanged();
        invalidate();
        if (xDiff == 0.0f && yDiff == 0.0f) {
            return false;
        }
        return true;
    }

    private void saveScaleContext(float x1, float y1, float x2, float y2) {
        this.mScaleBase = MathUtils.getMatrixScale(this.mOuterMatrix)[0] / MathUtils.getDistance(x1, y1, x2, y2);
        float[] center = MathUtils.inverseMatrixPoint(MathUtils.getCenterPoint(x1, y1, x2, y2), this.mOuterMatrix);
        this.mScaleCenter.set(center[0], center[1]);
    }

    private void scale(PointF scaleCenter, float scaleBase, float distance, PointF lineCenter) {
        if (isReady()) {
            float scale = scaleBase * distance;
            Matrix matrix = MathUtils.matrixTake();
            matrix.postScale(scale, scale, scaleCenter.x, scaleCenter.y);
            matrix.postTranslate(lineCenter.x - scaleCenter.x, lineCenter.y - scaleCenter.y);
            this.mOuterMatrix.set(matrix);
            MathUtils.matrixGiven(matrix);
            dispatchOuterMatrixChanged();
            invalidate();
        }
    }

    private void doubleTap(float x, float y) {
        if (isReady()) {
            Matrix innerMatrix = MathUtils.matrixTake();
            getInnerMatrix(innerMatrix);
            float innerScale = MathUtils.getMatrixScale(innerMatrix)[0];
            float outerScale = MathUtils.getMatrixScale(this.mOuterMatrix)[0];
            float currentScale = innerScale * outerScale;
            float displayWidth = (float) getWidth();
            float displayHeight = (float) getHeight();
            float maxScale = getMaxScale();
            float nextScale = calculateNextScale(innerScale, outerScale);
            if (nextScale > maxScale) {
                nextScale = maxScale;
            }
            if (nextScale < innerScale) {
                nextScale = innerScale;
            }
            Matrix animEnd = MathUtils.matrixTake(this.mOuterMatrix);
            animEnd.postScale(nextScale / currentScale, nextScale / currentScale, x, y);
            animEnd.postTranslate((displayWidth / 2.0f) - x, (displayHeight / 2.0f) - y);
            Matrix testMatrix = MathUtils.matrixTake(innerMatrix);
            testMatrix.postConcat(animEnd);
            RectF testBound = MathUtils.rectFTake(0.0f, 0.0f, (float) getDrawable().getIntrinsicWidth(), (float) getDrawable().getIntrinsicHeight());
            testMatrix.mapRect(testBound);
            float postX = 0.0f;
            float postY = 0.0f;
            if (testBound.right - testBound.left < displayWidth) {
                postX = (displayWidth / 2.0f) - ((testBound.right + testBound.left) / 2.0f);
            } else if (testBound.left > 0.0f) {
                postX = -testBound.left;
            } else if (testBound.right < displayWidth) {
                postX = displayWidth - testBound.right;
            }
            if (testBound.bottom - testBound.top < displayHeight) {
                postY = (displayHeight / 2.0f) - ((testBound.bottom + testBound.top) / 2.0f);
            } else if (testBound.top > 0.0f) {
                postY = -testBound.top;
            } else if (testBound.bottom < displayHeight) {
                postY = displayHeight - testBound.bottom;
            }
            animEnd.postTranslate(postX, postY);
            cancelAllAnimator();
            this.mScaleAnimator = new ScaleAnimator(this, this.mOuterMatrix, animEnd);
            this.mScaleAnimator.start();
            MathUtils.rectFGiven(testBound);
            MathUtils.matrixGiven(testMatrix);
            MathUtils.matrixGiven(animEnd);
            MathUtils.matrixGiven(innerMatrix);
        }
    }

    private void scaleEnd() {
        if (isReady()) {
            boolean change = false;
            Matrix currentMatrix = MathUtils.matrixTake();
            getCurrentImageMatrix(currentMatrix);
            float currentScale = MathUtils.getMatrixScale(currentMatrix)[0];
            float outerScale = MathUtils.getMatrixScale(this.mOuterMatrix)[0];
            float displayWidth = (float) getWidth();
            float displayHeight = (float) getHeight();
            float maxScale = getMaxScale();
            float scalePost = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
            float postX = 0.0f;
            float postY = 0.0f;
            if (currentScale > maxScale) {
                scalePost = maxScale / currentScale;
            }
            if (outerScale * scalePost < DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
                scalePost = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT / outerScale;
            }
            if (scalePost != DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
                change = true;
            }
            Matrix testMatrix = MathUtils.matrixTake(currentMatrix);
            testMatrix.postScale(scalePost, scalePost, this.mLastMovePoint.x, this.mLastMovePoint.y);
            RectF testBound = MathUtils.rectFTake(0.0f, 0.0f, (float) getDrawable().getIntrinsicWidth(), (float) getDrawable().getIntrinsicHeight());
            testMatrix.mapRect(testBound);
            if (testBound.right - testBound.left < displayWidth) {
                postX = (displayWidth / 2.0f) - ((testBound.right + testBound.left) / 2.0f);
            } else if (testBound.left > 0.0f) {
                postX = -testBound.left;
            } else if (testBound.right < displayWidth) {
                postX = displayWidth - testBound.right;
            }
            if (testBound.bottom - testBound.top < displayHeight) {
                postY = (displayHeight / 2.0f) - ((testBound.bottom + testBound.top) / 2.0f);
            } else if (testBound.top > 0.0f) {
                postY = -testBound.top;
            } else if (testBound.bottom < displayHeight) {
                postY = displayHeight - testBound.bottom;
            }
            if (!(postX == 0.0f && postY == 0.0f)) {
                change = true;
            }
            if (change) {
                Matrix animEnd = MathUtils.matrixTake(this.mOuterMatrix);
                animEnd.postScale(scalePost, scalePost, this.mLastMovePoint.x, this.mLastMovePoint.y);
                animEnd.postTranslate(postX, postY);
                cancelAllAnimator();
                this.mScaleAnimator = new ScaleAnimator(this, this.mOuterMatrix, animEnd);
                this.mScaleAnimator.start();
                MathUtils.matrixGiven(animEnd);
            }
            MathUtils.rectFGiven(testBound);
            MathUtils.matrixGiven(testMatrix);
            MathUtils.matrixGiven(currentMatrix);
        }
    }

    private void fling(float vx, float vy) {
        if (isReady()) {
            cancelAllAnimator();
            this.mFlingAnimator = new FlingAnimator(vx / 60.0f, vy / 60.0f);
            this.mFlingAnimator.start();
        }
    }

    private void cancelAllAnimator() {
        if (this.mScaleAnimator != null) {
            this.mScaleAnimator.cancel();
            this.mScaleAnimator = null;
        }
        if (this.mFlingAnimator != null) {
            this.mFlingAnimator.cancel();
            this.mFlingAnimator = null;
        }
    }
}
