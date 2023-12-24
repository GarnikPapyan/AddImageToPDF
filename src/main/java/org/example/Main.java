package org.example;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        System.out.println("Please enter the first letters of your first name, last name and patronymic to create a signature: AAA or AA ");
        Scanner scanner = new Scanner(System.in);
        String userInput = "AAA";
        boolean out = false;
        while (!out) {
           userInput = scanner.nextLine();
            if(userInput.length() > 3 || userInput.length() < 2) {
                System.out.println("More than 3 or less than 2 characters were entered, try again!! ");
            } else {
                System.out.println("This is the correct input thx ");
                out = true;
            }
        }
        addTextToImage(userInput);

        System.out.println("Please enter the path to your PDF file, if there is none, press '0' and the default file will be selected!");
        String pathToPdf = "C:\\Users\\Garnik-PC\\IdeaProjects\\ProcessingApplicationImgPdf\\src\\main\\resources\\pdf-dokument.pdf";
        String defaultPath = pathToPdf;
        boolean outForPdf = false;
        while (!outForPdf) {
            pathToPdf = scanner.nextLine();
            Path pdfPathIn = Paths.get(pathToPdf);
            if(pathToPdf.equals("0") || Files.exists(pdfPathIn)) {
                if(pathToPdf.equals("0")) {
                    pathToPdf = defaultPath;
                }
                try {

                    File file = new File(pathToPdf);
                    PDDocument document = Loader.loadPDF(file);


                    PDPage firstPage = document.getPage(0);


                    BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\Garnik-PC\\IdeaProjects\\ProcessingApplicationImgPdf\\src\\main\\resources\\out.png"));
                    PDImageXObject pdImageXObject = LosslessFactory.createFromImage(document, bufferedImage);


                    PDPageContentStream contentStream = new PDPageContentStream(document, firstPage, PDPageContentStream.AppendMode.APPEND, true);
                    contentStream.drawImage(pdImageXObject, 450, 10);
                    contentStream.close();


                    String savePdfPath ;
                    boolean outFinal = false;
                    while (!outFinal) {
                        System.out.println("Enter the correct path to save the file: C:\\Users\\Garnik-PC\\IdeaProjects\\File-Name.pdf ");
                        savePdfPath = scanner.nextLine();

                        Path pdfPath = Paths.get(savePdfPath);
                        if(!isValidPath(pdfPath)) {
                            System.out.println("Please enter correct path");
                            continue;
                        }
                        if(!savePdfPath.contains(".pdf")) {
                            System.out.println("Please enter correct path");
                            continue;
                        }
                        if(Files.exists(pdfPath)) {
                            Files.delete(pdfPath);
                        }
                        outFinal = true;
                        document.save(savePdfPath);
                        document.close();

                        System.out.println("The image was successfully added to an existing PDF page: " + pathToPdf);

                    }
                } catch (Exception e) {
                    System.out.println("File errors " + e.getMessage());
                }
                outForPdf = true;
            } else {
                System.out.println("This is not correct path please tyr again or enter '0' :");
            }

        }

    }


    public static void addTextToImage(String text) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File("C:\\Users\\Garnik-PC\\IdeaProjects\\ProcessingApplicationImgPdf\\src\\main\\resources\\pechatmy.png"));
        BufferedImage imageWithText = new BufferedImage(
                bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);


        Graphics g = imageWithText.getGraphics();
        g.drawImage(bufferedImage, 0, 0, null);


        g.setColor(Color.BLUE);
        Font font = new Font("Arial", Font.BOLD, 30);
        g.setFont(font);


        g.drawString(text, 30, 60);


        g.dispose();

        try {
            File outputImageFile = new File("C:\\Users\\Garnik-PC\\IdeaProjects\\ProcessingApplicationImgPdf\\src\\main\\resources\\out.png");
            ImageIO.write(imageWithText, "png", outputImageFile);
        } catch (Exception e) {
            System.out.println("You have error " + e.getMessage());
        }

    }
    private static boolean isValidPath(Path path) {
        return path.isAbsolute();
    }
//by Bugich(PG)
}
