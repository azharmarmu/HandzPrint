package com.marmu.handprint.sales_man.landing.activity.billing.view_delete_edit;

import android.os.Environment;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.HashMap;

/**
 * Created by azharuddin on 3/7/17.
 */

@SuppressWarnings("unchecked")
class PDF {
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    PDF(HashMap<String, Object> productDetails) {
        try {
            Document document = new Document();
            String val = String.valueOf(System.currentTimeMillis());
            String FILE = Environment.getExternalStorageDirectory() + "/hP_" + val + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();
            addContent(document, productDetails);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addContent(Document document, HashMap<String, Object> productDetails) throws DocumentException {

        Anchor anchor = new Anchor("Estimate", catFont);
        anchor.setName("Estimate");
        Paragraph title = new Paragraph(anchor);
        title.setAlignment(Element.ALIGN_CENTER);
        Chapter catPart = new Chapter(title, 1);
        addEmptyLine(new Paragraph(), 1);

        Paragraph storeName = new Paragraph("MUBARAK TRADERS", subFont);
        storeName.setAlignment(Element.ALIGN_CENTER);
        Section subCatPart = catPart.addSection(storeName);
        subCatPart.add(storeName);
        addEmptyLine(new Paragraph(), 1);

        Paragraph storeNumber = new Paragraph("9449802606", smallBold);
        storeNumber.setAlignment(Element.ALIGN_CENTER);
        subCatPart = catPart.addSection(storeNumber);
        subCatPart.add(storeNumber);
        addEmptyLine(new Paragraph(), 1);

        createTable(productDetails, subCatPart);

        document.add(catPart);
    }

    private static void createTable(HashMap<String, Object> productDetails, Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(5);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell cell = new PdfPCell(new Phrase("S1"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Items"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Price"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("QTY"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Total"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        table.setHeaderRows(1);
        int i = 1;
        for (String prodKey : productDetails.keySet()) {
            HashMap<String, Object> localProdDetails = (HashMap<String, Object>) productDetails.get(prodKey);
            table.addCell(String.valueOf(i));
            table.addCell(localProdDetails.get("prod_name").toString());
            table.addCell(localProdDetails.get("prod_price").toString());
            table.addCell(localProdDetails.get("prod_qty").toString());
            table.addCell(localProdDetails.get("prod_sub_total").toString());
            i++;
        }

        subCatPart.add(table);

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
