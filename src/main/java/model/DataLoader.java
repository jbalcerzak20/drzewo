package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class DataLoader {

    private String pathFile = "dane.dat";
    private File file = null;

    public DataLoader()
    {
        file = new File(pathFile);

        if(!exists())
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean exists()
    {
        if(file.exists())
            return true;
        else
            return false;
    }

    private String getText()
    {
        StringBuilder stringBuilder = new StringBuilder();
        String line = "";
        try {
            FileReader reader = new FileReader(file);
            BufferedReader buffer = new BufferedReader(reader);

            while ((line = buffer.readLine())!=null)
            {
                stringBuilder.append(line);
            }

            buffer.close();
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public JSONObject getDataAsJSON()
    {
        JSONObject object = null;

        String js = getText();

        if(js.equals(""))
            object = new JSONObject();
        else
            object = new JSONObject(js);


        if(!object.has("elements"))
            object.put("elements",new JSONArray());

        return object;
    }

    public void saveJSONToFile(JSONObject json)
    {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.println(json.toString(2));

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
