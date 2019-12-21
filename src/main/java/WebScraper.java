import java.net.*;
import java.util.*;
import java.io.*;

public class WebScraper {
    public static void main(String[] args) throws Exception
    {
//    	https://en.wikipedia.org/wiki/Carnivorous -> /wiki/Carnivorous
        List<Map<String, String>> levels = new ArrayList<Map<String, String>>();
        Map<String, String> startLevel = new HashMap<String, String>();
//        Set<String> startLevel = new HashSet<String>();
        String ret;
        String start = "/wiki/Anime";
        String end = "/wiki/Alt-right";

        startLevel.put(start, start);
        levels.add(startLevel);

        int i = 0;
        while(i < levels.size())
        {
            ret = getLinks(levels, levels.get(i), end);
            if(ret != null)
                System.out.println("SUCCESS AT " + i + ", path is " + ret);
            else
                System.out.println("FAILURE AT " + i);
            i ++;
        }

        System.out.println("Finished");
    }

    // TODO: Use map where key is link and value is path to key
    static String getLinks(List<Map<String, String>> levels, Map<String, String> currLevel, String end) throws Exception
    {
//        Set<String> links = new HashSet<String>();
        String val, currLink;
        Map<String, String> links = new HashMap<String, String>();
        for(Map.Entry<String,String> entry : currLevel.entrySet())
        {
            currLink = entry.getKey();
            val = entry.getValue();
            URL startURL = new URL("https://en.wikipedia.org" + currLink);
            BufferedReader in = new BufferedReader(new InputStreamReader(startURL.openStream()));

            String inputLine, newLink;
            int href, openQuote, closeQuote;
            while ((inputLine = in.readLine()) != null)
            {
                href = inputLine.indexOf("href=");
                if (href != -1)
                {
                    openQuote = inputLine.indexOf('\"', href + 1);
                    closeQuote = inputLine.indexOf('\"', openQuote + 1);
                    if (openQuote != -1 && closeQuote != -1)
                    {
                        newLink = inputLine.substring(openQuote + 1, closeQuote);
                        if (newLink.contains("/wiki/") && !newLink.contains("."))
                        {
                            links.put(newLink, val + ", " + newLink);
                            if(newLink.equals(end))
                                return val + ", " + newLink;
                        }
                    }
                }
            }
            in.close();
        }

        levels.add(links);

        return null;
    }
}
