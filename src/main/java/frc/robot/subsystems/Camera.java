package frc.robot.subsystems;

import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.lib.CameraConfig;
import frc.robot.lib.CameraFileReader;
import frc.robot.lib.RioLogger;

/**
 * Camera Subsystem. Allows camera to be initialized from JSON file
 */
public class Camera extends Subsystem {
    private Thread m_visionThread;
    private boolean readJSONFile = true;
    private String name = "Bobcat Vision";
    private String path = "/dev/video0";

    public Camera() {
    }

    public void initializeCamera() {

        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(320,240);
        camera.setFPS(30);
        // CameraServer inst = CameraServer.getInstance();
        // CameraFileReader camReader = new CameraFileReader();
        
        // m_visionThread = new Thread(() -> {
        //     CameraConfig config = null;

        //     if (readJSONFile) {
        //         RioLogger.log("Reading Camera JSON File");
        //         camReader.readCameraConfigFile();
        //         RioLogger.log("Camera File Reader is " + camReader.toString());
        //         if (camReader.isReadConfig()) {
        //             config = camReader.getPrimaryCamera();
        //             RioLogger.log("Primary Camera is " + config.toString());
        //             name = config.getName();
        //             path = config.getPath();
        //         }
        //     }

        //     UsbCamera camera = new UsbCamera(name, path);
        //     // Set the resolution
        //     // camera.setResolution(1280, 720);
        //     // camera.setResolution(320, 240);
        //     MjpegServer server = inst.startAutomaticCapture(camera);

        //     if (camReader.isReadConfig()) {
        //         camera.setConfigJson(config.getCameraConfig());
        //         camera.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
        //         server.setConfigJson(config.getStreamConfig());
        //     }

        //     RioLogger.log("Starting camera '" + name + "' on " + path);
        //     RioLogger.log("Camera settings:" + cameraValues(camera));

        //     while (!Thread.interrupted()) {

        //     }
        // });
        // m_visionThread.setDaemon(true);
        // m_visionThread.start();
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    private String cameraValues(UsbCamera camera) {
        StringBuffer sb = new StringBuffer();
        sb.append("name=" + camera.getName() + ", ");
        sb.append("path=" + camera.getPath() + ", ");
        VideoMode vm = camera.getVideoMode();
        sb.append("height=" + vm.height + ", ");
        sb.append("width=" + vm.width + ", ");
        return sb.toString();
    }
}
