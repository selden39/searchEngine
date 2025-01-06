package searchengine.services.stopindexingexecutor;

public class LastErrorMessage {
    private static String lastErrorMessage = "";

    public static void setLastErrorMessage(String lastErrorMessage) {
        LastErrorMessage.lastErrorMessage = lastErrorMessage;
    }

    public static String getLastErrorMessage(){
        return lastErrorMessage;
    }
}
