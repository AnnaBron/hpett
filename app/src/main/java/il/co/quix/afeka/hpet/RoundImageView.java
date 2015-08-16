

package il.co.quix.afeka.hpet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by anya on 8/16/2015.
 */
public class RoundImageView extends ImageView {

    public RoundImageView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {

        Drawable drawable = getDrawable();

        if (drawable == null)
            return;

        if (getWidth() == 0 || getHeight() == 0)
            return;

        Bitmap bit = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = bit.copy(Bitmap.Config.ARGB_8888, true);

        int width = getWidth();
        int height = getHeight();

        Bitmap roundBitmap = getRoundedCroppedBitmap(bitmap, width);
        canvas.drawBitmap(roundBitmap, 0, 0, null);
    }

    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius)
    {

        Bitmap resBitmap;

        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            resBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        else
            resBitmap = bitmap;

        Bitmap output = Bitmap.createBitmap(resBitmap.getWidth(), resBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect  = new Rect(0,0,resBitmap.getWidth(),resBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0,0,0,0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(resBitmap.getWidth()/2 + 0.7f, resBitmap.getHeight() / 2 + 0.7f, resBitmap.getWidth() / 2 + 0.1f , paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(resBitmap,rect,rect,paint);

        return output;

    }
}


