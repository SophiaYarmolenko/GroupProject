package com.groupProject.groupProject.controlles;
import lombok.Getter;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
@Getter
public class ArticleGenerationController
{
    private String articleName = "Something interesting";

    public String getName() throws IOException {
        URL url;
        HttpURLConnection connection;
        BufferedReader reader = null;
        String line;
        StringBuilder result = new StringBuilder();
        try
        {
            String link = "https://en.wikipedia.org/w/api.php?action=query&list=random&format=json&rnnamespace=0&rnlimit=1";
            url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            try
            {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                while ((line = reader.readLine()) != null)
                {
                    result.append(line + "\n");
                }
            }
            finally
            {
                connection.disconnect();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(reader != null)
                reader.close();
        }
        this.articleName = parseJsonResult(result.toString());
        return articleName.replaceAll(" ", "_");
    }

    public String parseJsonResult(String result) {
        int lastIndexOfColon = result.lastIndexOf(":");
        int lastIndexOfQuotes = result.lastIndexOf("\"");
        return result.substring(lastIndexOfColon+2, lastIndexOfQuotes);
    }

    public String generateGetRequestLinkToWiki() throws IOException {
        return "https://en.wikipedia.org/wiki/"+getName();
    }
}
