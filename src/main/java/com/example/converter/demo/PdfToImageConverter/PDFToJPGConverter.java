package com.example.converter.demo.PdfToImageConverter;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class PDFToJPGConverter {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String directoryPath = "C:\\Users\\Acer\\Desktop\\LesenTeil"; // Path directory

        System.out.print("Enter starting number for the new file sequence: ");
        int newStartNumber = sc.nextInt();

        // Open directory
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Invalid directory path: " + directoryPath);
            return;
        }

        // Get PDF files list
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
        if (files == null || files.length == 0) {
            System.out.println("No PDF files found in the directory.");
            return;
        }

        System.out.println("Found " + files.length + " PDF files. Starting conversion and deletion process...");

        int newNumber = newStartNumber;

        for (File currentFile : files) {
            PDDocument document = null;
            try {
                // Load PDF document
                document = PDDocument.load(currentFile);
                PDFRenderer pdfRenderer = new PDFRenderer(document);

                // Convert the first page <--(index 0)--> to an image
                BufferedImage image = pdfRenderer.renderImageWithDPI(0, 300);

                // Save image
                String outputFileName = "LesenTeil_Seite_" + newNumber + ".jpg";
                File outputFile = new File(directoryPath, outputFileName);
                ImageIO.write(image, "jpg", outputFile);
                System.out.println("Image created: " + outputFileName);

                newNumber++;

            } catch (IOException e) {
                System.err.println("Error processing file " + currentFile.getName() + ": " + e.getMessage());
            } finally {
                // Documents should be closed
                if (document != null) {
                    try {
                        document.close();
                    } catch (IOException e) {
                        System.err.println("Failed to close document: " + e.getMessage());
                    }
                }
            }

            // Delete original PDF file after converting into image
            if (currentFile.delete()) {
                System.out.println("Deleted original PDF: " + currentFile.getName());
            } else {
                System.err.println("Failed to delete PDF: " + currentFile.getName());
            }
        }

        System.out.println("Conversion and deletion process completed.");
        sc.close();
    }
}
