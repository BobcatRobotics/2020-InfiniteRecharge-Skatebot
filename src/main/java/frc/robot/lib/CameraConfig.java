package frc.robot.lib;

public class CameraConfig  {
    private int team = 177;
    private boolean server = true;

    private String name;
    private String path;
    private String cameraConfig;
    private String streamConfig;


    public int getTeam() {
        return this.team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public boolean isServer() {
        return this.server;
    }

    public boolean getServer() {
        return this.server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCameraConfig() {
        return this.cameraConfig;
    }

    public void setCameraConfig(String cameraConfig) {
        this.cameraConfig = cameraConfig;
    }

    public String getStreamConfig() {

        return this.streamConfig;
    }

    public void setStreamConfig(String streamConfig) {
        this.streamConfig = streamConfig;
    }

   
    @Override
    public String toString() {
        return "{" +
            " team='" + getTeam() + "'" +
            ", server='" + isServer() + "'" +
            ", name='" + getName() + "'" +
            ", path='" + getPath() + "'" +
            ", cameraConfig='" + getCameraConfig() + "'" +
            ", streamConfig='" + getStreamConfig() + "'" +
            "}";
    }
}

