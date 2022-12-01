package core;

import core.model.SomeResponce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class ReaderCSV {

    @Autowired
    private ResourceLoader resourceLoader;

    @RequestMapping("/symbols")
    public SomeResponce showYandexData(@RequestParam(value = "symbol") String symbol) {
        SomeResponce response = new SomeResponce();
        if ("yndx".equals(symbol)) {

            response.setResponse(csvToHTML(readCSV("yndx_copy.csv")));

        } else {
            response.setResponse("No Symbol Provided. Provide a symbol!");
        }

        return response;
    }

    private List<List<String>> readCSV(String filename) {
        List<List<String>> records = new ArrayList<>();
        Resource resource = resourceLoader.getResource(String.format("classpath:static/%s", filename));

        try (BufferedReader bufReader = new BufferedReader(
                new FileReader(resource.getFile()))) {
            String line;
            while ((line = bufReader.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return records;

    }

    private String csvToHTML(List<List<String>> listOfCsv) {

        StringBuilder outHTML = new StringBuilder();
        String header = """
                <!DOCTYPE html>
                <html lang="en-US">
                <head>

                \t<meta http-equiv="content-type" content="text/html; charset=utf-8" />
                \t<title>Yandex Table</title>

                </head>
                <body>""";
        String footer = "</body></html>";
        outHTML.append(header);

        String table = """
                <table><tr>
                <th>Open</th>
                <th>High</th>
                <th>Low</th>
                <th>Close</th>
                <th>Volume</th>
                <th>Adjusted</th>
                <th>Mean</th>
                <th>Returns</th>
                <th>Date</th></tr>""";
        String closeTable = "</table>";
        outHTML.append(table);

        for (List<String> line : listOfCsv) {
            outHTML.append("<tr>");

            for (String item : line) {
                outHTML.append(String.format("<td>%s</td>", item));
            }

            outHTML.append("</tr>");
        }
        outHTML.append(closeTable);
        outHTML.append(footer);
        return outHTML.toString();
    }
}
