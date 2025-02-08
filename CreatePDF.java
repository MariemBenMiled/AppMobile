package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;

public class CreatePDF {

    public void createSchedulePDF(Context context) {
        try {

            String filePath = context.getExternalFilesDir(null) + "/schedule.pdf";


            PdfWriter writer = new PdfWriter(filePath);


            PdfDocument pdf = new PdfDocument(writer);


            Document document = new Document(pdf);


            document.add(new Paragraph("Informatics Timetable"));
            document.add(new Paragraph("--------------------------------------------------"));


            float[] columnWidths = {1, 1, 1, 1, 1, 1};
            Table table = new Table(columnWidths);


            table.addCell("Time");
            table.addCell("Monday");
            table.addCell("Tuesday");
            table.addCell("Wednesday");
            table.addCell("Thursday");
            table.addCell("Friday");


            addOrganizedScheduleRow(table, "08:00-09:00", "Algorithms", "Networking", "Algorithms", "Networking", "Algorithms");
            addOrganizedScheduleRow(table, "09:00-10:00", "Data Structures", "Databases", "Data Structures", "Databases", "Data Structures");
            addOrganizedScheduleRow(table, "10:00-11:00", "Break", "Break", "Break", "Break", "Break");
            addOrganizedScheduleRow(table, "11:00-12:00", "Operating Systems", "Programming", "Operating Systems", "Programming", "Operating Systems");
            addOrganizedScheduleRow(table, "12:00-13:00", "Lunch Break", "Lunch Break", "Lunch Break", "Lunch Break", "Lunch Break");
            addOrganizedScheduleRow(table, "13:00-14:00", "Machine Learning", "Web Development", "Machine Learning", "Web Development", "Machine Learning");
            addOrganizedScheduleRow(table, "14:00-15:00", "Cybersecurity", "Software Engineering", "Cybersecurity", "Software Engineering", "Cybersecurity");


            document.add(table);


            document.close();


            Toast.makeText(context, "PDF Created Successfully at: " + filePath, Toast.LENGTH_LONG).show();


            openPDF(context, filePath);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error creating PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void addOrganizedScheduleRow(Table table, String time, String monday, String tuesday, String wednesday, String thursday, String friday) {

        table.addCell("Time: " + time);
        table.addCell("Subject: " + monday);
        table.addCell("Subject: " + tuesday);
        table.addCell("Subject: " + wednesday);
        table.addCell("Subject: " + thursday);
        table.addCell("Subject: " + friday);
    }

    private void openPDF(Context context, String filePath) {
        File pdfFile = new File(filePath);
        Uri pdfUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", pdfFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(intent);
    }
}
