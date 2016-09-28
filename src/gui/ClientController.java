package gui;

import clientserver.Client;

public class ClientController {
	Client client;
	GUI gui;
	
	public ClientController(Client c){
		this.client = c;
		gui = new GUI(this);
	}

	public String getName(){
		//Return username from client
		return "Jack";
	}
	
	public int getBananaCount(){
		//get player banana count
		return 0;
	}
	
	public void rotateLeft(){
		//rotate camera left
		System.out.println("rotate left");
	}
	
	public void rotateRight(){
		//rotate camera right
		System.out.println("rotate right");
	}
}