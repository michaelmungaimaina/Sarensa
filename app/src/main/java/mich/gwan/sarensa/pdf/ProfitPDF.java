package mich.gwan.sarensa.pdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import mich.gwan.sarensa.R;

public class ProfitPDF {

    private static final String TAG   = ProfitPDF.class.getSimpleName();
    private static final Font FONT_TITLE    = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLUE.darker());
    private static final Font FONT_SUBTITLE = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.NORMAL);
    private static final Font FONT_SIDE_TEXT = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
    private static final Font FONT_LOGO_TEXT = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLDITALIC);
    private static final Font FONT_SIGN = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
    private static final Font FONT_DESIGN = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC);
    private static final Font FONT_MID_TEXT = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLUE.darker());

    private static final Font FONT_CELL     = new Font(Font.FontFamily.TIMES_ROMAN,  12, Font.NORMAL);
    private static final Font FONT_LAST_ROW     = new Font(Font.FontFamily.TIMES_ROMAN,  12, Font.BOLD, BaseColor.BLUE.darker());
    private static final Font FONT_COLUMN   = new Font(Font.FontFamily.TIMES_ROMAN,  13, Font.NORMAL, BaseColor.WHITE);

    public interface OnDocClose
    {
        void onPDFClose(File file);
    }

    /**
     * Method for creating pdf
     * @param mContext the context of the activity
     * @param mCallback a callback when the document is closed
     * @param items the items to show on the pdf
     * @param filePath the path to the storage directory
     * @param isPortrait a boolean for page orientation
     * @throws Exception if document is not created
     */
    public static void createPdf(@NonNull Context mContext, OnDocClose mCallback, String station, List<String[]> items, @NonNull String filePath, boolean isPortrait) throws Exception
    {
        if(filePath.equals(""))
        {
            throw new NullPointerException("PDF File Name can't be null or blank. PDF File can't be created");
        }

        File file = new File(filePath);
        if (!file.exists())
        {
            // Make it, if it doesn't exit
            boolean success = file.createNewFile();
            if (!success)
            {
                file = null;
            }
        } else {
            file.delete();
            Thread.sleep(30);
        }

        Document document = new Document();
        document.setMargins(24f,24f,32f,52f);
        document.setPageSize(isPortrait? PageSize.A4:PageSize.A4.rotate());

        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        pdfWriter.setFullCompression();
        pdfWriter.setPageEvent(new PageNumeration(mContext));

        document.open();

        setMetaData(document);

        addHeader(mContext,document);
        createTopDataTable(document, station);
        addEmptyLine(document, 0.5);
        document.add(createDataTable(items));
        addEmptyLine(document,4);
        addManagerSign(mContext,document);
        addEmptyLine(document,2);
        document.close();

        try
        {
            pdfWriter.close();
        }
        catch (Exception ex)
        {
            Log.e(TAG,"Error While Closing pdfWriter : "+ex);
        }

        if(mCallback!=null)
        {
            mCallback.onPDFClose(file);
        }
    }

    /**
     * Method for creating empty lines iteratively
     * @param document
     * @param number
     * @throws DocumentException
     */
    private static  void addEmptyLine(Document document, double number) throws DocumentException
    {
        for (double i = 0; i < number; i++)
        {
            document.add(new Paragraph(" "));
        }
    }

    /**
     * method that sets the metedata of the document
     * @param document
     */
    private static void setMetaData(Document document)
    {
        document.addCreationDate();
        document.addAuthor( "Sarensa Enterprises");
        document.addCreator("Sarensa App");
        document.addHeader("Sarensa","Sarensa App");
    }

    /**
     * Method for creating the header file of the document
     * adds title and enterprise's logo
     * @param mContext
     * @param document
     * @throws Exception
     */
    private static void addHeader(Context mContext, Document document) throws Exception
    {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1});
        headerTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        headerTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        headerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2,7,2});
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell;
        Drawable d;
        ByteArrayOutputStream stream;
        Bitmap bmp;
        {

            //create a PdfTable with one column
            PdfPTable logoTable=new PdfPTable(1);
            logoTable.setWidthPercentage(100);
            logoTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            logoTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);

            /*LEFT TOP LOGO*/
            //create the first cell inside your table
            cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setUseAscender(true);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(2f);
            logoTable.addCell(cell);

            //create the second cell

            //Add the created table as a cell in your outer table
            cell = new PdfPCell(logoTable);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setUseAscender(true);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(2f);
            table.addCell(cell);
        }

        {
            /*MIDDLE TEXT
            * Create the cell with some paragraphs
            * */
            d = ContextCompat.getDrawable(mContext, R.drawable.header);
            assert d != null;
            bmp = ((BitmapDrawable) d).getBitmap();
            stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG,100,stream);

            Image logo=Image.getInstance(stream.toByteArray());
            logo.setWidthPercentage(100);
            logo.scaleToFit(314,131);

            cell = new PdfPCell(logo);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(1f);
            cell.setUseAscender(true);

            table.addCell(cell);
        }
        /* RIGHT TOP LOGO*/
        {
            //create a table inside the cell
            PdfPTable logoTable=new PdfPTable(1);
            logoTable.setWidthPercentage(100);
            logoTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            logoTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            //Initialize the logo drawable

            //Add the logo into the cell
            PdfPCell logoCell = new PdfPCell();
            logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            logoCell.setBorder(PdfPCell.NO_BORDER);
            logoTable.addCell(logoCell);

            //create a second cell

            //add the table into a cell
            cell = new PdfPCell(logoTable);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setUseAscender(true);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(2f);
            table.addCell(cell);
        }
        //image for the cell
        /*TOP LOGO*/
        d = ContextCompat.getDrawable(mContext, R.drawable.sarensa_top);
        assert d != null;
        bmp = ((BitmapDrawable) d).getBitmap();
        stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,stream);

        Image top_logo=Image.getInstance(stream.toByteArray());
        top_logo.setWidthPercentage(100);
        top_logo.scaleToFit(548,48);
        //create header cell with an image
        cell = new PdfPCell(top_logo);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setUseAscender(true);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPadding(0f);
        headerTable.addCell(cell);

        cell = new PdfPCell(table);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setUseAscender(true);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPadding(2f);
        headerTable.addCell(cell);

        document.add(headerTable);
    }

    /**
     * Method to display the header text
     * @param doc
     * @throws DocumentException
     */
    @NonNull
    private static void createTopDataTable(Document doc, String station) throws DocumentException {
        PdfPTable table1 = new PdfPTable(3);
        table1.setWidthPercentage(100);
        table1.setWidths(new float[]{1f, 2f, 1f});
        table1.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell;
        {
            cell = new PdfPCell(new Phrase("   \n", FONT_SIDE_TEXT));
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setUseBorderPadding(true);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBorderWidthTop(2f);
            cell.setBorderWidthBottom(2f);
            cell.setBorderColorBottom(BaseColor.BLUE.darker());
            cell.setBorderColorTop(BaseColor.BLUE.darker());
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setPadding(3f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(station + " PROFIT REPORT", FONT_MID_TEXT));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setUseBorderPadding(true);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBorderWidthTop(2f);
            cell.setBorderWidthBottom(2f);
            cell.setBorderColorBottom(BaseColor.BLUE.darker());
            cell.setBorderColorTop(BaseColor.BLUE.darker());
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setPadding(3f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("Print On: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), FONT_SIDE_TEXT));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setUseBorderPadding(true);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setBorderWidthTop(2f);
            cell.setBorderWidthBottom(2f);
            cell.setBorderColorBottom(BaseColor.BLUE.darker());
            cell.setBorderColorTop(BaseColor.BLUE.darker());
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setPadding(3f);
            table1.addCell(cell);
        }
        doc.add(table1);
    }

    /**
     * Method for adding the managers Signature
     * @param mContext
     * @param document
     * @throws Exception
     */
    private static void addManagerSign(Context mContext, Document document) throws Exception {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 2, 2});
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell;
        {
            //CREATE the first column content
            cell = new PdfPCell(new Phrase("", FONT_SIDE_TEXT));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setPadding(3f);
            table.addCell(cell);

            //create the second column
            cell = new PdfPCell(new Phrase("", FONT_MID_TEXT));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setPadding(3f);
            table.addCell(cell);

            //create the third column
            PdfPTable signTable=new PdfPTable(1);
            signTable.setWidthPercentage(100);
            signTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            signTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            signTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
            /*Manager Sign*/
            Drawable d = ContextCompat.getDrawable(mContext, R.drawable.sign);
            assert d != null;
            Bitmap bmp = ((BitmapDrawable) d).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

            Image logo = Image.getInstance(stream.toByteArray());
            logo.setWidthPercentage(80);
            logo.scaleToFit(105, 65);

            PdfPCell cel = new PdfPCell(logo);
            cel.setHorizontalAlignment(Element.ALIGN_CENTER);
            cel.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setUseBorderPadding(true);
            cel.setUseAscender(true);
            cel.setBorder(PdfPCell.BOTTOM);
            cell.setBorderWidthTop(1f);
            cell.setBorderColorBottom(BaseColor.BLUE.darker());
            cel.setPadding(2f);
            signTable.addCell(cel);

            cel = new PdfPCell();
            cel.setHorizontalAlignment(Element.ALIGN_CENTER);
            cel.setBorder(PdfPCell.NO_BORDER);
            cel.setPadding(8f);
            cel.setUseAscender(true);

            Paragraph temp = new Paragraph("MR. JOHN THUO", FONT_SIGN);
            temp.setAlignment(Element.ALIGN_CENTER);
            cel.addElement(temp);

            temp = new Paragraph("General Manager - Sarensa Enterprises", FONT_DESIGN);
            temp.setAlignment(Element.ALIGN_CENTER);
            cel.addElement(temp);

            signTable.addCell(cel);

            //Create the table as a cell now
            cell = new PdfPCell(signTable);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setUseAscender(true);
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setPadding(2f);
            table.addCell(cell);
        }

        {
            table.addCell(cell);
        }
        document.add(table);
    }

    /**
     * Method to create the
     * @param dataTable
     * @return
     * @throws DocumentException
     */
    private static PdfPTable createDataTable(List<String[]> dataTable) throws DocumentException
    {
        PdfPTable table1 = new PdfPTable(2);
        table1.setWidthPercentage(100);
        table1.setWidths(new float[]{1.5f,1f});
        table1.setHeaderRows(1);
        table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        //create the header columns
        PdfPCell cell;
        {
            cell = new PdfPCell(new Phrase("ITEM NAME", FONT_COLUMN));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(BaseColor.BLUE.darker());
            cell.setPadding(3f);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase("PROFIT", FONT_COLUMN));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setBackgroundColor(BaseColor.BLUE.darker());
            cell.setPadding(3f);
            table1.addCell(cell);
        }

        /**
         * create data columns
         * define their alternating color
         * define the dataset
         */
        float top_bottom_Padding = 5f;
        float left_right_Padding = 1f;
        boolean alternate = false;

        BaseColor lt_gray = new BaseColor(231,232,246); //#DDDDDD
        BaseColor cell_color;

        int size = dataTable.size();
        String[] temp;
        for (int i = 0; i < size - 1; i++)
        {
            cell_color = alternate ? lt_gray : BaseColor.WHITE;
            temp = dataTable.get(i);

            cell = new PdfPCell(new Phrase(temp[0], FONT_CELL));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPaddingLeft(90f);
            cell.setPaddingRight(left_right_Padding);
            cell.setPaddingTop(top_bottom_Padding);
            cell.setPaddingBottom(top_bottom_Padding);
            cell.setBackgroundColor(cell_color);
            table1.addCell(cell);

            cell = new PdfPCell(new Phrase(temp[1]+"0", FONT_CELL));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setPaddingLeft(left_right_Padding);
            cell.setPaddingRight(90f);
            cell.setPaddingTop(top_bottom_Padding);
            cell.setPaddingBottom(top_bottom_Padding);
            cell.setBackgroundColor(cell_color);
            table1.addCell(cell);

            alternate = !alternate;
        }

        String [] m = dataTable.get(size-1);
        cell = new PdfPCell(new Phrase(m[0], FONT_LAST_ROW));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setUseBorderPadding(true);
        cell.setBorder(Rectangle.TOP);
        cell.setBorderWidthTop(2f);
        cell.setBorderColorTop(BaseColor.BLUE.darker());
        cell.setPaddingLeft(90f);
        cell.setPaddingRight(left_right_Padding);
        cell.setPaddingTop(top_bottom_Padding);
        cell.setPaddingBottom(top_bottom_Padding);
        cell.setBackgroundColor(BaseColor.WHITE);
        table1.addCell(cell);

        cell = new PdfPCell(new Phrase(m[1], FONT_LAST_ROW));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setUseBorderPadding(true);
        cell.setBorder(Rectangle.TOP);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderWidthTop(2f);
        cell.setBorderColorTop(BaseColor.BLUE.darker());
        cell.setBorderColorBottom(BaseColor.BLUE.darker());
        cell.setPaddingLeft(left_right_Padding);
        cell.setPaddingRight(90f);
        cell.setPaddingTop(top_bottom_Padding);
        cell.setPaddingBottom(top_bottom_Padding);
        cell.setBackgroundColor(BaseColor.WHITE);
        table1.addCell(cell);

        return table1;
    }

}
