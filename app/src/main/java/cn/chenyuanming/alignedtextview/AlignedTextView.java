package cn.chenyuanming.alignedtextview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by chenyuanming on 9/19/16.
 */

public class AlignedTextView extends TextView {

    public AlignedTextView(Context context) {
        super(context);
    }

    public AlignedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlignedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        String text = getText().toString();
        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        paint.setColor(getCurrentTextColor());
        float xOffset = 0;
        int yOffset = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
        float canvasLength = getMeasuredWidth();
        float textLength = paint.measureText(text);
        float lineSpacing = 0;
        if (!TextUtils.isEmpty(getText()) && getText().length() > 1) {
            lineSpacing = (canvasLength - textLength) / (text.length() - 1) / paint.getTextSize();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            canvas.translate(-lineSpacing * getTextSize() / 2, 0);
            paint.setLetterSpacing(lineSpacing);  // setLetterSpacing is only available from LOLLIPOP and on
            canvas.drawText(text, xOffset, yOffset, paint);
        } else {
            float spacePercentage = lineSpacing;
            drawKernedText(canvas, text, xOffset, yOffset, paint, spacePercentage);
        }

    }

    /**
     * Draw kerned text by drawing the text string character by character with a space in between.
     * Return the width of the text.
     * If canvas is null, the text won't be drawn, but the width will still be returned/
     * kernPercentage determines the space between each letter. If it's 0, there will be no space between letters.
     * Otherwise, there will be space between each letter. The  value is a fraction of the width of a blank space.
     */
    private int drawKernedText(Canvas canvas, String text, float xOffset, float yOffset, Paint paint, float kernPercentage) {
        Rect textRect = new Rect();
        int width = 0;
        int space = Math.round(paint.measureText(" ") * kernPercentage);
//        canvas.translate(paint.measureText(" "),0);
        for (int i = 0; i < text.length(); i++) {
            if (canvas != null) {
                String s = String.valueOf(text.charAt(i));
                canvas.drawText(s, xOffset, yOffset, paint);
                canvas.translate(paint.measureText(s),0);
                canvas.translate(getTextSize()*kernPercentage,0);
            }
//            int charWidth;
//            if (text.charAt(i) == ' ') {
//                charWidth = Math.round(paint.measureText(String.valueOf(text.charAt(i)))) + space;
//            } else {
//                paint.getTextBounds(text, i, i + 1, textRect);
//                charWidth = textRect.width() + space;
//            }
//            xOffset += charWidth;
//            width += charWidth;
        }
        return width;
    }

}
