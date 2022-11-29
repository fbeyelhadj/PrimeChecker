import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class server extends Application {
	String area = "";

	public void start(Stage primaryStage) {
		TextArea ta = new TextArea();
		Scene scene = new Scene(new ScrollPane(ta), 450, 200);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();

		new Thread(() -> {
			try {
				ServerSocket serverSocket = new ServerSocket(8000);
				Platform.runLater(() -> ta.appendText("Server started at " + new Date() + '\n'));

				Socket socket = serverSocket.accept();

				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

				while (true) {
					int numb = inputFromClient.readInt();
					if (numb < 2 || numb % 2 == 0)
						area = "  not prime";
					else
						area = "prime";
					outputToClient.writeUTF(area);
					Platform.runLater(() -> {
						ta.appendText("The number received from client and to be checked is : " + numb + '\n');

					});
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}).start();
		Client client = new Client();
		Stage clientStage = new Stage();
		clientStage.initOwner(primaryStage);
		client.start(clientStage);
	}

	/**
	 * The main method is only needed for the IDE with limited JavaFX support. Not
	 * needed for running from the command line.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}