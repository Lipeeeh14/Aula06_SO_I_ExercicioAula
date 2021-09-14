package view;

import java.util.Random;
import java.util.concurrent.Semaphore;

import controller.ThreadShowController;

public class Main {

	public static Semaphore semaforo;
	
	public static void main(String[] args) {
		semaforo = new Semaphore(1);
		Random tempoSistema = new Random();
		
		for (int i = 0; i < 300; i++) {
			Thread show = new ThreadShowController(tempoSistema, i, semaforo);
			show.start();
		}
	}

}
