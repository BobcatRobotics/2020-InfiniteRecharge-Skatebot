package frc.robot.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.Reader;
import java.io.StringReader;


/**
 * Parses Camera Definition JSON File
 */
/*
  JSON format:
  {
      "team": <team number>,
      "ntmode": <"client" or "server", "client" if unspecified>
      "cameras": [
          {
              "name": <camera name>
              "path": <path, e.g. "/dev/video0">
              "pixel format": <"MJPEG", "YUYV", etc>   // optional
              "width": <video mode width>              // optional
              "height": <video mode height>            // optional
              "fps": <video mode fps>                  // optional
              "brightness": <percentage brightness>    // optional
              "white balance": <"auto", "hold", value> // optional
              "exposure": <"auto", "hold", value>      // optional
              "properties": [                          // optional
                  {
                      "name": <property name>
                      "value": <property value>
                  }
              ],
              "stream": {  // optional
                  "properties": [
                      {
                          "name": <stream property name>
                          "value": <stream property value>
                      }
                  ]
              }
          }
      ]
  }
*/
public class CameraFileReader {
    private String fileName = File.separator + "boot" + File.separator + "camera.json";
    private boolean readConfig = false;
    private List<CameraConfig> cameras = new ArrayList<CameraConfig>();

    public CameraFileReader() {
    }

    public String getFileName() {
        return this.fileName;
    }

    public boolean isReadConfig() {
        return this.readConfig;
    }

    public List<CameraConfig> getCameras() {
        return this.cameras;
    }

    public CameraConfig getPrimaryCamera() {
        if (cameras.size() > 0) {
            return cameras.get(0);
        } else {
            return new CameraConfig();
        }
    }

    /**
     * Read configuration file.
     */
    public boolean readCameraConfigFile() {
        CameraConfig camcfg = new CameraConfig();

        // Check if the file exists
        File cameraFile = new File(fileName);
        if (!cameraFile.exists()) {
            RioLogger.errorLog("camera configuration file does not exist. " + fileName);
            return false;
        }

        // parse file
        JsonObject obj = null;
        JsonElement top;
        try {
            top = JsonParser.parseString(Files.readString(Paths.get(fileName)));
        } catch (Exception ex) {
            RioLogger.errorLog("JSON Parser could not open file. " + ex);
            return false;
        }
        RioLogger.log("CameraFileReader. Finished parsing JSON File  " + fileName);

        // top level must be an object
        if (!top.isJsonObject()) {
            RioLogger.errorLog("JSON file has wrong structure. top is not a JSON object");
            return false;
        }
        obj = top.getAsJsonObject();
 
        JsonElement teamElement = obj.get("team");
        if (teamElement == null) {
            RioLogger.errorLog("could not read team number from JSON");
            return false;
        }
        camcfg.setTeam(teamElement.getAsInt());

        // ntmode (optional)
        if (obj.has("ntmode")) {
            String str = obj.get("ntmode").getAsString();
            if ("client".equalsIgnoreCase(str)) {
                camcfg.setServer(false);
            } else if ("server".equalsIgnoreCase(str)) {
                camcfg.setServer(true);
            } else {
                RioLogger.log("could not understand ntmode value '" + str + "'");
            }
        }
        // cameras
        JsonElement camerasElement = obj.get("cameras");
        if (camerasElement == null) {
            RioLogger.errorLog("could not read cameras");
            return false;
        }
        JsonArray camerajson = camerasElement.getAsJsonArray();
        for (JsonElement camera : camerajson) {
             if (!readCameraConfig(camcfg, camera.getAsJsonObject())) {
                RioLogger.errorLog("Error processing camera element in JSON." + camera);
                return false;
            }
            // Add a Camera
            cameras.add(camcfg);
        }
        RioLogger.log("CameraFileReader. Finished adding cameras. Nbr Cameras is  " + cameras.size());
        readConfig = true;
        return true;
    }

    /**
     * Read single camera configuration.
     */
    private boolean readCameraConfig(CameraConfig camcfg, JsonObject config) {
        RioLogger.log("Parsing camera." + config);
        // name
        JsonElement nameElement = config.get("name");
        if (nameElement == null) {
            RioLogger.errorLog("could not read camera name");
            return false;
        }
        camcfg.setName(nameElement.getAsString());
   
        // path
        JsonElement pathElement = config.get("path");
        if (pathElement == null) {
            RioLogger.errorLog("camera '" + camcfg.getName() + "': could not read path");
            return false;
        }
        camcfg.setPath(pathElement.getAsString());
  
        // stream properties
        JsonElement streamElement = config.get("stream");
        if (streamElement == null) {
            RioLogger.errorLog("camera '" + camcfg.getName() + "': could not read stream.");
            return false;
        }
 
        RioLogger.log("Finished parsing camera.");
        Gson gson = new GsonBuilder().create();
        camcfg.setStreamConfig(gson.toJson(streamElement));
        camcfg.setCameraConfig(gson.toJson(config));
        return true;
    }

    @Override
    public String toString() {
        return "{" +
            " fileName='" + fileName + "'" +
            ", readConfig='" + readConfig + "'" +
            ", cameras='" + cameras + "'" +
            "}";
    }
}