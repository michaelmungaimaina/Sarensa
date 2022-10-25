package mich.gwan.sarensa.pdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import mich.gwan.sarensa.BuildConfig;
import mich.gwan.sarensa.R;
import mich.gwan.sarensa.ui.sale.SaleFragment;

final class PageNumeration extends PdfPageEventHelper {
    private Context context;
    private static String TAG        = PageNumeration.class.getSimpleName();
    private static Font FONT_FOOTER  = new Font(Font.FontFamily.TIMES_ROMAN,  10, Font.NORMAL, BaseColor.DARK_GRAY);
    private static Font FONT_MID_FOOTER  = new Font(Font.FontFamily.TIMES_ROMAN,  8, Font.NORMAL, BaseColor.BLUE.darker());

    PageNumeration(Context context){
        this.context = context;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document){

        /*TOP FOOTER LOGO*/
        Drawable d= ContextCompat.getDrawable(context, R.drawable.footer_sub_logo);
        assert d != null;
        Bitmap bmp=((BitmapDrawable) d).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,stream);
        Image logo= null;
        try {
            logo = Image.getInstance(stream.toByteArray());
        } catch (BadElementException | IOException e) {
            e.printStackTrace();
        }
        assert logo != null;
        logo.setWidthPercentage(100);
        logo.scaleToFit(547,25);


        /* Background image */
        Drawable drawable= ContextCompat.getDrawable(context, R.drawable.footer_note_logo);
        assert drawable != null;
        Bitmap bm=((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        Image bgImage= null;
        try {
            bgImage = Image.getInstance(byteArrayOutputStream.toByteArray());
        } catch (BadElementException | IOException e) {
            e.printStackTrace();
        }
        assert bgImage != null;
        bgImage.setWidthPercentage(100);
        bgImage.scaleToFit(549,25);
        /* middle text  */
        Drawable drawabl= ContextCompat.getDrawable(context, R.drawable.midd_text);
        assert drawabl != null;
        Bitmap bitMap=((BitmapDrawable) drawabl).getBitmap();
        ByteArrayOutputStream byteArray=new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.PNG,100,byteArray);
        Image middleImage= null;
        try {
            middleImage = Image.getInstance(byteArray.toByteArray());
        } catch (BadElementException | IOException e) {
            e.printStackTrace();
        }
        assert middleImage != null;
        middleImage.setWidthPercentage(100);
        middleImage.scaleToFit(549,25);

        try {
            //create a PdfTable with one column
            PdfPTable logoTable=new PdfPTable(1);
            logoTable.setWidthPercentage(100);
            logoTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            logoTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell;
            // top footer logo
            cell = new PdfPCell(logo);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setUseAscender(true);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(0f);
            logoTable.addCell(cell);
            // footer data
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1,2,1});

            //1st Column
            Anchor anchor = new Anchor(new Phrase("Sarensa", FONT_FOOTER));
            anchor.setReference( "http://link_to_playstore/");
            cell = new PdfPCell(anchor);

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            cell.setPadding(4f);
            table.addCell(cell);
            table.setTotalWidth(document.getPageSize().getWidth()-document.leftMargin()-document.rightMargin());
            table.writeSelectedRows(0,-1,document.leftMargin(),document.bottomMargin()-5,writer.getDirectContent());

            //2nd Column
            cell = new PdfPCell(middleImage);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setPadding(0f);
            table.addCell(cell);

            //3rd column
            cell = new PdfPCell(new Phrase("Page - ".concat(String.valueOf(writer.getPageNumber())), FONT_FOOTER));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            cell.setPadding(4f);
            table.addCell(cell);

            PdfPCell cel = new PdfPCell(table);
            cel.setHorizontalAlignment(Element.ALIGN_CENTER);
            cel.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cel.setUseAscender(true);
            cel.setCellEvent(new ImageBackgroundEvent(bgImage));
            cel.setBorder(PdfPCell.NO_BORDER);
            cel.setPadding(0f);
            logoTable.addCell(cel);
            logoTable.setTotalWidth(document.getPageSize().getWidth()-document.leftMargin()-document.rightMargin());
            logoTable.writeSelectedRows(0,-1,document.leftMargin(),document.bottomMargin()-5,writer.getDirectContent());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e(TAG,ex.toString());
        }
    }
}
