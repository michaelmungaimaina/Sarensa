package mich.gwan.sarensa.pdf;

import android.util.Log;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

final class PageNumeration extends PdfPageEventHelper {
    private static String TAG        = PageNumeration.class.getSimpleName();
    private static Font FONT_FOOTER  = new Font(Font.FontFamily.TIMES_ROMAN,  8, Font.NORMAL, BaseColor.DARK_GRAY);
    private static Font FONT_MID_FOOTER  = new Font(Font.FontFamily.TIMES_ROMAN,  8, Font.NORMAL, BaseColor.BLUE.darker());

    PageNumeration()
    {

    }

    @Override
    public void onEndPage(PdfWriter writer, Document document)
    {
        try
        {
            PdfPCell cell;
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1,1,1});

            //1st Column
            Anchor anchor = new Anchor(new Phrase("Generated and processed by Sarensa App", FONT_FOOTER));
            anchor.setReference( "http://link_to_playstore/");
            cell = new PdfPCell(anchor);

            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(0);
            cell.setPadding(2f);
            table.addCell(cell);
            table.setTotalWidth(document.getPageSize().getWidth()-document.leftMargin()-document.rightMargin());
            table.writeSelectedRows(0,-1,document.leftMargin(),document.bottomMargin()-5,writer.getDirectContent());

            //2nd Column
            cell = new PdfPCell(new Phrase("Sarensa Enterprises Limited", FONT_MID_FOOTER));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setPadding(3f);
            table.addCell(cell);

            //3rd column
            cell = new PdfPCell(new Phrase("Page - ".concat(String.valueOf(writer.getPageNumber())), FONT_FOOTER));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBorder(0);
            cell.setPadding(2f);
            table.addCell(cell);
            table.setTotalWidth(document.getPageSize().getWidth()-document.leftMargin()-document.rightMargin());
            table.writeSelectedRows(0,-1,document.leftMargin(),document.bottomMargin()-5,writer.getDirectContent());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.e(TAG,ex.toString());
        }
    }
}
