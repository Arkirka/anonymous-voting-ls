package ru.vorobyov.voting.controller;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.vorobyov.voting.entities.Client;
import ru.vorobyov.voting.entities.Voting;
import ru.vorobyov.voting.services.ClientService;
import ru.vorobyov.voting.services.VotingService;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController  //warning
@RequestMapping("votingResults")
public class VotingResultsController {

    @Autowired
    private VotingService votingService;

    @Autowired
    private ClientService clientService;

    @RequestMapping("/")
    public ModelAndView getPage () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("votingresults");
        return modelAndView;
    }

    @GetMapping("getHeader")
    public String getHeader(){

        String header = "";
        String code = "";
        for(Voting voting : votingService.findAll()){
            header = voting.getTheme() + ";" + String.valueOf(voting.getCode());

        }

        return header;
    }
    @GetMapping("vote{vote}")
    public String getProgress(@PathVariable String vote){
        String response = "";
        Voting voting = new Voting();
        for(Voting votingFromDb : votingService.findAll()){
            voting = votingFromDb;
        }

        if(vote.equals("yes")){

            response = String.valueOf(voting.getYes());

        } else if(vote.equals("no")){

            response = String.valueOf(voting.getNo());

        } else if(vote.equals("neutral")){

            response = String.valueOf(voting.getNeutral());

        } else if(vote.equals("broken")){

            response = String.valueOf(voting.getBroken());

        }
        return response;
    }

    @GetMapping("print")
    public HttpStatus print(){
        printReport();
        printBilluten();
        return HttpStatus.OK;
    }

    @RequestMapping(value = "/zip", produces="application/zip")
    public byte[] zipFiles(HttpServletResponse response) throws IOException {
        //setting headers
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"reports.zip\"");

        //creating byteArray stream, make it bufforable and passing this buffor to ZipOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

        //simple file list, just for tests
        ArrayList<File> files = new ArrayList<>();
        File dir = new File("src\\main\\resources\\documentsResult\\");
        for (File file : dir.listFiles()){
            if (file.isFile())
                files.add(file);
            }

        //packing files
        for (File file : files) {
            //new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream = new FileInputStream(file);

            IOUtils.copy(fileInputStream, zipOutputStream);

            fileInputStream.close();
            zipOutputStream.closeEntry();
        }

        if (zipOutputStream != null) {
            zipOutputStream.finish();
            zipOutputStream.flush();
            IOUtils.closeQuietly(zipOutputStream);
        }
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);

        for (File file : dir.listFiles()){
            if (file.isFile())
                file.delete();
        }
        return byteArrayOutputStream.toByteArray();
    }

    @DeleteMapping("closevoting")
    public HttpStatus delete(){
        votingService.delete();
        clientService.delete();
        return HttpStatus.OK;
    }

    private void printBilluten(){
        int count = 0;
        for(Client client: clientService.findAll()){
            count++;

            try (FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\documentTemplates\\BillutenTemplate.docx")) {

                // открываем файл и считываем его содержимое в объект XWPFDocument
                XWPFDocument docxFile = new XWPFDocument(OPCPackage.open(fileInputStream));

                XWPFDocument doc = new XWPFDocument();

                // печатаем содержимое всех параграфов документа
                List<XWPFParagraph> paragraphs = docxFile.getParagraphs();
                for (XWPFParagraph p : paragraphs) {

                    String pResult = verifyParagraph(p.getText());
                    pResult = verifyBilluten(pResult, client);
                    XWPFParagraph p1 = doc.createParagraph();
                    p1.setAlignment(ParagraphAlignment.BOTH);

                    XWPFRun r1 = p1.createRun();
                    r1.setBold(false);
                    r1.setItalic(false);
                    r1.setFontSize(14);
                    r1.setFontFamily("Times New Roman");
                    r1.setText(pResult);
                }
                // save it to .docx file
                Calendar calendar = new GregorianCalendar();
                String time =  "_" + calendar.get(Calendar.YEAR) + "_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.DAY_OF_MONTH) + "_" + calendar.get(Calendar.HOUR) + "_" + calendar.get(Calendar.MINUTE) + "_" + calendar.get(Calendar.SECOND);
                String path = "src\\main\\resources\\documentsResult\\Billuten_№" + String.valueOf(count) + ".docx";
                try (FileOutputStream out = new FileOutputStream(path)) {
                    doc.write(out);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void printReport(){
        String text = "";
        Voting voting = new Voting();
        for(Voting votingFromDb : votingService.findAll()){
            voting = votingFromDb;
        }

        try (FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\documentTemplates\\ReportTemplate.docx")) {

            // открываем файл и считываем его содержимое в объект XWPFDocument
            XWPFDocument docxFile = new XWPFDocument(OPCPackage.open(fileInputStream));
            XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(docxFile);

            XWPFDocument doc = new XWPFDocument();

            // печатаем содержимое всех параграфов документа
            List<XWPFParagraph> paragraphs = docxFile.getParagraphs();
            for (XWPFParagraph p : paragraphs) {

                String pResult = verifyParagraph(p.getText()) ;
                XWPFParagraph p1 = doc.createParagraph();
                p1.setAlignment(ParagraphAlignment.BOTH);

                XWPFRun r1 = p1.createRun();
                r1.setBold(false);
                r1.setItalic(false);
                r1.setFontSize(14);
                r1.setFontFamily("Times New Roman");
                r1.setText(pResult);
            }
            // save it to .docx file
            Calendar calendar = new GregorianCalendar();
            String time =  "_" + calendar.get(Calendar.YEAR) + "_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.DAY_OF_MONTH) + "_" + calendar.get(Calendar.HOUR) + "_" + calendar.get(Calendar.MINUTE) + "_" + calendar.get(Calendar.SECOND);
            String path = "src\\main\\resources\\documentsResult\\Report.docx";
            try (FileOutputStream out = new FileOutputStream(path)) {
                doc.write(out);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String verifyParagraph(String p){
        Voting voting = new Voting();
        for(Voting votingFromDb : votingService.findAll()){
            voting = votingFromDb;
        }

        return p.replace("##votingQuestion", voting.getTheme())
                .replace("##votingMembers", String.valueOf(voting.getYes() + voting.getNo() + voting.getNeutral() + voting.getBroken()))
                .replace("##voicesFor", String.valueOf(voting.getYes())).replace("##voicesVersus", String.valueOf(voting.getNo()))
                .replace("##voicesNeutral", String.valueOf(voting.getNeutral()))
                .replace(" ##voicesBroken", String.valueOf(voting.getBroken()));
    }

    private String verifyBilluten(String p, Client client){
        return p.replace("##VoicesFor", String.valueOf(client.getYes())).replace( "##VoicesVersus", String.valueOf(client.getNo()))
                .replace( "##VoicesNeutral", String.valueOf(client.getNeutral()));
    }
}
