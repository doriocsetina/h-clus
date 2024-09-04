# UML Class Diagram

```plantuml
@startuml
class GuiController {
    - model: GuiModel
    - view: GuiView
    + actionPerformed(ActionEvent e)
    + LoadDendrogramButtonListener
    + CalculateDendrogramButtonListener
    + ClosingWindowListener
}

class GuiModel {
    - out: ObjectOutputStream
    - in: ObjectInputStream
    - socket: Socket
    - address: InetAddress
    - port: int
    - objectMapper: ObjectMapper
    + connectToServer(String ip, int port): boolean
    + receiveJsonTables(): String
    + decodeJsonString(String jsonString): List<String>
    + sendCalculateRequest(String table, int depth, int distance, boolean save, String saveName): String
    + sendLoadRequest(String table, String fileName): String
    + sendCloseMessage()
}

class GuiView {
    - mainFrame: JFrame
    + getIpAddress(): String
    + getPort(): String
    + closeLoginFrame()
    + createAndShowMainGUI(List<String> tables)
    + addLoadButtonListener(ActionListener listener)
    + addCalculateButtonListener(ActionListener listener)
    + addWindowListener(WindowListener listener)
    + setErrorMessage(String message)
    + setOutputText(String text)
    + setInfoMessage(String message)
    + getTableString(): String
    + getFileNameString(): String
    + getDistanceString(): String
    + isSaving(): boolean
    + getSaveFileString(): String
}

GuiController --> GuiModel
GuiController --> GuiView
GuiController ..> LoadDendrogramButtonListener
GuiController ..> CalculateDendrogramButtonListener
GuiController ..> ClosingWindowListener

LoadDendrogramButtonListener -up-|> ActionListener
CalculateDendrogramButtonListener -up-|> ActionListener
ClosingWindowListener -up-|> WindowListener
@enduml