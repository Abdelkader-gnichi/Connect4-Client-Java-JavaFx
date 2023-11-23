package application;

import java.time.LocalDateTime;
import java.util.Arrays;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

	private static int precedent;
	private static int nbTour;
	private static int initial = 1;
	private Portepion[][] portepion;

	Button re =new Button();
	@Override
	public void start(Stage primaryStage) {
		
		
		Label VS = new Label("VS"); Label player1 = new Label("PLAYER1"); Label player2 = new Label("PLAYER2");
		Label winner1 = new Label(""); Label egalite = new Label(""); Label winner2 = new Label("");
		try {
			Thread thread = new Thread("B") {
				public void run() {
					
					while (true) {
					
						if(arayfromBool(getWinOrEqTab())[0][0]==true && arayfromBool(getWinOrEqTab())[1][0]==false) {
							winner1.setTextFill(Color.RED);
							winner1.setText("RED WON!");
							winner2.setTextFill(Color.BLACK);
							winner2.setText("BLACK LOST!");
							player1.setVisible(false);
							player2.setVisible(false);
							winner1.setVisible(true);
							winner2.setVisible(true);
						}else if(arayfromBool(getWinOrEqTab())[0][0]==false && arayfromBool(getWinOrEqTab())[1][0]==true){
							winner2.setTextFill(Color.BLACK);
							winner2.setText("BLACK WON!");
							winner1.setTextFill(Color.RED);
							winner1.setText("RED LOST!");
							player1.setVisible(false);
							player2.setVisible(false);
							winner1.setVisible(true);
							winner2.setVisible(true);
							
						}else if(arayfromBool(getWinOrEqTab())[0][0]==false && arayfromBool(getWinOrEqTab())[1][0]==false && arayfromBool(getWinOrEqTab())[2][0]==true){
							
							VS.setVisible(false);
							egalite.setText("EGALITE !");
							egalite.setVisible(true);
						}
							
						if(Boolean.parseBoolean(isReset())) {
							resetWinOrEq();
							winner1.setVisible(false);
							winner2.setVisible(false);
							egalite.setVisible(false);
							player1.setVisible(true);
							player2.setVisible(true);
							VS.setVisible(true);
							re.fire();
                     	   initial=1;
							for (int i = 0; i < 7; i++) {
								for (int j = 0; j < 6; j++)
									portepion[i][j].setImage(null);
						}
						
							setResetToFalse();
							
					}
						
						
						if (initial % 2 == 1) {
							
							
							int[][] tableau = arayfromString(getTable());
							for (int i = 0; i < 7; i++) {
								for (int j = 0; j < 6; j++)
									portepion[i][j].set(tableau[i][j]);
							}
						}
						else if (initial % 2 == 0) {
							
							int[][] tableau = arayfromString(getTable());
						    for (int i = 0; i < 7; i++) {
							  for (int j = 0; j < 6; j++)
								portepion[i][j].set(tableau[i][j]);
						}
						}
					
				}
				}
			};

			thread.start();

			// alignement
			int N = 4;
			// colonnes et lignes
			int C = 7;
			int L = 6;
			// couleur de la page jeu
			Color couleurPage = Color.rgb(61, 73, 86);

			Group root = new Group();
			Scene scene = new Scene(root, 462, 623);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene.setFill(couleurPage);

			Group root1 = new Group();
			Scene scene1 = new Scene(root1, 402, 432);
			scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene1.setFill(couleurPage);
			root1.setLayoutX(36);
			root1.setLayoutY(170);
			root.getChildren().add(root1);

			Circle circle1 = new Circle();
			Circle circle2 = new Circle();
			circle1.setFill(Color.RED);
			circle2.setFill(Color.BLACK);


			circle1.setLayoutX(112);
			circle1.setLayoutY(59);
			circle1.setRadius(32);

			circle2.setLayoutX(385);
			circle2.setLayoutY(59);
			circle2.setRadius(32);


			root.getChildren().addAll(circle1, circle2);

			When w = Bindings.when((scene1.widthProperty().divide(scene1.heightProperty())).greaterThan(7.0 / 6.0));

			Rectangle r = new Rectangle(0, 0, 402, 432);

			r.setFill(Color.rgb(61, 73, 86));

			
			root1.getChildren().addAll(r);
			// ?

			r.heightProperty().bind(
					w.then(scene1.heightProperty().subtract(100)).otherwise(r.widthProperty().multiply(6.0 / 7.0)));
			r.widthProperty().bind(w.then(r.heightProperty().multiply(7.0 / 6.0)).otherwise(scene1.widthProperty()));

			for (int i = 0; i < L; i++) {
				for (int j = 0; j < C; j++) {
					
					Circle c = new Circle(5 + 28 + 100 * j, 5 + 28 + 100 * i, 28);
					c.setFill(Color.WHITE);
					c.radiusProperty().bind(r.heightProperty().divide(12).subtract(5));
					c.centerXProperty().bind(r.widthProperty().divide(7).multiply(j + 0.5));
					c.centerYProperty().bind(r.heightProperty().divide(6).multiply(i + 0.5));

					root1.getChildren().add(c);
				}
			}

			portepion = new Portepion[7][6];

			for (int i = 0; i < L; i++) {
				for (int j = 0; j < C; j++) {
					// started with colonne after ligne
					portepion[j][i] = new Portepion();
					portepion[j][i].layoutXProperty().bind(r.widthProperty().divide(7).multiply(j));
					
					portepion[j][i].layoutYProperty().bind(r.heightProperty().divide(6).multiply(i));
					portepion[j][i].fitHeightProperty().bind(r.heightProperty().divide(6));
					portepion[j][i].fitWidthProperty().bind(r.widthProperty().divide(7));

					root1.getChildren().add(portepion[j][i]);
				}
			}

			
			VS.setTextFill(Color.WHITE);
			VS.setFont(Font.font("System", FontWeight.BOLD, scene.getHeight() / 20));
			VS.setLayoutX(227);
			VS.setLayoutY(60);
			
			// player1
			player1.setTextFill(Color.WHITE);
			player1.setFont(Font.font("System", FontWeight.BOLD, scene.getHeight() / 20));
			player1.setLayoutX(47);
			player1.setLayoutY(94);
			
			// player2
			player2.setTextFill(Color.WHITE);
			player2.setFont(Font.font("System", FontWeight.BOLD, scene.getHeight() / 20));

			player2.setLayoutX(321);
			player2.setLayoutY(94);

			// button1
			Button Exit = new Button("Exit");
			Exit.setTextFill(Color.BLACK);
			Exit.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, scene.getHeight() / 20));
			Exit.setLayoutX(41);
			Exit.setLayoutY(540);
			Exit.prefWidth(115);
			Exit.prefHeight(41);
			Exit.getStyleClass().add("Exit");
			Exit.setOnAction(e -> Platform.exit());

			// button2
			Button reset = new Button("Reset");
			
			reset.setTextFill(Color.BLACK);
			reset.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, scene.getHeight() / 20));
			reset.setLayoutX(321);
			reset.setLayoutY(540);
			reset.prefWidth(115);
			reset.prefHeight(41);
			reset.getStyleClass().add("reset");
			reset.setOnAction(e -> {
				
				resetWinOrEq();				winner1.setVisible(false);
				winner2.setVisible(false);
				egalite.setVisible(false);
				player1.setVisible(true);
				player2.setVisible(true);
				VS.setVisible(true);
				reset();
				re.fire();
				initial=1;
				for (int i = 0; i < 7; i++) {
					for (int j = 0; j < 6; j++)
						portepion[i][j].setImage(null);
			}
			     	
			});

			re.setOnAction(e -> {
				/*Main app = new Main();
				app.start(primaryStage);*/
				//LocalDateTime.now().getSecond() % 2 == 0
				winner2.setVisible(false);
				egalite.setVisible(false);
				player1.setVisible(true);
				player2.setVisible(true);
				VS.setVisible(true);
				initial=1;
				for (int i = 0; i < 7; i++) {
					for (int j = 0; j < 6; j++)
						portepion[i][j].setImage(null);
			}});
			     
			
			
			winner1.setTextFill(Color.RED);
			winner1.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, scene.getHeight() / 25));
			winner1.setLayoutX(316);
			winner1.setLayoutY(98);

			winner1.setVisible(false);
			
			egalite.setTextFill(Color.WHITE);
			egalite.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, scene.getHeight() / 25));
			egalite.setLayoutX(215);
			egalite.setLayoutY(60);

			egalite.setVisible(false);
			
			winner2.setTextFill(Color.BLACK);

			winner2.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, scene.getHeight() / 25));
			winner2.setLayoutX(47);
			winner2.setLayoutY(98);

			winner2.setVisible(false);
			root.getChildren().addAll(player1, player2, winner1, winner2, reset, Exit, VS, egalite);

			Rectangle[] curseurselect = new Rectangle[C];
			for (int i = 0; i < C; i++) {
				curseurselect[i] = new Rectangle(0, 0, 10, 10);
				curseurselect[i].layoutXProperty().bind(r.widthProperty().divide(C).multiply(i));
				curseurselect[i].heightProperty().bind(r.heightProperty());
				curseurselect[i].widthProperty().bind(r.widthProperty().divide(C));
				curseurselect[i].setFill(Color.TRANSPARENT);
				curseurselect[i].setStroke(Color.TRANSPARENT);
				curseurselect[i].setStrokeType(StrokeType.INSIDE);
				curseurselect[i].setStrokeWidth(12);
				curseurselect[i].setVisible(false);

				root1.getChildren().addAll(curseurselect[i]);
			}

			// conteneur de curseur de selection
			Rectangle r2 = new Rectangle(0, 0, 10, 10);
			r2.heightProperty().bind(r.heightProperty());
			r2.widthProperty().bind(r.widthProperty());
			r2.setFill(Color.TRANSPARENT);
			root1.getChildren().addAll(r2);

			precedent = -1; 
			r2.setOnMouseMoved(e -> {

				int val = (int) (e.getX() / (r.getWidth() / C));
				if (val != precedent) {
					curseurselect[val].setVisible(true);
					if (precedent > -1)
						curseurselect[precedent].setVisible(false);
				}
				precedent = val;

			});
			nbTour =2;
			// placement de pion apres un mouseclickes
			// evenement lors de clic sur le rectangle curseurselection return colonne ou le
			r2.setOnMouseClicked(e -> {
				// e.get(x) prend ou le mouseclicked s'est situe

				// e.get(x) : donne de 1--10 /700/nbc-->val (0..10)/10-->
				// 135 //700/7
				int colonne = (int) (e.getX() / (r.getWidth() / C));// sur quel rectangle le click s'est passe

				// placement du pion
				if (portepion[colonne][0].getStatus() == 0 && !winner1.isVisible() && !winner2.isVisible() && !egalite.isVisible()) {

					int newligne = L - 1;
					while (portepion[colonne][newligne].getStatus() != 0) {
						newligne--;
					}
					// portepion[colonne][newligne].resize(10, 10);

					portepion[colonne][newligne].set(nbTour % 2 == 1 ? 1 : 2);
					initial=Integer.parseInt(nbrTour());
					// conditions pour etre the winner

				
					int couleur = (nbTour % 2 == 1 ? 1 : 2);
					
					int max = 0;
					int x;
					int y;
					int somme;

					x = colonne;
					y = newligne;
					somme = -1;
					while (y >= 0 && x >= 0 && portepion[x][y].getStatus() == couleur) {
						y--;
						x--;
						somme++;
					}
					x = colonne;
					y = newligne;
					while (y < L && x < C && portepion[x][y].getStatus() == couleur) {
						y++;
						x++;
						somme++;
					}
					if (somme > max)
						max = somme;

					// diagonale haut droite et bas gauche
					x = colonne;
					y = newligne;
					somme = -1;
					while (y >= 0 && x < C && portepion[x][y].getStatus() == couleur) {
						y--;
						x++;
						somme++;
					}
					x = colonne;
					y = newligne;
					while (y < L && x >= 0 && portepion[x][y].getStatus() == couleur) {
						y++;
						x--;
						somme++;
					}
					if (somme > max)
						max = somme;

					// verticale
					x = colonne;
					y = newligne;
					somme = -1;
					while (y >= 0 && portepion[x][y].getStatus() == couleur) {
						y--;
						somme++;
					}
					y = newligne;
					while (y < L && portepion[x][y].getStatus() == couleur) {
						y++;
						somme++;
					}
					if (somme > max)
						max = somme;
					// horizontale
					x = colonne;
					y = newligne;
					somme = -1;
					while (x >= 0 && portepion[x][y].getStatus() == couleur) {
						x--;
						somme++;
					}
					x = colonne;
					while (x < C && portepion[x][y].getStatus() == couleur) {
						x++;
						somme++;
					}
					if (somme > max)
						max = somme;

					if (max >= N) {
						player1.setVisible(false);
						player2.setVisible(false);
						winner1.setVisible(true);
						winner2.setVisible(true);
						
						
						if (couleur == 1) {
							setWinOrEq(0,true);
							setWinOrEq(1,false);
						//	win1=true;
							winner1.setTextFill(Color.RED);
							winner1.setText("RED WON!");
							winner2.setTextFill(Color.BLACK);
							winner2.setText("BLACK LOST!");
							
						} else {
							setWinOrEq(0,false);
							setWinOrEq(1,true);
						//	win2=true;
							winner2.setTextFill(Color.BLACK);
							winner2.setText("BLACK WON!");
							winner1.setTextFill(Color.RED);
							winner1.setText("RED LOST!");
						
							
						}
						

						nbTour--;
						nbTour--;
						thread.stop();
					}

					if (nbTour % 2 == 1) 
						
						this.initialize(colonne, newligne, 1);
						
					else 
						this.initialize(colonne, newligne, 2);
						
					int[][] tableau = arayfromString(getTable());
					for (int i = 0; i < 7; i++) {
						for (int j = 0; j < 6; j++)

							portepion[i][j].set(tableau[i][j]);

					}

					nbTour++;
					nbTour++;
					
					if (initial > C * L && max < N) {
						setWinOrEq(2,true);
						setWinOrEq(0,false);
						setWinOrEq(1,false);
						//eq=true;
						VS.setVisible(false);
						egalite.setText("EGALITE !");
						egalite.setVisible(true);

						nbTour--;

					}

				}

			});

			primaryStage.setTitle("jeu pion AS");
			primaryStage.getIcons().add(new Image("file:/home/gadour/Games/client/src/pionjeuAS.png"));

			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
			thread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initialize(int ligne, int colonne, int value) {
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(clientConfig);
		WebResource webResource = client.resource("http://localhost:8081/add/" + ligne + "/" + colonne + "/" + value);

		ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		System.out.println("Output from Server .... \n");
		String output = response.getEntity(String.class);
		System.out.println(output);

	}

	
	public String getTable() {
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(clientConfig);
		WebResource webResource = client.resource("http://localhost:8081/getTable");

		ClientResponse response = webResource.type("application/json").accept("application/json")
				.get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		System.out.println("Output from Server .... \n");
		String output = response.getEntity(String.class);
		System.out.println(output);
		
		return output;
	}

	public int[][] arayfromString(String s) {
		String[][] arr = Arrays.stream(s.substring(2, s.length() - 2).split("\\],\\["))
				.map(e -> Arrays.stream(e.split("\\s*,\\s*")).toArray(String[]::new)).toArray(String[][]::new);

		int[][] intTable = new int[7][6];

		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 6; j++)
				// ou on va trouver 0 ou 1 ou 2
				intTable[i][j] = Integer.valueOf(arr[i][j]);
		}

		return intTable;
	}
	public String nbrTour() {
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(clientConfig);
		WebResource webResource = client.resource("http://localhost:8081/nbrTour");

		ClientResponse response = webResource.type("application/json").accept("application/json")
				.get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		
		System.out.println("nbrTour: ");
		String output = response.getEntity(String.class);
		System.out.println(output);
		// output : string
		return output;
	}

	public void reset() {
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(clientConfig);
		WebResource webResource = client.resource("http://localhost:8081/reset");

		ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		System.out.println("RESET GAME .... \n");
		String output = response.getEntity(String.class);
		System.out.println(output);
		
	

	}
	public String isReset() {
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(clientConfig);
		WebResource webResource = client.resource("http://localhost:8081/isReset");

		ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		//System.out.println("RESET GAME .... \n");
		String output = response.getEntity(String.class);
		//System.out.println(output);
		
		return output;

	}
	public void setResetToFalse() {
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(clientConfig);
		WebResource webResource = client.resource("http://localhost:8081/setRe");

		ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		
	}
	
	public void setWinOrEq(int indice, boolean value) {
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(clientConfig);
		
		WebResource webResource = client.resource("http://localhost:8081/setWinOrEq/" + indice + "/" + value );

		ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		//System.out.println("RESET GAME .... \n");
		//String output = response.getEntity(String.class);
		//System.out.println(output);
		
	}
	public String getWinOrEqTab() {
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(clientConfig);
		WebResource webResource = client.resource("http://localhost:8081/getWinOreEq");

		ClientResponse response = webResource.type("application/json").accept("application/json")
				.get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		
		String output = response.getEntity(String.class);
		System.out.println(output);
		// output : string
		return output;
	}
	
	public boolean[][] arayfromBool(String s) {
		String[][] arr = Arrays.stream(s.substring(2, s.length() - 2).split("\\],\\["))
				.map(e -> Arrays.stream(e.split("\\s*,\\s*")).toArray(String[]::new)).toArray(String[][]::new);

		boolean[][] boolTable = new boolean[3][1];

			for (int i = 0; i < 3; i++)
				boolTable[i][0] = Boolean.valueOf(arr[i][0]);
		

		return boolTable;
	}
	
	public void resetWinOrEq() {
		DefaultClientConfig clientConfig = new DefaultClientConfig();
		com.sun.jersey.api.client.Client client = com.sun.jersey.api.client.Client.create(clientConfig);
		WebResource webResource = client.resource("http://localhost:8081/resetWinOrEq");

		ClientResponse response = webResource.type("application/json").get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		
	}
	public static void main(String[] args) {
		launch(args);
	}
}
