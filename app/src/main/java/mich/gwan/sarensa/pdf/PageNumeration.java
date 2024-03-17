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
    private static String TAG = PageNumeration.class.getSimpleName();
    private static Font FONT_FOOTER = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, new BaseColor(0,0,0,200));
    private static Font FONT_FOOTER1 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, new BaseColor(0,0,0,0));

    PageNumeration(Context context) {
        this.context = context;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {

        /* Background image */
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.footer);
        assert drawable != null;
        Bitmap bm = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        Image bgImage = null;
        try {
            bgImage = Image.getInstance(byteArrayOutputStream.toByteArray());
        } catch (BadElementException | IOException e) {
            e.printStackTrace();
        }
        assert bgImage != null;
        bgImage.setWidthPercentage(100);
        bgImage.scaleToFit(549, 25);

        try {
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);

            PdfPCell cell;

            // Anchor Texts
            PdfPTable innerTable = new PdfPTable(2);
            innerTable.setWidthPercentage(100);
            innerTable.setWidths(new float[]{1, 1});

            // Left Anchor Text top
            Anchor anchorLeft = new Anchor(new Phrase("qwerty", FONT_FOOTER1));
            anchorLeft.setReference("http://link_to_left_text/");
            PdfPCell leftCell = new PdfPCell(anchorLeft);
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.setPadding(4f);
            innerTable.addCell(leftCell);

            // Right Anchor Text top
            Anchor anchorRight = new Anchor(new Phrase("qwerty", FONT_FOOTER1));
            anchorRight.setReference("http://link_to_right_text/");
            PdfPCell rightCell = new PdfPCell(anchorRight);
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.setPadding(4f);
            innerTable.addCell(rightCell);

            // Left Anchor Text middle
            Anchor anchorLef = new Anchor(new Phrase("Sarensa", FONT_FOOTER));
            anchorLeft.setReference("http://link_to_left_text/");
            PdfPCell leftCel = new PdfPCell(anchorLef);
            leftCel.setBorder(Rectangle.NO_BORDER);
            leftCel.setPadding(4f);
            innerTable.addCell(leftCel);

            // Right Anchor Text middle
            Anchor anchorRigh = new Anchor(new Phrase("Page - ".concat(String.valueOf(writer.getPageNumber())), FONT_FOOTER));
            anchorRigh.setReference("http://link_to_right_text/");
            PdfPCell rightCel = new PdfPCell(anchorRigh);
            rightCel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightCel.setBorder(Rectangle.NO_BORDER);
            rightCel.setPadding(4f);
            innerTable.addCell(rightCel);

            // Left Anchor Text bottom
            Anchor anchorLe = new Anchor(new Phrase("qwerty", FONT_FOOTER1));
            anchorLef.setReference("http://link_to_left_text/");
            PdfPCell leftCe = new PdfPCell(anchorLe);
            leftCe.setBorder(Rectangle.NO_BORDER);
            leftCe.setPadding(4f);
            innerTable.addCell(leftCe);

            // Right Anchor Text bottom
            Anchor anchorRig = new Anchor(new Phrase("qwerty", FONT_FOOTER1));
            anchorRig.setReference("http://link_to_right_text/");
            PdfPCell rightCe = new PdfPCell(anchorRig);
            rightCe.setHorizontalAlignment(Element.ALIGN_RIGHT);
            rightCe.setBorder(Rectangle.NO_BORDER);
            rightCe.setPadding(4f);
            innerTable.addCell(rightCe);

            cell = new PdfPCell(innerTable);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPadding(0f);
            cell.setCellEvent(new ImageBackgroundEvent(bgImage));
            table.addCell(cell);

            table.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
            table.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin() - -5, writer.getDirectContent());
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.toString());
        }
    }
}
