package com.example.myapplication;

import android.util.Log;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class PDFExtractor {

    private FirebaseFirestore db;

    public PDFExtractor() {

        db = FirebaseFirestore.getInstance();
    }

    public void extractDataFromPDF(String pdfFilePath) {
        try {

            PdfReader reader = new PdfReader(pdfFilePath);
            PdfDocument pdfDoc = new PdfDocument(reader);


            StringBuilder extractedText = new StringBuilder();
            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                extractedText.append(PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i)));
            }


            HashMap<String, String> pdfData = new HashMap<>();
            pdfData.put("schedule", extractedText.toString());


            db.collection("schedules").add(pdfData)
                    .addOnSuccessListener(documentReference -> {

                        Log.d("Firestore", "Document successfully written with ID: " + documentReference.getId());
                    })
                    .addOnFailureListener(e -> {

                        Log.e("Firestore", "Error adding document", e);
                    });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PDFExtractor", "Error extracting data from PDF", e);
        }
    }}
